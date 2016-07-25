package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors.ActorsCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.GetNodeCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.GetActorsCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.GetNodeCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.GetNodeCatalogResponseProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class GetActorCatalogResponseProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(GetActorCatalogResponseProcessor.class));

    /**
     * Constructor
     */
    public GetActorCatalogResponseProcessor() {
        super(PackageType.GET_ACTOR_CATALOG_RESPONSE);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        GetActorsCatalogResponse messageContent = GetActorsCatalogResponse.parseContent(packageReceived.getContent());

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            if (messageContent.getStatus() == GetNodeCatalogResponse.STATUS.SUCCESS){

                 /*
                 * Get the block of transactions
                 */
                List<ActorsCatalog>  transactionList = messageContent.getActorsCatalogList();

                long totalRowInDb = getDaoFactory().getActorsCatalogDao().getAllCount();

                LOG.info("Row in node catalog  = "+totalRowInDb);
                LOG.info("nodesCatalog size = "+transactionList.size());

                for (ActorsCatalog actorsCatalog : transactionList)
                    processCatalogUpdate(actorsCatalog);

                totalRowInDb = getDaoFactory().getActorsCatalogDao().getAllCount();

                LOG.info("Row in node catalog  = "+totalRowInDb);
                LOG.info("Row in catalog seed node = "+messageContent.getCount());

                if (totalRowInDb < messageContent.getCount()){

                    LOG.info("Requesting more transactions.");

                    GetNodeCatalogRequest getNodeCatalogRequest = new GetNodeCatalogRequest((int) totalRowInDb, ActorsCatalogPropagationConfiguration.MAX_REQUESTABLE_ITEMS);
                    Package packageRespond = Package.createInstance(getNodeCatalogRequest.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.GET_ACTOR_CATALOG_REQUEST, channelIdentityPrivateKey, destinationIdentityPublicKey);

                    /*
                     * Send the respond
                     */
                    session.getAsyncRemote().sendObject(packageRespond);

                } else {

                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Process finish..."));
                }

            } else {

                LOG.warn(messageContent.getStatus() + " - " + messageContent.getDetails());
            }

        } catch (Exception exception){

            LOG.info(FermatException.wrapException(exception).toString());
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, exception.getMessage()));
            } catch (IOException e) {
                LOG.info(FermatException.wrapException(e).toString());
            }
        }

    }

    /**
     * Process the transaction
     */
    private synchronized void processCatalogUpdate(ActorsCatalog actorCatalogToAddOrUpdate) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, InvalidParameterException, CantInsertRecordDataBaseException {

        LOG.info("Executing method processCatalogUpdate");

        try {

            ActorsCatalog actorsCatalog = getDaoFactory().getActorsCatalogDao().findById(actorCatalogToAddOrUpdate.getIdentityPublicKey());

                /*
                 * If version in our node catalog is minor to the version in the remote catalog then I will update it.
                 */
            if (actorsCatalog.getVersion() < actorCatalogToAddOrUpdate.getVersion()) {

                getDaoFactory().getActorsCatalogDao().update(actorCatalogToAddOrUpdate, actorCatalogToAddOrUpdate.getVersion(), actorCatalogToAddOrUpdate.getLastUpdateType(), 0);
            }

        } catch (RecordNotFoundException recordNotFoundException) {

            getDaoFactory().getActorsCatalogDao().create(actorCatalogToAddOrUpdate, actorCatalogToAddOrUpdate.getVersion(), actorCatalogToAddOrUpdate.getLastUpdateType(), 0);
        }
    }

}

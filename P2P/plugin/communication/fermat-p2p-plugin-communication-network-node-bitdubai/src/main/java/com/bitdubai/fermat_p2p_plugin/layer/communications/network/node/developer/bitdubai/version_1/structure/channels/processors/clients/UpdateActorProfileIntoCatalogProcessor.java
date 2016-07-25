package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.UpdateActorProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.UpdateProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantCreateTransactionStatementPairException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ThumbnailUtil;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.sql.Timestamp;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.AddActorIntoCatalogProcessor</code>
 * process all packages received the type <code>MessageType.UPDATE_ACTOR_PROFILE_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 20/06/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class UpdateActorProfileIntoCatalogProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(UpdateActorProfileIntoCatalogProcessor.class));

    /**
     * Represent the nodeIdentity
     */
    private String nodeIdentity = ((NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT)).getIdentity().getPublicKey();

    /**
     * Constructor
     */
    public UpdateActorProfileIntoCatalogProcessor() {
        super(PackageType.UPDATE_ACTOR_PROFILE_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        UpdateActorProfileMsgRequest messageContent = UpdateActorProfileMsgRequest.parseContent(packageReceived.getContent());
        ActorProfile actorProfile = (ActorProfile) messageContent.getProfileToUpdate();
        UpdateProfileMsjRespond updateProfileMsjRespond = null;

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Validate if exist
             */
            if (JPADaoFactory.getActorCatalogDao().exist(actorProfile.getIdentityPublicKey())){

                LOG.info("Existing profile");

                boolean hasChanges = validateProfileChange(actorProfile);

                LOG.info("hasChanges = "+hasChanges);

                if (hasChanges){

                    Timestamp currentMillis = new Timestamp(System.currentTimeMillis());
                    LOG.info("Updating profile");

                    ActorCatalog actorCatalog = createActorCatalogInstance(actorProfile, currentMillis);

                    JPADaoFactory.getActorCatalogDao().update(actorCatalog);

                    /*
                     * If all ok, respond whit success message
                     */
                    updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.SUCCESS, MsgRespond.STATUS.SUCCESS.toString(), actorProfile.getIdentityPublicKey());
                }

            } else {

                updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, "The actor profile no exist", actorProfile.getIdentityPublicKey());

            }

            /*
             * Send the respond
             */
            channel.sendPackage(session, updateProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_ACTOR_PROFILE_RESPONSE, destinationIdentityPublicKey);

            LOG.info("Process finish");

        } catch (Exception exception){

            try {

                LOG.error(exception);
                updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, exception.getCause().getMessage(), actorProfile.getIdentityPublicKey());
                channel.sendPackage(session, updateProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_ACTOR_PROFILE_RESPONSE, destinationIdentityPublicKey);

            }catch (Exception e) {
               LOG.error(e);
            }
        }

    }

    /**
     * Update a row into the data base
     *
     * @param actorProfile
     *
     * @throws CantCreateTransactionStatementPairException if something goes wrong.
     */
    private ActorCatalog createActorCatalogInstance(ActorProfile actorProfile, Timestamp currentMillis) throws IOException, CantReadRecordDataBaseException {

        /*
         * Create the actorCatalog
         */
        NodeCatalog nodeCatalog = null;

        try {
            nodeCatalog = JPADaoFactory.getNodeCatalogDao().findById(nodeIdentity);
        } catch (CantReadRecordDataBaseException e) {
            LOG.error("find Node Catalog in ActorProfile", e);
        }

        ActorCatalog actorCatalog = new ActorCatalog(actorProfile, ThumbnailUtil.generateThumbnail(actorProfile.getPhoto(),"JPG"), nodeCatalog, "");

        Client client = JPADaoFactory.getClientDao().findById(actorProfile.getClientIdentityPublicKey());

        actorCatalog.setClient(client);

        /*
         * Save into the data base
         */
        return actorCatalog;
    }

    /**
     * Validate if the profile register have changes
     *
     * @param actorProfile
     * @return boolean
     * @throws CantReadRecordDataBaseException
     * @throws RecordNotFoundException
     */
    private boolean validateProfileChange(ActorProfile actorProfile) throws CantReadRecordDataBaseException, RecordNotFoundException, IOException {

        /*
         * Create the actorsCatalog
         */

        //Home Node
        NodeCatalog nodeCatalog = null;

        try {
            nodeCatalog = JPADaoFactory.getNodeCatalogDao().findById(nodeIdentity);
        } catch (CantReadRecordDataBaseException e) {
            LOG.error("find Node Catalog in ActorProfile", e);;
        }

        ActorCatalog actorCatalog = new ActorCatalog(actorProfile, ThumbnailUtil.generateThumbnail(actorProfile.getPhoto(),"JPG"), nodeCatalog, "");

        //Client
        Client client = JPADaoFactory.getClientDao().findById(actorProfile.getClientIdentityPublicKey());

        actorCatalog.setClient(client);

        ActorCatalog actorsCatalogRegister = JPADaoFactory.getActorCatalogDao().findById(actorProfile.getIdentityPublicKey());

        return !actorsCatalogRegister.equals(actorCatalog);
    }
}

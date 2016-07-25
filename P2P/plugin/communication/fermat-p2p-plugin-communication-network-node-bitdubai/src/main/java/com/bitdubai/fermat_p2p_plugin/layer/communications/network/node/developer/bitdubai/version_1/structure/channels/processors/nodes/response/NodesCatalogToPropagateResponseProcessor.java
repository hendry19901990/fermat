package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToAddOrUpdateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.NodesCatalogToPropagateResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.NodesCatalogToPropagateRequestProcessor</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogToPropagateResponseProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NodesCatalogToPropagateResponseProcessor.class));

    /**
     * Constructor
     */
    public NodesCatalogToPropagateResponseProcessor() {
        super(PackageType.NODES_CATALOG_TO_PROPAGATE_RESPONSE);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public synchronized void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        NodesCatalogToPropagateResponse messageContent = NodesCatalogToPropagateResponse.parseContent(packageReceived.getContent());

        List<PropagationInformation> propagationInformationResponseListReceived = messageContent.getPropagationInformationResponseList();

        try {

            LOG.info("ResponseProcessor ->: propagationInformationResponseListReceived.size() -> "+propagationInformationResponseListReceived.size());

            List<NodesCatalog> nodesCatalogList = new ArrayList<>();

            Boolean lateNotification = Boolean.FALSE;

            for (PropagationInformation propagationInformation : propagationInformationResponseListReceived) {

                try {

                    NodesCatalog nodesCatalog = getDaoFactory().getNodesCatalogDao().findById(propagationInformation.getId());

                    /*
                     * If version in our node catalog is minor to the version in the remote catalog then I would request for it.
                     * If version in our node catalog is major to the version in the remote catalog then I would send it.
                     * else no action needed
                     */
                    if (propagationInformation.getVersion() == null || nodesCatalog.getVersion() > propagationInformation.getVersion()) {
                        nodesCatalogList.add(nodesCatalog);
                    } else if (propagationInformation.getVersion() != null && nodesCatalog.getVersion().equals(propagationInformation.getVersion())) {
                        lateNotification = Boolean.TRUE;
                    }

                    getDaoFactory().getNodesCatalogDao().decreasePendingPropagationsCounter(propagationInformation.getId());

                } catch (RecordNotFoundException recordNotFoundException) {
                    // no action here
                }
            }

            if (lateNotification) {
                try {
                    getDaoFactory().getNodesCatalogDao().increaseLateNotificationCounter(destinationIdentityPublicKey);
                } catch (Exception e) {
                    LOG.info("ResponseProcessor ->: Unexpected error trying to update the late notification counter -> "+e.getMessage());
                }
            }

            LOG.info("ResponseProcessor ->: nodesCatalogList.size() -> " +nodesCatalogList.size());

            NodesCatalogToAddOrUpdateRequest addNodeToCatalogResponse = new NodesCatalogToAddOrUpdateRequest(nodesCatalogList);
            Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.NODES_CATALOG_TO_ADD_OR_UPDATE_REQUEST, channelIdentityPrivateKey, destinationIdentityPublicKey);

            /*
             * Send the respond
             */
            session.getAsyncRemote().sendObject(packageRespond);

        } catch (Exception exception){

            try {

                LOG.info(FermatException.wrapException(exception).toString());
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Can't process NODES_CATALOG_TO_PROPAGATE_RESPONSE. ||| "+ exception.getMessage()));

            } catch (Exception e) {
                LOG.info(FermatException.wrapException(e).toString());
            }

        }
    }

}

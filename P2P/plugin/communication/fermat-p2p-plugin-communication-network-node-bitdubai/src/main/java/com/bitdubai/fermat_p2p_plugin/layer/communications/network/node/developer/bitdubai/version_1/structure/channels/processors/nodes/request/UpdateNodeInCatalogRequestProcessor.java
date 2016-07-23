package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.NodesCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.AddNodeToCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.UpdateNodeInCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.sql.Timestamp;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.UpdateNodeInCatalogRequestProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class UpdateNodeInCatalogRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(UpdateNodeInCatalogRequestProcessor.class.getName()));

    /**
     * Constructor
     */
    public UpdateNodeInCatalogRequestProcessor() {
        super(PackageType.UPDATE_NODE_IN_CATALOG_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint )
     */
    @Override
    public synchronized void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        AddNodeToCatalogRequest messageContent = AddNodeToCatalogRequest.parseContent(packageReceived.getContent());

        NodeProfile nodeProfile = messageContent.getNodeProfile();
        UpdateNodeInCatalogResponse updateNodeInCatalogResponse;

        try {

            if (!getDaoFactory().getNodesCatalogDao().exists(nodeProfile.getIdentityPublicKey())){

                LOG.info("The node profile to update no exist");

                /*
                 * Notify the node already exist
                 */
                updateNodeInCatalogResponse = new UpdateNodeInCatalogResponse(UpdateNodeInCatalogResponse.STATUS.FAIL, "The node profile to update does not exist", nodeProfile, Boolean.FALSE);

            } else {

                LOG.info("Updating ...");

                /*
                 * Insert NodesCatalog into data base
                 */
                NodesCatalog nodesCatalog = createUpdateNodeCatalogRecord(nodeProfile);

                getDaoFactory().getNodesCatalogDao().update(nodesCatalog, null, NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

                /*
                 * If all ok, respond whit success message
                 */
                updateNodeInCatalogResponse = new UpdateNodeInCatalogResponse(UpdateNodeInCatalogResponse.STATUS.SUCCESS, UpdateNodeInCatalogResponse.STATUS.SUCCESS.toString(), nodeProfile, Boolean.TRUE);
            }

            Package packageRespond = Package.createInstance(updateNodeInCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_NODE_IN_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

            /*
             * Send the respond
             */
            session.getAsyncRemote().sendObject(packageRespond);

            LOG.info("Processing finish");

        } catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                updateNodeInCatalogResponse = new UpdateNodeInCatalogResponse(UpdateNodeInCatalogResponse.STATUS.EXCEPTION, exception.getLocalizedMessage(), nodeProfile, Boolean.FALSE);
                Package packageRespond = Package.createInstance(updateNodeInCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_NODE_IN_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.error(e);
            }

        }

    }

    /**
     * Update a row into the data base
     *
     * @param nodeProfile
     */
    private NodesCatalog createUpdateNodeCatalogRecord(NodeProfile nodeProfile) {

        /*
         * Create the NodesCatalog
         */
        NodesCatalog nodeCatalog = new NodesCatalog();
        nodeCatalog.setIp(nodeProfile.getIp());
        nodeCatalog.setDefaultPort(nodeProfile.getDefaultPort());
        nodeCatalog.setIdentityPublicKey(nodeProfile.getIdentityPublicKey());
        nodeCatalog.setName(nodeProfile.getName());
        nodeCatalog.setOfflineCounter(0);
        nodeCatalog.setLastConnectionTimestamp(new Timestamp(System.currentTimeMillis()));
        nodeCatalog.setLastLocation(nodeProfile.getLocation());

        return nodeCatalog;
    }

}

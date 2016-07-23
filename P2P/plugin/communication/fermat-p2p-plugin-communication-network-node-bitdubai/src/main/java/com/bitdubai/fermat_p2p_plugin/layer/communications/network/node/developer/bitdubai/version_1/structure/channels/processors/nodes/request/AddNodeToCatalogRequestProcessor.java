package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.NodesCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.AddNodeToCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.AddNodeToCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.sql.Timestamp;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.AddNodeToCatalogRequestProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class AddNodeToCatalogRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(AddNodeToCatalogRequestProcessor.class));

    /**
     * Constructor
     */
    public AddNodeToCatalogRequestProcessor() {
        super(PackageType.ADD_NODE_TO_CATALOG_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint )
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        AddNodeToCatalogRequest messageContent = AddNodeToCatalogRequest.parseContent(packageReceived.getContent());

        NodeProfile nodeProfile = messageContent.getNodeProfile();

        AddNodeToCatalogResponse addNodeToCatalogResponse = null;

        try {

            if (getDaoFactory().getNodesCatalogDao().exists(nodeProfile.getIdentityPublicKey())){

                /*
                 * Notify the node already exist
                 */
                addNodeToCatalogResponse = new AddNodeToCatalogResponse(AddNodeToCatalogResponse.STATUS.FAIL, "The node profile already exist", nodeProfile, Boolean.TRUE);

            } else {

                try {

                    NodesCatalog nodesCatalog = createNodesCatalogRecord(nodeProfile);
                    getDaoFactory().getNodesCatalogDao().create(nodesCatalog, 0, NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

                    /*
                     * If all ok, respond whit success message
                     */
                    addNodeToCatalogResponse = new AddNodeToCatalogResponse(AddNodeToCatalogResponse.STATUS.SUCCESS, AddNodeToCatalogResponse.STATUS.SUCCESS.toString(), nodeProfile, null);

                } catch (CantInsertRecordDataBaseException exception) {

                    exception.printStackTrace();
                    LOG.error(exception.getMessage());
                    addNodeToCatalogResponse = new AddNodeToCatalogResponse(AddNodeToCatalogResponse.STATUS.EXCEPTION, exception.getMessage(), nodeProfile, Boolean.FALSE);
                }
            }

            Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ADD_NODE_TO_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

            /*
             * Send the respond
             */
            session.getAsyncRemote().sendObject(packageRespond);

            LOG.info("Processing finish");

        } catch (Exception exception){

            try {

                exception.printStackTrace();
                LOG.error(exception.getMessage());

                /*
                 * Respond whit fail message
                 */
                addNodeToCatalogResponse = new AddNodeToCatalogResponse(AddNodeToCatalogResponse.STATUS.EXCEPTION, exception.getLocalizedMessage(), nodeProfile, Boolean.FALSE);
                Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ADD_NODE_TO_CATALOG_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
    }

    /**
     * Create a new node catalog record
     *
     * @param nodeProfile
     */
    private NodesCatalog createNodesCatalogRecord(NodeProfile nodeProfile) {

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

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.NodesCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToAddOrUpdateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

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
public class NodesCatalogToAddOrUpdateRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NodesCatalogToAddOrUpdateRequestProcessor.class));

    /**
     * Constructor
     */
    public NodesCatalogToAddOrUpdateRequestProcessor() {
        super(PackageType.NODES_CATALOG_TO_ADD_OR_UPDATE_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public synchronized void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: " + packageReceived.getPackageType());

        NodesCatalogToAddOrUpdateRequest messageContent = NodesCatalogToAddOrUpdateRequest.parseContent(packageReceived.getContent());

        List<NodesCatalog> nodesCatalogList = messageContent.getNodesCatalogList();

        try {

            LOG.info("NodesCatalogToAddOrUpdateRequestProcessor ->: nodesCatalogList.size() -> "+(nodesCatalogList != null ? nodesCatalogList.size() : null));

            for (NodesCatalog nodesCatalogToAddOrUpdate : nodesCatalogList) {

                try {

                    NodesCatalog nodesCatalog = getDaoFactory().getNodesCatalogDao().findById(nodesCatalogToAddOrUpdate.getIdentityPublicKey());

                    /*
                     * If version in our node catalog is minor to the version in the remote catalog then I will update it.
                     * else no action needed
                     */
                    if (nodesCatalog.getVersion() < nodesCatalogToAddOrUpdate.getVersion()) {

                        getDaoFactory().getNodesCatalogDao().update(nodesCatalogToAddOrUpdate, nodesCatalogToAddOrUpdate.getVersion(), NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);
                    }

                } catch (RecordNotFoundException recordNotFoundException) {

                    getDaoFactory().getNodesCatalogDao().create(nodesCatalogToAddOrUpdate, nodesCatalogToAddOrUpdate.getVersion(), NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);
                }
            }

            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "There's no more information to exchange."));

        } catch (Exception exception){

            try {

                LOG.info(FermatException.wrapException(exception).toString());
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Can't process NODES_CATALOG_TO_PROPAGATE_REQUEST. ||| "+ exception.getMessage()));

            } catch (Exception e) {
                LOG.info(FermatException.wrapException(e).toString());
            }

        }

    }

}

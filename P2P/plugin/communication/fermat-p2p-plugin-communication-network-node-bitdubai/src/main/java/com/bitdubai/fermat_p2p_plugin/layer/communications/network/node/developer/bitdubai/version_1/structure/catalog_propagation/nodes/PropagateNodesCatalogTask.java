package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.clients.FermatWebSocketClientNodeChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToPropagateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodePropagationInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.PropagateNodesCatalogTask</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class PropagateNodesCatalogTask implements Runnable {

    /**
     * Represents the LOGGER entity
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(PropagateNodesCatalogTask.class));

    /**
     * Represents the networkNodePluginRoot
     */
    private NetworkNodePluginRoot networkNodePluginRoot;

    /**
     * Constructor
     */
    public PropagateNodesCatalogTask(final NetworkNodePluginRoot networkNodePluginRoot){

        this.networkNodePluginRoot = networkNodePluginRoot;
    }

    /**
     * (non-javadoc)
     * @see Runnable#run()
     */
    @Override
    public void run() {
        try {
            propagateNodesCatalog();
        } catch (Exception e) {
            LOG.error("Unhandled error during propagation.", e);
        }
    }

    /**
     * Propagation logic implementation
     *
     * Asks if there are items to share and nodes to share with
     *
     * If there are both, then list the records to share having in count the MAX_RECORDS_TO_PROPAGATE and the quantity of nodes (to avoid sending the information again and again).
     *
     * Creates the message.
     *
     * Send the message to the nodes.
     *
     * If there's an error sending the message, marks the node with the offline counter plus one.
     *
     * Finally marks the nodecatalog record as tried to propagate one more time (for each message sent).
     */
    private synchronized void propagateNodesCatalog() throws Exception {

        LOG.info("Executing node propagateNodesCatalog()");

        Integer currentNodesInCatalog = JPADaoFactory.getNodeCatalogDao().getCountOfNodesToPropagateWith(networkNodePluginRoot.getIdentity().getPublicKey());

        LOG.info("Executing node propagation: currentNodesInCatalog: "+currentNodesInCatalog);

        if (currentNodesInCatalog > 0) {

            Integer countOfItemsToShare = JPADaoFactory.getNodeCatalogDao().getCountOfItemsToShare(currentNodesInCatalog);

            LOG.info("Executing node propagation: countOfItemsToShare: "+countOfItemsToShare);

            if (countOfItemsToShare > 0) {

                List<NodeCatalog> itemsToShare = JPADaoFactory.getNodeCatalogDao().listItemsToShare(currentNodesInCatalog);

                List<NodePropagationInformation> itemInformationToShareList = new ArrayList<>();

                for (NodeCatalog nodeCatalog : itemsToShare)
                    itemInformationToShareList.add(new NodePropagationInformation(nodeCatalog.getId(), nodeCatalog.getVersion()));

                NodesCatalogToPropagateRequest nodesCatalogToPropagateRequest = new NodesCatalogToPropagateRequest(itemInformationToShareList);

                String messageContent = nodesCatalogToPropagateRequest.toJson();

                FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel;

                List<NodeCatalog> nodesCatalogList = JPADaoFactory.getNodeCatalogDao().listNodesToPropagateWith(
                        networkNodePluginRoot.getIdentity().getPublicKey(),
                        NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS,
                        0
                );

                for (final NodeCatalog nodeCatalogToPropagateWith : nodesCatalogList) {

                    try {

                        fermatWebSocketClientNodeChannel = new FermatWebSocketClientNodeChannel(nodeCatalogToPropagateWith);

                        if (fermatWebSocketClientNodeChannel.sendMessage(messageContent, PackageType.NODES_CATALOG_TO_PROPAGATE_REQUEST)) {

                            for (NodePropagationInformation nodePropagationInformation : itemInformationToShareList)
                                JPADaoFactory.getNodeCatalogDao().increaseTriedToPropagateTimes(nodePropagationInformation.getId());
                        }

                    } catch (CantUpdateRecordDataBaseException exception) {

                        LOG.error("Error trying to update the propagation information: "  +nodeCatalogToPropagateWith, exception);

                    } catch (Exception e) {

                        JPADaoFactory.getNodeCatalogDao().changeOfflineCounter(
                                nodeCatalogToPropagateWith.getId(),
                                nodeCatalogToPropagateWith.getOfflineCounter() + 1
                        );

                        for (NodePropagationInformation nodePropagationInformation : itemInformationToShareList)
                            JPADaoFactory.getNodeCatalogDao().increaseTriedToPropagateTimes(nodePropagationInformation.getId());

                        LOG.error("Error trying to send NODES_CATALOG_TO_PROPAGATE_REQUEST message to the node: "  +nodeCatalogToPropagateWith, e);
                    }
                }
            } else {

                LOG.info("No nodes in catalog to propagate with.");
            }
        } else {

            LOG.info("No information to propagate in nodes catalog.");
        }
    }
}

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.clients.FermatWebSocketClientNodeChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.ActorCatalogToPropagateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.PropagateActorsCatalogTask</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class PropagateActorsCatalogTask implements Runnable {

    /**
     * Represents the LOGGER entity
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(PropagateActorsCatalogTask.class));

    /**
     * Represents the networkNodePluginRoot
     */
    private NetworkNodePluginRoot networkNodePluginRoot;

    /**
     * Constructor
     */
    public PropagateActorsCatalogTask(final NetworkNodePluginRoot networkNodePluginRoot ){

        this.networkNodePluginRoot = networkNodePluginRoot;
    }

    /**
     * (non-javadoc)
     * @see Runnable#run()
     */
    @Override
    public void run() {
        try {
            propagateActorsCatalog();
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
    private synchronized void propagateActorsCatalog() throws Exception {

        LOG.info("Executing node propagateActorsCatalog()");

        Integer currentNodesInCatalog = JPADaoFactory.getNodeCatalogDao().getCountOfNodesToPropagateWith(networkNodePluginRoot.getIdentity().getPublicKey());

        LOG.info("Executing node propagation: currentNodesInCatalog: "+currentNodesInCatalog);

        if (currentNodesInCatalog > 0) {

            Integer countOfItemsToShare = JPADaoFactory.getActorCatalogDao().getCountOfItemsToShare(currentNodesInCatalog);

            LOG.info("Executing node propagation: countOfItemsToShare: "+countOfItemsToShare);

            if (countOfItemsToShare > 0) {

                List<ActorCatalog> itemsToShareList = JPADaoFactory.getActorCatalogDao().listItemsToShare(currentNodesInCatalog);
                List<ActorPropagationInformation> informationToShareList = new ArrayList<>();

                for (ActorCatalog actorCatalog : itemsToShareList)
                    informationToShareList.add(new ActorPropagationInformation(actorCatalog.getId(), actorCatalog.getVersion(), actorCatalog.getLastUpdateType()));

                ActorCatalogToPropagateRequest nodesCatalogToPropagateRequest = new ActorCatalogToPropagateRequest(informationToShareList);

                String messageContent = nodesCatalogToPropagateRequest.toJson();

                FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel;

                List<NodeCatalog> nodesCatalogList = JPADaoFactory.getNodeCatalogDao().listNodesToPropagateWith(
                        networkNodePluginRoot.getIdentity().getPublicKey(),
                        ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS,
                        0
                );

                for (final NodeCatalog nodeCatalogToPropagateWith : nodesCatalogList) {

                    try {

                        fermatWebSocketClientNodeChannel = new FermatWebSocketClientNodeChannel(nodeCatalogToPropagateWith);

                        fermatWebSocketClientNodeChannel.sendMessage(messageContent, PackageType.ACTOR_CATALOG_TO_PROPAGATE_REQUEST);

                    } catch (Exception e) {

                        JPADaoFactory.getNodeCatalogDao().changeOfflineCounter(
                                nodeCatalogToPropagateWith.getId(),
                                nodeCatalogToPropagateWith.getOfflineCounter() + 1
                        );

                        LOG.error("Error trying to send ACTOR_CATALOG_TO_PROPAGATE_REQUEST message to the node: "  +nodeCatalogToPropagateWith, e);
                    }


                    for (ActorPropagationInformation actorPropagationInformation : informationToShareList)
                        JPADaoFactory.getActorCatalogDao().increaseTriedToPropagateTimes(actorPropagationInformation.getId());
                }
            } else {

                LOG.info("No nodes in catalog to propagate with.");
            }
        } else {

            LOG.info("No information to propagate in nodes catalog.");
        }
    }
}

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.layer.all_definition.enums.AgentStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.clients.FermatWebSocketClientNodeChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToPropagateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogPropagationInformationDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.PropagateNodesCatalogAgent</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class PropagateNodesCatalogAgent extends FermatAgent {

    /**
     * Represents the LOGGER entity
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(PropagateNodesCatalogAgent.class));

    /**
     * Represents the scheduledThreadPool
     */
    private ScheduledExecutorService scheduledThreadPool;

    /**
     * Represents the scheduledFutures
     */
    private List<ScheduledFuture> scheduledFutures;

    /**
     * Represents the networkNodePluginRoot
     */
    private NetworkNodePluginRoot networkNodePluginRoot;

    /**
     * Represents the nodesCatalogDao
     */
    private NodesCatalogDao nodesCatalogDao;

    /**
     * Represents the networkNodePluginRoot
     */
    private NodesCatalogPropagationInformationDao nodesCatalogPropagationInformationDao;

    /**
     * Constructor
     */
    public PropagateNodesCatalogAgent(final NetworkNodePluginRoot networkNodePluginRoot,
                                      final DaoFactory            daoFactory           ){

        this.networkNodePluginRoot                 = networkNodePluginRoot;
        this.nodesCatalogDao                       = daoFactory.getNodesCatalogDao();
        this.nodesCatalogPropagationInformationDao = daoFactory.getNodesCatalogPropagationInformationDao();
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

        long currentNodesInCatalog = nodesCatalogDao.getCountOfNodesToPropagateWith(networkNodePluginRoot.getIdentity().getPublicKey());

        LOG.info("Executing node propagation: currentNodesInCatalog: "+currentNodesInCatalog);

        if (currentNodesInCatalog > 0) {

            long countOfItemsToShare = nodesCatalogPropagationInformationDao.getCountOfItemsToShare(currentNodesInCatalog);

            LOG.info("Executing node propagation: countOfItemsToShare: "+countOfItemsToShare);

            if (countOfItemsToShare > 0) {

                List<PropagationInformation> itemsToShare = nodesCatalogPropagationInformationDao.listItemsToShare(currentNodesInCatalog);

                NodesCatalogToPropagateRequest nodesCatalogToPropagateRequest = new NodesCatalogToPropagateRequest(itemsToShare);

                String messageContent = nodesCatalogToPropagateRequest.toJson();

                FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel;

                List<NodesCatalog> nodesCatalogList = nodesCatalogDao.listNodesToPropagateWith(
                        networkNodePluginRoot.getIdentity().getPublicKey(),
                        NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS,
                        0
                );

                for (final NodesCatalog nodeCatalogToPropagateWith : nodesCatalogList) {

                    try {

                        fermatWebSocketClientNodeChannel = new FermatWebSocketClientNodeChannel(nodeCatalogToPropagateWith);

                        if (fermatWebSocketClientNodeChannel.sendMessage(messageContent, PackageType.NODES_CATALOG_TO_PROPAGATE_REQUEST)) {

                            for (PropagationInformation propagationInformation : itemsToShare)
                                nodesCatalogPropagationInformationDao.increaseTriedToPropagateTimes(propagationInformation);
                        }

                    } catch (CantUpdateRecordDataBaseException | RecordNotFoundException exception) {

                        LOG.error("Error trying to update the propagation information: "  +nodeCatalogToPropagateWith, exception);

                    } catch (Exception e) {

                        nodesCatalogDao.setOfflineCounter(
                                nodeCatalogToPropagateWith.getIdentityPublicKey(),
                                nodeCatalogToPropagateWith.getOfflineCounter() + 1
                        );

                        for (PropagationInformation propagationInformation : itemsToShare)
                            nodesCatalogPropagationInformationDao.increaseTriedToPropagateTimes(propagationInformation);

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

    /**
     * (non-javadoc)
     * @see FermatAgent#start()
     */
    @Override
    public synchronized void start() throws CantStartAgentException {

        LOG.info("Starting propagate nodes catalog agent.");
        try {

            if(this.isStarted())
                return;

            if(this.isPaused()) {
                this.resume();
                return;
            }

            this.scheduledThreadPool   = Executors.newScheduledThreadPool(2);
            this.scheduledFutures      = new ArrayList<>();

            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PropagationTask(), NodesCatalogPropagationConfiguration.PROPAGATION_INITIAL_DELAY, NodesCatalogPropagationConfiguration.PROPAGATION_INTERVAL, TimeUnit.SECONDS));
            this.status = AgentStatus.STARTED;

        } catch (CantStartAgentException exception) {

            LOG.error("Unhandled error trying to start the agent.", exception);
            throw exception;
        } catch (Exception exception) {

            LOG.error("Unhandled error trying to start the agent.", exception);
            throw new CantStartAgentException(exception, null, "You should inspect the cause.");
        }
    }

    /**
     * (non-javadoc)
     * @see FermatAgent#resume()
     */
    public synchronized void resume() throws CantStartAgentException {

        LOG.info("Resuming propagate nodes catalog agent.");
        try {

            if(this.isStarted())
                return;

            if(this.isStopped())
                throw new CantStartAgentException("The agent is stopped, can't resume it, you should start it.");

            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PropagationTask(), NodesCatalogPropagationConfiguration.PROPAGATION_INITIAL_DELAY, NodesCatalogPropagationConfiguration.PROPAGATION_INTERVAL, TimeUnit.SECONDS));
            this.status = AgentStatus.STARTED;

        } catch (CantStartAgentException exception) {

            LOG.error("Unhandled error trying to resume the agent.", exception);
            throw exception;
        } catch (Exception exception) {

            LOG.error("Unhandled error trying to resume the agent.", exception);

            throw new CantStartAgentException(exception, null, "You should inspect the cause.");
        }
    }

    /**
     * (non-javadoc)
     * @see FermatAgent#pause()
     */
    public synchronized void pause() throws CantStopAgentException {

        LOG.info("Pausing propagate nodes catalog agent.");
        try {

            if (isPaused())
                return;

            if(!this.isStarted())
                throw new CantStopAgentException("The agent is not running and it cannot be paused. Current status: "+this.getStatus());

            for (ScheduledFuture future: scheduledFutures) {
                future.cancel(Boolean.TRUE);
                scheduledFutures.remove(future);
            }

            this.status = AgentStatus.PAUSED;

        } catch (CantStopAgentException exception) {

            LOG.error("Unhandled error trying to pause the agent.", exception);
            throw exception;
        } catch (Exception exception) {

            LOG.error("Unhandled error trying to pause the agent.", exception);

            throw new CantStopAgentException(exception, null, "You should inspect the cause.");
        }
    }

    /**
     * (non-javadoc)
     * @see FermatAgent#stop()
     */
    public synchronized void stop() throws CantStopAgentException {

        LOG.info("Stopping propagate nodes catalog agent.");
        try {

            if (isStopped())
                return;

            if(!this.isStarted() && !this.isPaused())
                throw new CantStartAgentException("The agent is not running, it cannot be stopped.");

            for (ScheduledFuture future: scheduledFutures) {
                future.cancel(Boolean.TRUE);
                scheduledFutures.remove(future);
            }

            scheduledThreadPool.shutdown();
            scheduledFutures    = null;
            scheduledThreadPool = null;
            this.status = AgentStatus.STOPPED;

        } catch (Exception exception) {

            LOG.error("Unhandled error trying to stop the agent.", exception);

            throw new CantStopAgentException(exception, null, "You should inspect the cause.");
        }
    }

    /**
     * Internal thread
     */
    private class PropagationTask implements Runnable {

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
    }
}

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.layer.all_definition.enums.AgentStatus;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogDao;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>PropagateNodesCatalogAgent</code>
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
     * Represents the networkNodePluginRoot
     */
    private NodesCatalogDao nodesCatalogDao;

    /**
     * Constructor
     */
    public PropagateNodesCatalogAgent(final NetworkNodePluginRoot networkNodePluginRoot,
                                      final DaoFactory            daoFactory           ){

        this.networkNodePluginRoot = networkNodePluginRoot;
        this.nodesCatalogDao       = daoFactory.getNodesCatalogDao();
    }

    /**
     * Propagation logic implementation
     */
    private void propagateNodesCatalog() {

        LOG.info("Executing node propagateNodesCatalog()");

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

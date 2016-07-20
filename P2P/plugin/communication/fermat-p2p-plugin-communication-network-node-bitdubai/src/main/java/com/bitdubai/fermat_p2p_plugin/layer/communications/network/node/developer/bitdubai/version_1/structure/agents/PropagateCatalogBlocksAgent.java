package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.agents;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.layer.all_definition.enums.AgentStatus;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.agents.PropagateCatalogBlocksAgent</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class PropagateCatalogBlocksAgent extends FermatAgent {

    /**
     * Represents the LOGGER entity
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(PropagateCatalogBlocksAgent.class));

    /**
     * Represents the propagation initial delay
     */
    private final int PROPAGATION_INITIAL_DELAY = 0;

    /**
     * Represents the propagation interval
     */
    private final int PROPAGATION_INTERVAL = 3;

    /**
     * Represent the scheduledThreadPool
     */
    private ScheduledExecutorService scheduledThreadPool;

    /**
     * Represent the scheduledFutures
     */
    private List<ScheduledFuture> scheduledFutures;

    /**
     * Represent the networkNodePluginRoot
     */
    private NetworkNodePluginRoot networkNodePluginRoot;

    /**
     * Constructor
     */
    public PropagateCatalogBlocksAgent(final NetworkNodePluginRoot networkNodePluginRoot,
                                       final DaoFactory            daoFactory           ){

        this.networkNodePluginRoot = networkNodePluginRoot;
    }

    /**
     * Propagation logic implementation
     */
    private void propagateBlocks() {

        LOG.info("Executing node propagateBlocks()");

    }

    /**
     * (non-javadoc)
     * @see FermatAgent#start()
     */
    @Override
    public synchronized void start() throws CantStartAgentException {

        LOG.info("Starting propagate catalog blocks agent.");
        try {

            if(this.isStarted())
                return;

            if(this.isPaused()) {
                this.resume();
                return;
            }

            this.scheduledThreadPool   = Executors.newScheduledThreadPool(2);
            this.scheduledFutures      = new ArrayList<>();

            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PropagationTask(), PROPAGATION_INITIAL_DELAY, PROPAGATION_INTERVAL, TimeUnit.MINUTES));
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

        LOG.info("Resuming propagate catalog blocks agent.");
        try {

            if(this.isStarted())
                return;

            if(this.isStopped())
                throw new CantStartAgentException("The agent is stopped, can't resume it, you should start it.");

            scheduledFutures.add(scheduledThreadPool.scheduleAtFixedRate(new PropagationTask(), PROPAGATION_INITIAL_DELAY, PROPAGATION_INTERVAL, TimeUnit.MINUTES));
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

        LOG.info("Pausing propagate catalog blocks agent.");
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

        LOG.info("Stopping propagate catalog blocks agent.");
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
                propagateBlocks();
            } catch (Exception e) {
                LOG.error("Unhandled error during propagation.", e);
            }
        }
    }
}

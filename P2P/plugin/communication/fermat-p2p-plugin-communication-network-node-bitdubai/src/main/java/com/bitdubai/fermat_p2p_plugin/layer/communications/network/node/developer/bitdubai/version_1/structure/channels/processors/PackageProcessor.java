package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.apache.commons.lang.NotImplementedException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor</code>
 * is the base class for all message processor class <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public abstract class PackageProcessor {

    /**
     * Represent the packageType
     */
    private PackageType packageType;

    /**
     * Represent the gson instance
     */
    private Gson gson;

    /**
     * Represent the jsonParser instance
     */
    private JsonParser jsonParser;

    /**
     * Represent the networkNodePluginRoot
     */
    private NetworkNodePluginRoot networkNodePluginRoot;

    /**
     * Constructor with parameter
     * @param packageType
     */
    public PackageProcessor(PackageType packageType) {
        this.packageType = packageType;
        this.gson        = GsonProvider.getGson();
        this.jsonParser  = GsonProvider.getJsonParser();
        this.networkNodePluginRoot = (NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT);
    }

    /**
     * Gets the value of packageType and returns
     *
     * @return packageType
     */
    public PackageType getPackageType() {
        return packageType;
    }

    /**
     * Gets the value of gson and returns
     *
     * @return gson
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Gets the value of jsonParser and returns
     *
     * @return jsonParser
     */
    public JsonParser getJsonParser() {
        return jsonParser;
    }

    /**
     * Get the NetworkNodePluginRoot
     * @return NetworkNodePluginRoot
     */
    public NetworkNodePluginRoot getNetworkNodePluginRoot() {
        return networkNodePluginRoot;
    }

    /**
     * Save the method call history
     *
     * @param parameters
     * @param profileIdentityPublicKey
     */
    protected void methodCallsHistory(String parameters, String profileIdentityPublicKey) throws CantInsertRecordDataBaseException {
/*
        MethodCallsHistory methodCallsHistory = new MethodCallsHistory();
        methodCallsHistory.setMethodName(getPackageType().toString());
        methodCallsHistory.setParameters(parameters);
        methodCallsHistory.setProfileIdentityPublicKey(profileIdentityPublicKey);

        getDaoFactory().getMethodCallsHistoryDao().create(methodCallsHistory);*/
    }

    /**
     * Method that call to process the message
     *
     * @param session that send the package
     * @param packageReceived to process
     */
    public synchronized void processingPackage(final Session session, final Package packageReceived, final FermatWebSocketChannelEndpoint channel){
        throw new NotImplementedException();
    }

    /**
     *
     * @param messageToSend
     * @return boolean
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public final boolean sendMessage(Future<Void> messageToSend) throws ExecutionException, InterruptedException {
        return messageToSend.get() == null;
    }

}

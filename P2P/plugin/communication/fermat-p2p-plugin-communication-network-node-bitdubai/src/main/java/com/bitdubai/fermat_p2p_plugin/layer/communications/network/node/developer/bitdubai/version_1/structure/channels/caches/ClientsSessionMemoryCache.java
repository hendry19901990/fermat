package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches.ClientsSessionMemoryCache</code>
 * is responsible the manage the cache of the client session connected with the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.servers.FermatWebSocketClientChannelServerEndpoint</code><p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 27/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientsSessionMemoryCache {

    /**
     * Represent the singleton instance
     */
    private static ClientsSessionMemoryCache instance;

    /**
     * Holds all client sessions
     */
    private final Map<String , Session> clientSessionsById;

    /**
     * Constructor
     */
    private ClientsSessionMemoryCache() {
        clientSessionsById = new ConcurrentHashMap<>();
    }

    /**
     * Return the singleton instance
     *
     * @return ClientsSessionMemoryCache
     */
    public static ClientsSessionMemoryCache getInstance(){

        /*
         * If no exist create a new one
         */
        if (instance == null){
            instance = new ClientsSessionMemoryCache();
        }

        return instance;
    }

    /**
     * Get the session client
     *
     * @param clientPublicKey the client identity
     * @return the session of the client
     */
    public Session get(String clientPublicKey){

        /*
         * Return the session of this client
         */
        return getInstance().clientSessionsById.get(clientPublicKey);
    }

    /**
     * Add a new session to the memory cache
     *
     * @param clientpk the client public key
     * @param session the client session
     */
    public void add(final String clientpk,final Session session){

        /*
         * Add to the cache
         */
        getInstance().clientSessionsById.put(clientpk, session);
    }

    /**
     * Remove the session client
     *
     * @param clientPublicKey the session of the connection
     * @return the id of the session
     */
    public Session remove(String clientPublicKey){

        /*
         * remove the session of this client
         */

        return getInstance().clientSessionsById.remove(clientPublicKey);
    }

    /**
     * Verify is exist a session for a session id
     *
     * @param clientPublicKey the session id
     * @return (TRUE or FALSE)
     */
    public boolean exist(String clientPublicKey){

        return getInstance().clientSessionsById.containsKey(clientPublicKey);
    }
}

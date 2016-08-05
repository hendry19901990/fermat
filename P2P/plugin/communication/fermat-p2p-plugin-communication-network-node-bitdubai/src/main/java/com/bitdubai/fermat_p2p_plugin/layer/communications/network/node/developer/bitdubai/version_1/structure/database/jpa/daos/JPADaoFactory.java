/*
 * @#JPADaoFactory.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;


/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory</code>
 * represent the Data Access Object factory
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public final class JPADaoFactory {

    /**
     * Represent the instance
     */
    private static final JPADaoFactory instance = new JPADaoFactory();

    /**
     * Represent the actorSessionDao
     */
    private final ActorSessionDao actorSessionDao;

    /**
     * Represent the clientSessionDao
     */
    private final ClientSessionDao clientSessionDao;

    /**
     * Represent the clientDao
     */
    private final ClientDao clientDao;

    /**
     * Represent the methodCallsHistoryDao
     */
    private final MethodCallsHistoryDao methodCallsHistoryDao;

    /**
     * Represent the networkServiceSessionDao
     */
    private final NetworkServiceSessionDao networkServiceSessionDao;

    /**
     * Represent the networkServiceDao
     */
    private final NetworkServiceDao networkServiceDao;

    /**
     * Represent the nodeCatalogDao
     */
    private final NodeCatalogDao nodeCatalogDao;

    /**
     * Represent the profileRegistrationHistoryDao
     */
    private final ProfileRegistrationHistoryDao profileRegistrationHistoryDao;

    /**
     * Represents the GeoLocationDao
     */
    private final GeoLocationDao geoLocationDao;

    /**
     * Constructor
     */
    private JPADaoFactory() {
        super();
        actorSessionDao = new ActorSessionDao();
        clientSessionDao = new ClientSessionDao();
        clientDao = new ClientDao();
        methodCallsHistoryDao = new MethodCallsHistoryDao();
        networkServiceSessionDao = new NetworkServiceSessionDao();
        networkServiceDao = new NetworkServiceDao();
        nodeCatalogDao = new NodeCatalogDao();
        profileRegistrationHistoryDao = new ProfileRegistrationHistoryDao();
        geoLocationDao = new GeoLocationDao();
    }

    /**
     * Get the value of actorCatalogDao
     *
     * @return actorCatalogDao
     */
    static public ActorCatalogDao getActorCatalogDao() {
        return new ActorCatalogDao();
    }

    /**
     * Get the value of actorSessionDao
     *
     * @return actorSessionDao
     */
    static public ActorSessionDao getActorSessionDao() {
        return instance.actorSessionDao;
    }

    /**
     * Get the value of clientSessionDao
     *
     * @return clientSessionDao
     */
    static public ClientSessionDao getClientSessionDao() {
        return instance.clientSessionDao;
    }

    /**
     * Get the value of clientDao
     *
     * @return clientDao
     */
    static public ClientDao getClientDao() {
        return instance.clientDao;
    }

    /**
     * Get the value of methodCallsHistoryDao
     *
     * @return methodCallsHistoryDao
     */
    static public MethodCallsHistoryDao getMethodCallsHistoryDao() {
        return instance.methodCallsHistoryDao;
    }

    /**
     * Get the value of networkServiceSessionDao
     *
     * @return networkServiceSessionDao
     */
    static public NetworkServiceSessionDao getNetworkServiceSessionDao() {
        return instance.networkServiceSessionDao;
    }

    /**
     * Get the value of networkServiceDao
     *
     * @return networkServiceDao
     */
    static public NetworkServiceDao getNetworkServiceDao() {
        return instance.networkServiceDao;
    }

    /**
     * Get the value of nodeCatalogDao
     *
     * @return nodeCatalogDao
     */
    static public NodeCatalogDao getNodeCatalogDao() {
        return instance.nodeCatalogDao;
    }

    /**
     * Get the value of profileRegistrationHistoryDao
     *
     * @return profileRegistrationHistoryDao
     */
    static public ProfileRegistrationHistoryDao getProfileRegistrationHistoryDao() {
        return instance.profileRegistrationHistoryDao;
    }

    static public GeoLocationDao getGeoLocationDao() {
        return instance.geoLocationDao;
    }

    static public JPADaoFactory getInstance(){
        return instance;
    }
}

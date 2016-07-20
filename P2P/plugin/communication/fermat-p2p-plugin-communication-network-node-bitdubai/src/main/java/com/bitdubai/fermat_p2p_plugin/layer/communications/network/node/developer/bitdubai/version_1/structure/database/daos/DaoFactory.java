package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory</code> is
 * a factory class to manage the construction of the dao's classes
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 30/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class DaoFactory {

    /**
     * Represent the actorsCatalogDao instance
     */
    private ActorsCatalogDao actorsCatalogDao;

    /**
     * Represent the registrationHistoryDao instance
     */
    private RegistrationHistoryDao registrationHistoryDao;

    /**
     * Represent the checkedInProfilesDao instance
     */
    private CheckedInProfilesDao checkedInProfilesDao;

    /**
     * Represent the methodCallsHistoryDao instance
     */
    private MethodCallsHistoryDao methodCallsHistoryDao;

    /**
     * Represent the nodesCatalogDao instance
     */
    private NodesCatalogDao nodesCatalogDao;

    /**
     * Represents the NodesCatalogPropagationInformationDao instance
     */
    private NodesCatalogPropagationInformationDao nodesCatalogPropagationInformationDao;

    /**
     * Constructor
     * @param database
     */
    public DaoFactory(Database database){

        this.actorsCatalogDao                      = new ActorsCatalogDao(database);
        this.registrationHistoryDao                = new RegistrationHistoryDao(database);
        this.checkedInProfilesDao                  = new CheckedInProfilesDao(database);
        this.methodCallsHistoryDao                 = new MethodCallsHistoryDao(database);
        this.nodesCatalogDao                       = new NodesCatalogDao(database);
        this.nodesCatalogPropagationInformationDao = new NodesCatalogPropagationInformationDao(database);
    }

    public ActorsCatalogDao getActorsCatalogDao() {
        return actorsCatalogDao;
    }

    public RegistrationHistoryDao getRegistrationHistoryDao() {
        return registrationHistoryDao;
    }

    public CheckedInProfilesDao getCheckedInProfilesDao() {
        return checkedInProfilesDao;
    }

    public NodesCatalogDao getNodesCatalogDao() {
        return nodesCatalogDao;
    }

    public NodesCatalogPropagationInformationDao getNodesCatalogPropagationInformationDao() {
        return nodesCatalogPropagationInformationDao;
    }

    public MethodCallsHistoryDao getMethodCallsHistoryDao() {
        return methodCallsHistoryDao;
    }
}

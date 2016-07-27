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
     * Represent the checkedInProfilesDao instance
     */
    private CheckedInProfilesDao checkedInProfilesDao;

    /**
     * Constructor
     * @param database
     */
    public DaoFactory(Database database){

        this.checkedInProfilesDao                   = new CheckedInProfilesDao(database);
    }

    public CheckedInProfilesDao getCheckedInProfilesDao() {
        return checkedInProfilesDao;
    }

}

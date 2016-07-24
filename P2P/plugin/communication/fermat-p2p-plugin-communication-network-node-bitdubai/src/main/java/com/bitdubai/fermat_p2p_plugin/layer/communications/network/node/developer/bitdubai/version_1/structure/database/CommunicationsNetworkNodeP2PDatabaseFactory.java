package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_ACTOR_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_ALIAS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_EXTRA_DATA_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_HOSTED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_LAST_CONNECTION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_LAST_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_LAST_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_LAST_UPDATE_TIME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_NODE_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_PHOTO_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_THUMBNAIL_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_VERSION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.METHOD_CALLS_HISTORY_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.METHOD_CALLS_HISTORY_CREATE_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.METHOD_CALLS_HISTORY_METHOD_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.METHOD_CALLS_HISTORY_PARAMETERS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.METHOD_CALLS_HISTORY_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.METHOD_CALLS_HISTORY_UUID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_IP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_VERSION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_DETAIL_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_DEVICE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_PROFILE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_RESULT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.PROFILES_REGISTRATION_HISTORY_TYPE_COLUMN_NAME;

/**
 *  The Class  <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseFactory</code>
 * is responsible for creating the tables in the database where it is to keep the information.
 * <p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class CommunicationsNetworkNodeP2PDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;

    public CommunicationsNetworkNodeP2PDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * Create the database
     *
     * @param ownerId      the owner id
     * @param databaseName the database name
     * @return Database
     * @throws CantCreateDatabaseException
     */
    public Database createDatabase(UUID ownerId, String databaseName) throws CantCreateDatabaseException {
        Database database;

        /**
         * I will create the database where I am going to store the information of this wallet.
         */
        try {
            database = this.pluginDatabaseSystem.createDatabase(ownerId, databaseName);
        } catch (CantCreateDatabaseException cantCreateDatabaseException) {
            /**
             * I can not handle this situation.
             */
            throw new CantCreateDatabaseException(cantCreateDatabaseException, "databaseName="+databaseName, "Exception not handled by the plugin, There is a problem and i cannot create the database.");
        }

        /**
         * Next, I will add the needed tables.
         */
        try {
            DatabaseTableFactory table;
            DatabaseFactory databaseFactory = database.getDatabaseFactory();

            /**
             * Create actor catalog table.
             */
            table = databaseFactory.newTableFactory(ownerId, ACTOR_CATALOG_TABLE_NAME);

            table.addColumn(ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME       , DatabaseDataType.STRING      ,  255, Boolean.TRUE );
            table.addColumn(ACTOR_CATALOG_NAME_COLUMN_NAME                      , DatabaseDataType.STRING      ,  100, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_ALIAS_COLUMN_NAME                     , DatabaseDataType.STRING      ,  100, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_ACTOR_TYPE_COLUMN_NAME                , DatabaseDataType.STRING      ,  255, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_PHOTO_COLUMN_NAME                     , DatabaseDataType.STRING      , 2500, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_THUMBNAIL_COLUMN_NAME                 , DatabaseDataType.STRING      , 2500, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_LAST_LATITUDE_COLUMN_NAME             , DatabaseDataType.REAL        ,   50, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_LAST_LONGITUDE_COLUMN_NAME            , DatabaseDataType.REAL        ,   50, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_EXTRA_DATA_COLUMN_NAME                , DatabaseDataType.STRING      ,  255, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_HOSTED_TIMESTAMP_COLUMN_NAME          , DatabaseDataType.LONG_INTEGER,  100, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_LAST_UPDATE_TIME_COLUMN_NAME          , DatabaseDataType.LONG_INTEGER,  100, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_LAST_CONNECTION_COLUMN_NAME           , DatabaseDataType.LONG_INTEGER,  100, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_NODE_IDENTITY_PUBLIC_KEY_COLUMN_NAME  , DatabaseDataType.STRING      ,  255, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING      ,  255, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_VERSION_COLUMN_NAME                   , DatabaseDataType.LONG_INTEGER,  100, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME      , DatabaseDataType.INTEGER     ,   10, Boolean.FALSE);
            table.addColumn(ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME  , DatabaseDataType.INTEGER     ,   10, Boolean.FALSE);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }

            /**
             * Create checked in profile table.
             */

            table = databaseFactory.newTableFactory(ownerId, CHECKED_IN_PROFILES_TABLE_NAME);

            table.addColumn(CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME , DatabaseDataType.STRING      , 255, Boolean.FALSE);
            table.addColumn(CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME   , DatabaseDataType.STRING      , 255, Boolean.FALSE);
            table.addColumn(CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME         , DatabaseDataType.STRING      ,  50, Boolean.FALSE);
            table.addColumn(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME        , DatabaseDataType.STRING      ,  10, Boolean.FALSE);
            table.addColumn(CHECKED_IN_PROFILES_LATITUDE_COLUMN_NAME            , DatabaseDataType.STRING      ,  50, Boolean.FALSE);
            table.addColumn(CHECKED_IN_PROFILES_LONGITUDE_COLUMN_NAME           , DatabaseDataType.STRING      ,  10, Boolean.FALSE);
            table.addColumn(CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 100, Boolean.FALSE);

            List<String> indexColumnList = new ArrayList<>();
            indexColumnList.add(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME);
            indexColumnList.add(CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME);
            table.addIndex(indexColumnList);

            table.addIndex(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME);
            table.addIndex(CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME);
            table.addIndex(CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }

            /**
             * Create Profiles Registration History table.
             */
            table = databaseFactory.newTableFactory(ownerId, PROFILES_REGISTRATION_HISTORY_TABLE_NAME);

            table.addColumn(PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME                 , DatabaseDataType.STRING      , 100, Boolean.TRUE );
            table.addColumn(PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING      , 255, Boolean.FALSE);
            table.addColumn(PROFILES_REGISTRATION_HISTORY_DEVICE_TYPE_COLUMN_NAME        , DatabaseDataType.STRING      ,  50, Boolean.FALSE);
            table.addColumn(PROFILES_REGISTRATION_HISTORY_PROFILE_TYPE_COLUMN_NAME       , DatabaseDataType.STRING      ,  10, Boolean.FALSE);
            table.addColumn(PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME  , DatabaseDataType.LONG_INTEGER, 100, Boolean.FALSE);
            table.addColumn(PROFILES_REGISTRATION_HISTORY_TYPE_COLUMN_NAME               , DatabaseDataType.STRING      ,  10, Boolean.FALSE);
            table.addColumn(PROFILES_REGISTRATION_HISTORY_RESULT_COLUMN_NAME             , DatabaseDataType.STRING      ,  10, Boolean.FALSE);
            table.addColumn(PROFILES_REGISTRATION_HISTORY_DETAIL_COLUMN_NAME             , DatabaseDataType.STRING      , 100, Boolean.FALSE);

            table.addIndex (PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }

            /**
             * Create method calls history table.
             */
            table = databaseFactory.newTableFactory(ownerId, METHOD_CALLS_HISTORY_TABLE_NAME);

            table.addColumn(METHOD_CALLS_HISTORY_UUID_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.TRUE);
            table.addColumn(METHOD_CALLS_HISTORY_METHOD_NAME_COLUMN_NAME, DatabaseDataType.STRING, 255, Boolean.FALSE);
            table.addColumn(METHOD_CALLS_HISTORY_PARAMETERS_COLUMN_NAME, DatabaseDataType.STRING, 1000, Boolean.FALSE);
            table.addColumn(METHOD_CALLS_HISTORY_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 255, Boolean.FALSE);
            table.addColumn(METHOD_CALLS_HISTORY_CREATE_TIMESTAMP_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 100, Boolean.FALSE);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }

            /**
             * Create nodes catalog table.
             */
            table = databaseFactory.newTableFactory(ownerId, NODES_CATALOG_TABLE_NAME);

            table.addColumn(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME      , DatabaseDataType.STRING      , 255, Boolean.TRUE );
            table.addColumn(NODES_CATALOG_NAME_COLUMN_NAME                     , DatabaseDataType.STRING      , 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_IP_COLUMN_NAME                       , DatabaseDataType.STRING      ,  32, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME             , DatabaseDataType.INTEGER     , 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME            , DatabaseDataType.REAL        ,  50, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME           , DatabaseDataType.REAL        ,  50, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME, DatabaseDataType.INTEGER     , 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME          , DatabaseDataType.INTEGER     , 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME     , DatabaseDataType.LONG_INTEGER, 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_VERSION_COLUMN_NAME                  , DatabaseDataType.LONG_INTEGER, 100, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME     , DatabaseDataType.INTEGER     ,  10, Boolean.FALSE);
            table.addColumn(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME , DatabaseDataType.INTEGER     ,  10, Boolean.FALSE);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException cantCreateTableException) {
                throw new CantCreateDatabaseException(cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table.");
            }

        } catch (InvalidOwnerIdException invalidOwnerId) {
            /**
             * This shouldn't happen here because I was the one who gave the owner id to the database file system,
             * but anyway, if this happens, I can not continue.
             */
            throw new CantCreateDatabaseException(invalidOwnerId, "", "There is a problem with the ownerId of the database.");
        }
        return database;
    }

}

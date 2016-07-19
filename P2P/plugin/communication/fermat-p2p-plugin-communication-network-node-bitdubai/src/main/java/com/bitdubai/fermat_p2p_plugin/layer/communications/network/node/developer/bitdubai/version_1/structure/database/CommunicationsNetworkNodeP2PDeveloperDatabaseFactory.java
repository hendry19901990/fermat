package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInitializeCommunicationsNetworkNodeP2PDatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_ACTOR_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_ALIAS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_GENERATION_TIME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_HASH_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_NODE_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_PENDING_PROPAGATIONS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_SIGNATURE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_STATUS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_TYPE_COLUMN_NAME;
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
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_PHOTO_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_THUMBNAIL_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_ACTOR_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_ALIAS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_EXTRA_DATA_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_GENERATION_TIME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_HOSTED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_LAST_CONNECTION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_LAST_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_LAST_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_NODE_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_PHOTO_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_THUMBNAIL_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRANSACTION_TRANSACTION_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.DATA_BASE_NAME;
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
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_DEFAULT_PORT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_HASH_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_REGISTERED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TRANSACTION_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_DEFAULT_PORT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_IP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_LAST_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_LAST_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_REGISTERED_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_TRANSACTION_TYPE_COLUMN_NAME;
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
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDeveloperDatabaseFactoryTemp</code> have
 * contains the methods that the Developer Database Tools uses to show the information.
 * <p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */

public final class CommunicationsNetworkNodeP2PDeveloperDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID                 pluginId            ;

    private Database database;

    /**
     * Constructor
     *
     * @param pluginDatabaseSystem database system plug-in reference
     * @param pluginId             plug-in id reference
     */
    public CommunicationsNetworkNodeP2PDeveloperDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem,
                                                                final UUID pluginId) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId             = pluginId            ;
    }

    /**
     * This method open or creates the database i'll be working with
     *
     * @throws CantInitializeCommunicationsNetworkNodeP2PDatabaseException
     */
    public void initializeDatabase() throws CantInitializeCommunicationsNetworkNodeP2PDatabaseException {
        try {

             /*
              * Open new database connection
              */
            database = this.pluginDatabaseSystem.openDatabase(pluginId, DATA_BASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
            throw new CantInitializeCommunicationsNetworkNodeP2PDatabaseException(cantOpenDatabaseException.getMessage());

        } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            CommunicationsNetworkNodeP2PDatabaseFactory communicationsNetworkNodeP2PDatabaseFactory = new CommunicationsNetworkNodeP2PDatabaseFactory(pluginDatabaseSystem);

            try {
                  /*
                   * We create the new database
                   */
                database = communicationsNetworkNodeP2PDatabaseFactory.createDatabase(pluginId, DATA_BASE_NAME);
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeCommunicationsNetworkNodeP2PDatabaseException(cantCreateDatabaseException.getMessage());
            }
        }
    }


    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        /**
         * I only have one database on my plugin. I will return its name.
         */
        List<DeveloperDatabase> databases = new ArrayList<>();
        databases.add(developerObjectFactory.getNewDeveloperDatabase("Communications Network Node", DATA_BASE_NAME));
        return databases;
    }


    public List<DeveloperDatabaseTable> getDatabaseTableList(final DeveloperObjectFactory developerObjectFactory,
                                                             final DeveloperDatabase      developerDatabase     ) {

        List<DeveloperDatabaseTable> tables = new ArrayList<>();

        /**
         * Table actor catalog columns.
         */
        List<String> actorcatalogColumns = new ArrayList<>();

        actorcatalogColumns.add(ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_NAME_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_ALIAS_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_ACTOR_TYPE_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_PHOTO_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_THUMBNAIL_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_LAST_LATITUDE_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_LAST_LONGITUDE_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_EXTRA_DATA_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_HOSTED_TIMESTAMP_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_LAST_UPDATE_TIME_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_LAST_CONNECTION_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_NODE_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        actorcatalogColumns.add(ACTOR_CATALOG_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        /**
         * Table actor catalog addition.
         */
        DeveloperDatabaseTable actorcatalogTable = developerObjectFactory.getNewDeveloperDatabaseTable(ACTOR_CATALOG_TABLE_NAME, actorcatalogColumns);
        tables.add(actorcatalogTable);

        /**
         * Table actor catalog transaction columns.
         */
        List<String> actorcatalogtransactionColumns = new ArrayList<>();

        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_NAME_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_ALIAS_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_ACTOR_TYPE_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_PHOTO_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_THUMBNAIL_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_LAST_LATITUDE_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_LAST_LONGITUDE_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_EXTRA_DATA_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_HOSTED_TIMESTAMP_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_LAST_CONNECTION_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_NODE_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_TRANSACTION_TYPE_COLUMN_NAME);
        actorcatalogtransactionColumns.add(ACTOR_CATALOG_TRANSACTION_GENERATION_TIME_COLUMN_NAME);
        /**
         * Table actor catalog transaction addition.
         */
        DeveloperDatabaseTable actorcatalogtransactionTable = developerObjectFactory.getNewDeveloperDatabaseTable(ACTOR_CATALOG_TRANSACTION_TABLE_NAME, actorcatalogtransactionColumns);
        tables.add(actorcatalogtransactionTable);

        /**
         * Table Actor Catalog Blocks columns.
         */
        List<String> actorCatalogBlocksColumns = new ArrayList<>();

        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_HASH_ID_COLUMN_NAME);
        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_NODE_PUBLIC_KEY_COLUMN_NAME);
        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_SIGNATURE_COLUMN_NAME);
        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_GENERATION_TIME_COLUMN_NAME);
        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_STATUS_COLUMN_NAME);
        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_TYPE_COLUMN_NAME);
        actorCatalogBlocksColumns.add(CATALOG_BLOCKS_PENDING_PROPAGATIONS_COLUMN_NAME);
        /**
         * Table Actor Catalog Blocks addition.
         */
        DeveloperDatabaseTable actorCatalogBlocksTable = developerObjectFactory.getNewDeveloperDatabaseTable(CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_TABLE_NAME, actorCatalogBlocksColumns);
        tables.add(actorCatalogBlocksTable);

        /**
         * Table checked in profile columns.
         */
        List<String> checkedInProfilesColumns = new ArrayList<>();

        checkedInProfilesColumns.add(CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        checkedInProfilesColumns.add(CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME);
        checkedInProfilesColumns.add(CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME);
        checkedInProfilesColumns.add(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME);
        checkedInProfilesColumns.add(CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME);

        /**
         * Table checked in profile addition.
         */
        DeveloperDatabaseTable checkedInProfilesTable = developerObjectFactory.getNewDeveloperDatabaseTable(CHECKED_IN_PROFILES_TABLE_NAME, checkedInProfilesColumns);
        tables.add(checkedInProfilesTable);

        /**
         * Table Profiles Registration History columns.
         */
        List<String> profilesRegistrationHistoryColumns = new ArrayList<>();

        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_DEVICE_TYPE_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_PROFILE_TYPE_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_TYPE_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_RESULT_COLUMN_NAME);
        profilesRegistrationHistoryColumns.add(PROFILES_REGISTRATION_HISTORY_DETAIL_COLUMN_NAME);

        /**
         * Table Profiles Registration History addition.
         */
        DeveloperDatabaseTable profilesRegistrationHistoryTable = developerObjectFactory.getNewDeveloperDatabaseTable(PROFILES_REGISTRATION_HISTORY_TABLE_NAME, profilesRegistrationHistoryColumns);
        tables.add(profilesRegistrationHistoryTable);

        /**
         * Table method calls history columns.
         */
        List<String> methodcallshistoryColumns = new ArrayList<>();

        methodcallshistoryColumns.add(METHOD_CALLS_HISTORY_UUID_COLUMN_NAME);
        methodcallshistoryColumns.add(METHOD_CALLS_HISTORY_METHOD_NAME_COLUMN_NAME);
        methodcallshistoryColumns.add(METHOD_CALLS_HISTORY_PARAMETERS_COLUMN_NAME);
        methodcallshistoryColumns.add(METHOD_CALLS_HISTORY_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        methodcallshistoryColumns.add(METHOD_CALLS_HISTORY_CREATE_TIMESTAMP_COLUMN_NAME);
        /**
         * Table method calls history addition.
         */
        DeveloperDatabaseTable methodcallshistoryTable = developerObjectFactory.getNewDeveloperDatabaseTable(METHOD_CALLS_HISTORY_TABLE_NAME, methodcallshistoryColumns);
        tables.add(methodcallshistoryTable);

        /**
         * Table nodes catalog columns.
         */
        List<String> nodescatalogColumns = new ArrayList<>();

        nodescatalogColumns.add(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_NAME_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_IP_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME);
        nodescatalogColumns.add(NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME);
        /**
         * Table nodes catalog addition.
         */
        DeveloperDatabaseTable nodescatalogTable = developerObjectFactory.getNewDeveloperDatabaseTable(NODES_CATALOG_TABLE_NAME, nodescatalogColumns);
        tables.add(nodescatalogTable);

        /**
         * Table nodes catalog transaction columns.
         */
        List<String> nodescatalogtransactionColumns = new ArrayList<>();

        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_NAME_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_IP_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_DEFAULT_PORT_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_LAST_LATITUDE_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_LAST_LONGITUDE_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_REGISTERED_TIMESTAMP_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME);
        nodescatalogtransactionColumns.add(NODES_CATALOG_TRANSACTION_TRANSACTION_TYPE_COLUMN_NAME);
        /**
         * Table nodes catalog transaction addition.
         */
        DeveloperDatabaseTable nodescatalogtransactionTable = developerObjectFactory.getNewDeveloperDatabaseTable(NODES_CATALOG_TRANSACTION_TABLE_NAME, nodescatalogtransactionColumns);
        tables.add(nodescatalogtransactionTable);

        /**
         * Table nodes catalog transactions pending for propagation columns.
         */
        List<String> nodescatalogtransactionspendingforpropagationColumns = new ArrayList<>();

        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_HASH_ID_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_NAME_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_IP_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_DEFAULT_PORT_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LATITUDE_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_LONGITUDE_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_REGISTERED_TIMESTAMP_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME);
        nodescatalogtransactionspendingforpropagationColumns.add(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TRANSACTION_TYPE_COLUMN_NAME);
        /**
         * Table nodes catalog transactions pending for propagation addition.
         */
        DeveloperDatabaseTable nodescatalogtransactionspendingforpropagationTable = developerObjectFactory.getNewDeveloperDatabaseTable(NODES_CATALOG_TRANSACTIONS_PENDING_FOR_PROPAGATION_TABLE_NAME, nodescatalogtransactionspendingforpropagationColumns);
        tables.add(nodescatalogtransactionspendingforpropagationTable);

        return tables;
    }

    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(final DeveloperObjectFactory developerObjectFactory,
                                                                      final DeveloperDatabase      developerDatabase     ,
                                                                      final DeveloperDatabaseTable developerDatabaseTable) {

        try {

            this.initializeDatabase();

            List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<>();

            final DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());

            selectedTable.loadToMemory();

            final List<DatabaseTableRecord> records = selectedTable.getRecords();

            List<String> developerRow;

            for (final DatabaseTableRecord row: records){

                developerRow = new ArrayList<>();

                for (final DatabaseRecord field : row.getValues())
                    developerRow.add(field.getValue());

                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }

            return returnedRecords;

        } catch (final CantLoadTableToMemoryException                 |
                       CantInitializeCommunicationsNetworkNodeP2PDatabaseException e) {

            System.err.println(e);

            return new ArrayList<>();

        } catch (final Exception e){

            return new ArrayList<>();
        }
    }

}
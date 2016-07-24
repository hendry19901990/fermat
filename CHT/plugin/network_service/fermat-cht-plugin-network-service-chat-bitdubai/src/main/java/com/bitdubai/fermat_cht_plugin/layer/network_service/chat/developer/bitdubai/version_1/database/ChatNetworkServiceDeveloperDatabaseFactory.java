package com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_cht_plugin.layer.network_service.chat.developer.bitdubai.version_1.exceptions.CantInitializeChatNetworkServiceDatabaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.factories.NetworkServiceDatabaseFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.DATABASE_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_CONTENT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_CONTENT_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_DELIVERY_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_RECEIVER_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_SENDER_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_SHIPPING_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_STATUS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.INCOMING_MESSAGES_TABLE_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_CONTENT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_CONTENT_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_DELIVERY_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_FAIL_COUNT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_IS_BETWEEN_ACTORS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_RECEIVER_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_SENDER_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_SHIPPING_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_STATUS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.constants.NetworkServiceDatabaseConstants.OUTGOING_MESSAGES_TABLE_NAME;

/**
 * Created by natalia on 18/12/15.
 */
public class ChatNetworkServiceDeveloperDatabaseFactory {//implements DealsWithPluginDatabaseSystem, DealsWithPluginIdentity {

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginIdentity Interface member variables.
     */
    UUID pluginId;


    Database database;

    private ErrorManager errorManager;
    /**
     * Constructor
     *
     * @param pluginDatabaseSystem
     * @param pluginId
     */
    public ChatNetworkServiceDeveloperDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId, ErrorManager errorManager) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
        this.errorManager = errorManager;
    }
    private void reportUnexpectedError(final Exception e) {
        errorManager.reportUnexpectedPluginException(Plugins.CHAT_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
    }
    /**
     * This method open or creates the database i'll be working with
     *
     * @throws CantInitializeChatNetworkServiceDatabaseException
     */
    public void initializeDatabase(String tableId) throws CantInitializeChatNetworkServiceDatabaseException {


        switch (tableId) {

            case ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME:
                try {

             /*
              * Open new database connection
              */
                    database = this.pluginDatabaseSystem.openDatabase(pluginId, ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME);

                } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
                    CantInitializeChatNetworkServiceDatabaseException cantInitializeChatNetworkServiceDatabaseException = new CantInitializeChatNetworkServiceDatabaseException(cantOpenDatabaseException.getMessage());
                    reportUnexpectedError(cantInitializeChatNetworkServiceDatabaseException);
                    throw cantInitializeChatNetworkServiceDatabaseException;

                } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
                    ChatNetworkServiceDatabaseFactory chatNetworkServiceDatabaseFactory = new ChatNetworkServiceDatabaseFactory(pluginDatabaseSystem,errorManager);

                    try {
                  /*
                   * We create the new database
                   */
                        database = chatNetworkServiceDatabaseFactory.createDatabase(pluginId, ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME);
                    } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                        CantInitializeChatNetworkServiceDatabaseException cantInitializeChatNetworkServiceDatabaseException = new CantInitializeChatNetworkServiceDatabaseException(cantCreateDatabaseException.getMessage());
                        reportUnexpectedError(cantInitializeChatNetworkServiceDatabaseException);
                        throw cantInitializeChatNetworkServiceDatabaseException;
                    }
                }
                break;

            case DATABASE_NAME:
                try {

                    this.database = this.pluginDatabaseSystem.openDatabase(pluginId, DATABASE_NAME);

                } catch (CantOpenDatabaseException e) {

                    throw new CantInitializeChatNetworkServiceDatabaseException(e, "tableId: " + tableId, "Error trying to open the database.");

                } catch (DatabaseNotFoundException e) {

                    NetworkServiceDatabaseFactory communicationLayerNetworkServiceDatabaseFactory = new NetworkServiceDatabaseFactory(pluginDatabaseSystem);

                    try {

                        this.database = communicationLayerNetworkServiceDatabaseFactory.createDatabase(pluginId, DATABASE_NAME);

                    } catch (CantCreateDatabaseException z) {

                        throw new CantInitializeChatNetworkServiceDatabaseException(z, "tableId: " + tableId, "Error trying to create the database.");
                    }
                }
        }
    }

    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        /**
         * I only have one database on my plugin. I will return its name.
         */
        List<DeveloperDatabase> databases = new ArrayList<>();
        databases.add(developerObjectFactory.getNewDeveloperDatabase(
                "Chat Network Service",
                ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME
        ));

        databases.add(developerObjectFactory.getNewDeveloperDatabase(
                "Network Service Template",
                DATABASE_NAME
        ));

        return databases;
    }

    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        List<DeveloperDatabaseTable> tables = new ArrayList<>();

        switch (developerDatabase.getId()) {

            case ChatNetworkServiceDataBaseConstants.DATA_BASE_NAME:

                /**
                 * incomingChatColumns Chat columns.
                 */
                List<String> incomingChatColumns = new ArrayList<>();

                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_ID_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_RESPONSE_TO_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_IDCHAT_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_IDOBJECTO_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_LOCALACTORTYPE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_LOCALACTORPUBKEY_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_REMOTEACTORTYPE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_REMOTEACTORPUBKEY_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_CHATNAME_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_CHATSTATUS_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_MESSAGE_STATUS_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_DATE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_IDMENSAJE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_MESSAGE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_DISTRIBUTIONSTATUS_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_PROCCES_STATUS_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_PROTOCOL_STATE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_SENTDATE_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_READ_MARK_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_SENT_COUNT_COLUMN_NAME);
                incomingChatColumns.add(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_XML_MSG_REPRESENTATION);
                /**
                 * incomingChatColumns Outgoing Chat addition.
                 */
                DeveloperDatabaseTable incomingChatTable = developerObjectFactory.getNewDeveloperDatabaseTable(ChatNetworkServiceDataBaseConstants.CHAT_METADATA_TRANSACTION_RECORD_TABLE, incomingChatColumns);
                tables.add(incomingChatTable);
                break;

            case DATABASE_NAME:

                /**
                 * Table incoming messages columns.
                 */
                List<String> incomingMessagesColumns = new ArrayList<>();

                incomingMessagesColumns.add(INCOMING_MESSAGES_ID_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_SENDER_PUBLIC_KEY_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_RECEIVER_PUBLIC_KEY_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_SHIPPING_TIMESTAMP_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_DELIVERY_TIMESTAMP_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_CONTENT_TYPE_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_STATUS_COLUMN_NAME);
                incomingMessagesColumns.add(INCOMING_MESSAGES_CONTENT_COLUMN_NAME);

                /**
                 * Table incoming messages addition.
                 */
                DeveloperDatabaseTable incomingMessagesTable = developerObjectFactory.getNewDeveloperDatabaseTable(INCOMING_MESSAGES_TABLE_NAME, incomingMessagesColumns);
                tables.add(incomingMessagesTable);

                /**
                 * Table outgoing messages columns.
                 */
                List<String> outgoingMessagesColumns = new ArrayList<>();

                outgoingMessagesColumns.add(OUTGOING_MESSAGES_ID_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_SENDER_PUBLIC_KEY_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_RECEIVER_PUBLIC_KEY_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_SHIPPING_TIMESTAMP_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_DELIVERY_TIMESTAMP_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_CONTENT_TYPE_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_STATUS_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_IS_BETWEEN_ACTORS_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_FAIL_COUNT_COLUMN_NAME);
                outgoingMessagesColumns.add(OUTGOING_MESSAGES_CONTENT_COLUMN_NAME);

                /**
                 * Table outgoing messages addition.
                 */
                DeveloperDatabaseTable outgoingMessagesTable = developerObjectFactory.getNewDeveloperDatabaseTable(OUTGOING_MESSAGES_TABLE_NAME, outgoingMessagesColumns);
                tables.add(outgoingMessagesTable);
                break;
        }

        return tables;
    }

    public synchronized final List<DeveloperDatabaseTableRecord> getDatabaseTableContent(final DeveloperObjectFactory developerObjectFactory,
                                                                                         final DeveloperDatabase      developerDatabase     ,
                                                                                         final DeveloperDatabaseTable developerDatabaseTable) {

        try {

            initializeDatabase(developerDatabase.getId());

            final List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<>();

            final DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());

            try {

                selectedTable.loadToMemory();
            } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

                return returnedRecords;
            }

            final List<DatabaseTableRecord> records = selectedTable.getRecords();

            List<String> developerRow;

            for (DatabaseTableRecord row : records) {

                developerRow = new ArrayList<>();

                for (DatabaseRecord field : row.getValues())
                    developerRow.add(field.getValue());

                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }
            return returnedRecords;

        } catch (Exception e) {

            System.err.println(e.toString());
            return new ArrayList<>();
        }
    }

}
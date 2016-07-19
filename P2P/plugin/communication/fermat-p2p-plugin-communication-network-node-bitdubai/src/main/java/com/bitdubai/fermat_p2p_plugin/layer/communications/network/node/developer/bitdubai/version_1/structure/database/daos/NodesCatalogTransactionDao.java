package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalogTransaction;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_BLOCK_HASH_ID;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_DEFAULT_PORT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRANSACTION_GENERATION_TIME_COLUMN_NAME;
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

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogTransactionDao</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 19/07/2016.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogTransactionDao extends AbstractBaseDao<NodesCatalogTransaction> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public NodesCatalogTransactionDao(Database dataBase) {
        super(dataBase, NODES_CATALOG_TRANSACTION_TABLE_NAME, NODES_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME);
    }

    /**
     * Method that list the requested quantity of nodes catalog transactions currently off block.
     *
     * @return All entities.
     *
     * @throws CantReadRecordDataBaseException if something goes wrong.
     */
    public final List<NodesCatalogTransaction> getTransactionsOffBlock(final Integer quantity) throws CantReadRecordDataBaseException {

        if (quantity == null)
            throw new java.security.InvalidParameterException("Quantity must not be null");

        try {

            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addFilterOrder(NODES_CATALOG_TRANSACTION_GENERATION_TIME_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);

            table.addStringFilter(NODES_CATALOG_TRANSACTION_BLOCK_HASH_ID, null, DatabaseFilterType.IS_NULL);

            table.setFilterTop(quantity.toString());

            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            final List<NodesCatalogTransaction> list = new ArrayList<>();

            // Convert into entity objects and add to the list.
            for (DatabaseTableRecord record : records)
                list.add(getEntityFromDatabaseTableRecord(record));

            return list;

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + getTableName(), "The data no exist");
        } catch (final InvalidParameterException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + getTableName(), "Invalid parameter found, maybe the enum is wrong.");
        }
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected NodesCatalogTransaction getEntityFromDatabaseTableRecord(DatabaseTableRecord record) throws InvalidParameterException {

        NodesCatalogTransaction entity = new NodesCatalogTransaction();

        entity.setHashId(record.getStringValue(NODES_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME));
        entity.setLastConnectionTimestamp(getTimestampFromLongValue(record.getLongValue(NODES_CATALOG_TRANSACTION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME)));
        entity.setDefaultPort(record.getIntegerValue(NODES_CATALOG_TRANSACTION_DEFAULT_PORT_COLUMN_NAME));
        entity.setIdentityPublicKey(record.getStringValue(NODES_CATALOG_TRANSACTION_IDENTITY_PUBLIC_KEY_COLUMN_NAME));
        entity.setIp(record.getStringValue(NODES_CATALOG_TRANSACTION_IP_COLUMN_NAME));
        entity.setName(record.getStringValue(NODES_CATALOG_TRANSACTION_NAME_COLUMN_NAME));
        entity.setLastLocation(record.getDoubleValue(NODES_CATALOG_TRANSACTION_LAST_LATITUDE_COLUMN_NAME), record.getDoubleValue(NODES_CATALOG_TRANSACTION_LAST_LONGITUDE_COLUMN_NAME));
        entity.setRegisteredTimestamp(getTimestampFromLongValue(record.getLongValue(NODES_CATALOG_TRANSACTION_REGISTERED_TIMESTAMP_COLUMN_NAME)));
        entity.setTransactionType(record.getStringValue(NODES_CATALOG_TRANSACTION_TRANSACTION_TYPE_COLUMN_NAME));


        return entity;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(NodesCatalogTransaction entity) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setStringValue(NODES_CATALOG_TRANSACTION_HASH_ID_COLUMN_NAME, entity.getHashId());
        databaseTableRecord.setLongValue(NODES_CATALOG_TRANSACTION_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, getLongValueFromTimestamp(entity.getLastConnectionTimestamp()));
        databaseTableRecord.setIntegerValue(NODES_CATALOG_TRANSACTION_DEFAULT_PORT_COLUMN_NAME, entity.getDefaultPort());
        databaseTableRecord.setStringValue(NODES_CATALOG_TRANSACTION_IDENTITY_PUBLIC_KEY_COLUMN_NAME, entity.getIdentityPublicKey());
        databaseTableRecord.setStringValue(NODES_CATALOG_TRANSACTION_IP_COLUMN_NAME, entity.getIp());
        databaseTableRecord.setStringValue(NODES_CATALOG_TRANSACTION_NAME_COLUMN_NAME, entity.getName());

        databaseTableRecord.setDoubleValue(NODES_CATALOG_TRANSACTION_LAST_LATITUDE_COLUMN_NAME, entity.getLastLocation().getLatitude());
        databaseTableRecord.setDoubleValue(NODES_CATALOG_TRANSACTION_LAST_LONGITUDE_COLUMN_NAME, entity.getLastLocation().getLongitude());


        databaseTableRecord.setLongValue(NODES_CATALOG_TRANSACTION_REGISTERED_TIMESTAMP_COLUMN_NAME, getLongValueFromTimestamp(entity.getRegisteredTimestamp()));
        databaseTableRecord.setStringValue(NODES_CATALOG_TRANSACTION_TRANSACTION_TYPE_COLUMN_NAME, entity.getTransactionType());

        return databaseTableRecord;
    }
}

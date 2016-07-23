package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;

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

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogDao</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 20/07/2016.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogDao extends AbstractBaseDao<NodesCatalog> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public NodesCatalogDao(final Database dataBase) {

        super(
                dataBase,
                NODES_CATALOG_TABLE_NAME,
                NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME
        );
    }

    /**
     * Method that create a new entity in the data base.
     *
     * @param entity to create.
     *
     * @throws CantInsertRecordDataBaseException if something goes wrong.
     */
    public final void create(final NodesCatalog entity             ,
                             final Integer      version            ,
                             final Integer      pendingPropagations) throws CantInsertRecordDataBaseException {

        if (entity == null)
            throw new IllegalArgumentException("The entity is required, can not be null");

        try {

            DatabaseTableRecord entityRecord = getDatabaseTableRecordForNewNodeCatalogRecord(entity, version, pendingPropagations);

            getDatabaseTable().insertRecord(entityRecord);

        } catch (final CantInsertRecordException cantInsertRecordException) {

            throw new CantInsertRecordDataBaseException(
                    cantInsertRecordException,
                    "Table Name: " + this.getTableName(),
                    "The Template Database triggered an unexpected problem that wasn't able to solve by itself."
            );
        }
    }

    /**
     * Method that update an entity in the data base.
     * If version is null i will update the record with the previous version plus one.
     *
     * @param entity to update.
     *
     * @throws CantUpdateRecordDataBaseException  if something goes wrong.
     * @throws RecordNotFoundException            if we can't find the record in db.
     */
    public final void update(final NodesCatalog entity             ,
                                   Integer      version            ,
                             final Integer      pendingPropagations) throws CantUpdateRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        if (entity == null)
            throw new IllegalArgumentException("The entity is required, can not be null.");

        try {

            final DatabaseTable table = this.getDatabaseTable();
            table.addStringFilter(this.getIdTableName(), entity.getId(), DatabaseFilterType.EQUAL);
            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            if (!records.isEmpty()) {
                if (version == null)
                    version = records.get(0).getIntegerValue(NODES_CATALOG_VERSION_COLUMN_NAME) + 1;

                table.updateRecord(getDatabaseTableRecordForNewNodeCatalogRecord(entity, version, pendingPropagations));
            } else
                throw new RecordNotFoundException("id: " + entity.getId(), "Cannot find an entity with that id.");

        } catch (final CantUpdateRecordException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The record do not exist");
        } catch (final CantLoadTableToMemoryException e) {

            throw new CantUpdateRecordDataBaseException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");

        }
    }

    /**
     * Method that update a nodes catalog record in the database increasing its offline counter.
     *
     * @param publicKey       of the node profile to update.
     * @param offlineCounter  to set.
     *
     * @throws CantUpdateRecordDataBaseException  if something goes wrong.
     * @throws RecordNotFoundException            if we can't find the record in db.
     */
    public final void setOfflineCounter(final String  publicKey     ,
                                        final Integer offlineCounter) throws CantUpdateRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        if (publicKey == null)
            throw new IllegalArgumentException("The publicKey is required, can not be null.");

        if (offlineCounter == null)
            throw new IllegalArgumentException("The offlineCounter is required, can not be null.");

        try {

            final DatabaseTable table = this.getDatabaseTable();
            table.addStringFilter(this.getIdTableName(), publicKey, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                record.setIntegerValue(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME, offlineCounter);
                table.updateRecord(record);
            } else
                throw new RecordNotFoundException("publicKey: " + publicKey, "Cannot find an node catalog with this public key.");

        } catch (final CantUpdateRecordException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The record do not exist");
        } catch (final CantLoadTableToMemoryException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    public final void increaseLateNotificationCounter(final String publicKey) throws CantUpdateRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        if (publicKey == null)
            throw new IllegalArgumentException("The publicKey is required, can not be null.");

        try {

            final DatabaseTable table = this.getDatabaseTable();
            table.addStringFilter(this.getIdTableName(), publicKey, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                record.setIntegerValue(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME, record.getIntegerValue(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME)+1);
                table.updateRecord(record);
            } else
                throw new RecordNotFoundException("publicKey: " + publicKey, "Cannot find an node catalog with this public key.");

        } catch (final CantUpdateRecordException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The record do not exist");
        } catch (final CantLoadTableToMemoryException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    /**
     * Method that list paginated nodes with less late_notification_counter and offline_counter values.
     * Filter the list with the @identityPublicKey of the node who is searching in its own catalog.
     *
     * @param identityPublicKey own node public key.
     * @param max               quantity of records to bring.
     * @param offset            pointer where the database must bring the records.
     *
     * @return All entities.
     *
     * @throws CantReadRecordDataBaseException if something goes wrong.
     */
    public final List<NodesCatalog> listNodesToPropagateWith(final String  identityPublicKey,
                                                             final Integer max              ,
                                                             final Integer offset           ) throws CantReadRecordDataBaseException {

        try {

            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addFilterOrder(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);
            table.addFilterOrder(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME          , DatabaseFilterOrder.ASCENDING);

            table.addStringFilter(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME, identityPublicKey, DatabaseFilterType.NOT_EQUALS);

            table.setFilterTop(max.toString());
            table.setFilterOffSet(offset.toString());

            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            final List<NodesCatalog> list = new ArrayList<>();

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
     * Method that get the count of all entities on the table.
     *
     * @return count of All entities.
     *
     * @throws CantReadRecordDataBaseException if something goes wrong.
     */
    public final long getCountOfNodesToPropagateWith(final String identityPublicKey) throws CantReadRecordDataBaseException {

        try {
            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addStringFilter(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME, identityPublicKey, DatabaseFilterType.NOT_EQUALS);

            return table.getCount();

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The data no exist");
        }
    }

    private DatabaseTableRecord getDatabaseTableRecordForNewNodeCatalogRecord(final NodesCatalog entity             ,
                                                                              final Integer      version            ,
                                                                              final Integer      pendingPropagations) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setLongValue   (NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, getLongValueFromTimestamp(entity.getLastConnectionTimestamp()));
        databaseTableRecord.setIntegerValue(NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME             , entity.getDefaultPort());
        databaseTableRecord.setStringValue (NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME      , entity.getIdentityPublicKey());
        databaseTableRecord.setStringValue (NODES_CATALOG_IP_COLUMN_NAME                       , entity.getIp());
        databaseTableRecord.setStringValue (NODES_CATALOG_NAME_COLUMN_NAME                     , entity.getName());
        databaseTableRecord.setDoubleValue (NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME            , entity.getLastLocation().getLatitude());
        databaseTableRecord.setDoubleValue (NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME           , entity.getLastLocation().getLongitude());
        databaseTableRecord.setLongValue   (NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME     , getLongValueFromTimestamp(entity.getRegisteredTimestamp()));
        databaseTableRecord.setIntegerValue(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME, entity.getLateNotificationsCounter());
        databaseTableRecord.setIntegerValue(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME          , entity.getOfflineCounter());
        databaseTableRecord.setIntegerValue(NODES_CATALOG_VERSION_COLUMN_NAME                  , version);
        databaseTableRecord.setIntegerValue(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME     , pendingPropagations);
        databaseTableRecord.setIntegerValue(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME , 0);

        return databaseTableRecord;
    }


    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected NodesCatalog getEntityFromDatabaseTableRecord(DatabaseTableRecord record) throws InvalidParameterException {

        NodesCatalog entity = new NodesCatalog();

        entity.setLastConnectionTimestamp(getTimestampFromLongValue(record.getLongValue(NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME)));
        entity.setDefaultPort(record.getIntegerValue(NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME));
        entity.setIdentityPublicKey(record.getStringValue(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME));
        entity.setIp(record.getStringValue(NODES_CATALOG_IP_COLUMN_NAME));
        entity.setName(record.getStringValue(NODES_CATALOG_NAME_COLUMN_NAME));
        entity.setLastLocation(record.getDoubleValue(NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME), record.getDoubleValue(NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME));
        entity.setRegisteredTimestamp(getTimestampFromLongValue(record.getLongValue(NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME)));
        entity.setLateNotificationsCounter(record.getIntegerValue(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME));
        entity.setOfflineCounter(record.getIntegerValue(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME));
        entity.setVersion(record.getIntegerValue(NODES_CATALOG_VERSION_COLUMN_NAME));
        
        return entity;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(NodesCatalog entity) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setLongValue(NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, getLongValueFromTimestamp(entity.getLastConnectionTimestamp()));
        databaseTableRecord.setIntegerValue(NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME, entity.getDefaultPort());
        databaseTableRecord.setStringValue(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME, entity.getIdentityPublicKey());
        databaseTableRecord.setStringValue(NODES_CATALOG_IP_COLUMN_NAME, entity.getIp());
        databaseTableRecord.setStringValue(NODES_CATALOG_NAME_COLUMN_NAME, entity.getName());
        databaseTableRecord.setDoubleValue(NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME, entity.getLastLocation().getLatitude());
        databaseTableRecord.setDoubleValue(NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME, entity.getLastLocation().getLongitude());
        databaseTableRecord.setLongValue(NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME, getLongValueFromTimestamp(entity.getRegisteredTimestamp()));
        databaseTableRecord.setIntegerValue(NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME, entity.getLateNotificationsCounter());
        databaseTableRecord.setIntegerValue(NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME, entity.getOfflineCounter());
        databaseTableRecord.setIntegerValue(NODES_CATALOG_VERSION_COLUMN_NAME, entity.getVersion());

        return databaseTableRecord;
    }

}

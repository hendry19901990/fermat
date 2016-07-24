package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_VERSION_COLUMN_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.NodesCatalogPropagationInformationDao</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogPropagationInformationDao extends AbstractBaseDao<PropagationInformation> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public NodesCatalogPropagationInformationDao(final Database dataBase) {

        super(
                dataBase,
                NODES_CATALOG_TABLE_NAME,
                NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME
        );
    }

    public final void increaseTriedToPropagateTimes(final PropagationInformation propagationInformation) throws CantUpdateRecordDataBaseException, RecordNotFoundException {

        if (propagationInformation == null)
            throw new IllegalArgumentException("The propagationInformation is required, can not be null.");

        try {

            final DatabaseTable table = this.getDatabaseTable();
            table.addStringFilter(this.getIdTableName(), propagationInformation.getId(), DatabaseFilterType.EQUAL);
            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                propagationInformation.increaseTriedToPropagateTimes();
                record.setIntegerValue(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, propagationInformation.getTriedToPropagateTimes());
                table.updateRecord(record);
            } else
                throw new RecordNotFoundException("publicKey: " + propagationInformation.getId(), "Cannot find an node catalog with this public key.");

        } catch (final CantUpdateRecordException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The record do not exist");
        } catch (final CantLoadTableToMemoryException e) {

            throw new CantUpdateRecordDataBaseException(e, "Table Name: " + this.getTableName(), "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    /**
     * Method that list paginated nodes with more pending propagations and less tried to propagate times.
     *
     * @param maxTriedToPropagateTimes  indicates the max tried to propagate times of the nodes catalog records to propagate.
     *                                  if null it will not apply the filter
     *
     * @return a list of propagation information instances.
     *
     * @throws CantReadRecordDataBaseException if something goes wrong.
     */
    public final List<PropagationInformation> listItemsToShare(final Long    maxTriedToPropagateTimes) throws CantReadRecordDataBaseException {

        try {

            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addFilterOrder(NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);

            table.addStringFilter(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME    , String.valueOf(0)                       , DatabaseFilterType.GREATER_THAN);

            if (maxTriedToPropagateTimes != null)
                table.addStringFilter(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, String.valueOf(maxTriedToPropagateTimes), DatabaseFilterType.LESS_THAN   );

            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            final List<PropagationInformation> list = new ArrayList<>();

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
    public final long getCountOfItemsToShare(Long maxTriedToPropagateTimes) throws CantReadRecordDataBaseException {

        try {
            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addStringFilter(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME, String.valueOf(0), DatabaseFilterType.GREATER_THAN);

            if (maxTriedToPropagateTimes != null)
                table.addStringFilter(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, String.valueOf(maxTriedToPropagateTimes), DatabaseFilterType.LESS_THAN   );

            return table.getCount();

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The data no exist");
        }
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected PropagationInformation getEntityFromDatabaseTableRecord(final DatabaseTableRecord record) throws InvalidParameterException {

        String  identityPublicKey     = record.getStringValue (NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME     );
        Integer version               = record.getIntegerValue(NODES_CATALOG_VERSION_COLUMN_NAME                 );
        Integer pendingPropagations   = record.getIntegerValue(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME    );
        Integer triedToPropagateTimes = record.getIntegerValue(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME);

        return new PropagationInformation(
                identityPublicKey    ,
                version              ,
                pendingPropagations  ,
                triedToPropagateTimes
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(final PropagationInformation entity) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setStringValue (NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME     , entity.getId()                   );
        databaseTableRecord.setIntegerValue(NODES_CATALOG_VERSION_COLUMN_NAME                 , entity.getVersion()              );
        databaseTableRecord.setIntegerValue(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME    , entity.getPendingPropagations()  );
        databaseTableRecord.setIntegerValue(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, entity.getTriedToPropagateTimes());

        return databaseTableRecord;
    }
}

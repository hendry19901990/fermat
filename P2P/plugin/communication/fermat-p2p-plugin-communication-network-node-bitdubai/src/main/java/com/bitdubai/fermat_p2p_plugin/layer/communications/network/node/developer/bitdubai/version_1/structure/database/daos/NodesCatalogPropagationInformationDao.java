package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.PropagationInformation;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME;
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

    /**
     * Method that list paginated nodes with more pending propagations and less tried to propagate times.
     *
     * @param max      quantity of records to bring.
     * @param offset   pointer where the database must bring the records.
     *
     * @return a list of propagation information instances.
     *
     * @throws CantReadRecordDataBaseException if something goes wrong.
     */
    public final List<PropagationInformation> listItemsToShare(final Integer max   ,
                                                               final Integer offset) throws CantReadRecordDataBaseException {

        try {

            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addFilterOrder(NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME    , DatabaseFilterOrder.DESCENDING);
            table.addFilterOrder(NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);

            table.setFilterTop(max.toString());
            table.setFilterOffSet(offset.toString());

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

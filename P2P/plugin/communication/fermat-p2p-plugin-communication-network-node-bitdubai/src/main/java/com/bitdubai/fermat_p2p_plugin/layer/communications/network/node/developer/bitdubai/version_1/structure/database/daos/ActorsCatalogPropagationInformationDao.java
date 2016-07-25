package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_LAST_UPDATE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_VERSION_COLUMN_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.ActorsCatalogPropagationInformationDao</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorsCatalogPropagationInformationDao extends AbstractBaseDao<ActorPropagationInformation> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public ActorsCatalogPropagationInformationDao(final Database dataBase) {

        super(
                dataBase,
                ACTOR_CATALOG_TABLE_NAME,
                ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME
        );
    }

    public final void increaseTriedToPropagateTimes(final ActorPropagationInformation actorPropagationInformation) throws CantUpdateRecordDataBaseException, RecordNotFoundException {

        if (actorPropagationInformation == null)
            throw new IllegalArgumentException("The actorPropagationInformation is required, can not be null.");

        try {

            final DatabaseTable table = this.getDatabaseTable();
            table.addStringFilter(this.getIdTableName(), actorPropagationInformation.getId(), DatabaseFilterType.EQUAL);
            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                actorPropagationInformation.increaseTriedToPropagateTimes();
                record.setIntegerValue(ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, actorPropagationInformation.getTriedToPropagateTimes());
                table.updateRecord(record);
            } else
                throw new RecordNotFoundException("publicKey: " + actorPropagationInformation.getId(), "Cannot find an node catalog with this public key.");

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
    /*public final List<ActorPropagationInformation> listItemsToShare(final Long    maxTriedToPropagateTimes) throws CantReadRecordDataBaseException {

        try {

            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            table.addFilterOrder(ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME, DatabaseFilterOrder.ASCENDING);

            table.addStringFilter(ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME, String.valueOf(0), DatabaseFilterType.GREATER_THAN);

            if (maxTriedToPropagateTimes != null)
                table.addStringFilter(ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, String.valueOf(maxTriedToPropagateTimes), DatabaseFilterType.LESS_THAN);

            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            final List<ActorPropagationInformation> list = new ArrayList<>();

            // Convert into entity objects and add to the list.
            for (DatabaseTableRecord record : records)
                list.add(getEntityFromDatabaseTableRecord(record));

            return list;

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + getTableName(), "The data no exist");
        } catch (final InvalidParameterException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + getTableName(), "Invalid parameter found, maybe the enum is wrong.");
        }
    }*/

    public final List<ActorPropagationInformation> listItemsToShare(final Long maxTriedToPropagateTimes) throws CantReadRecordDataBaseException {

        try {

            // load the data base to memory
            DatabaseTable table = getDatabaseTable();

            String customQuery = "SELECT "+ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME+", "+
                    ACTOR_CATALOG_VERSION_COLUMN_NAME+", "+
                    ACTOR_CATALOG_LAST_UPDATE_TYPE_COLUMN_NAME+", "+
                    ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME+", "+
                    ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME+
                    " FROM "+ACTOR_CATALOG_TABLE_NAME+
                    " WHERE "+ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME+" > 0"+
                    (maxTriedToPropagateTimes != null ? " AND "+ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME+" < "+maxTriedToPropagateTimes : " ")+
                    "ORDER BY "+ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME;

            final List<DatabaseTableRecord> records = table.customQuery(customQuery, true);

            final List<ActorPropagationInformation> list = new ArrayList<>();

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

            table.addStringFilter(ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME, String.valueOf(0), DatabaseFilterType.GREATER_THAN);

            if (maxTriedToPropagateTimes != null)
                table.addStringFilter(ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME, String.valueOf(maxTriedToPropagateTimes), DatabaseFilterType.LESS_THAN   );

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
    protected ActorPropagationInformation getEntityFromDatabaseTableRecord(final DatabaseTableRecord record) throws InvalidParameterException {

        String                  identityPublicKey     = record.getStringValue (ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME     );
        Integer                 version               = record.getIntegerValue(ACTOR_CATALOG_VERSION_COLUMN_NAME);
        ActorCatalogUpdateTypes lastUpdateType        = ActorCatalogUpdateTypes.getByCode(record.getStringValue(ACTOR_CATALOG_LAST_UPDATE_TYPE_COLUMN_NAME));
        Integer                 pendingPropagations   = record.getIntegerValue(ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME    );
        Integer                 triedToPropagateTimes = record.getIntegerValue(ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME);

        return new ActorPropagationInformation(
                identityPublicKey    ,
                version              ,
                lastUpdateType       ,
                pendingPropagations  ,
                triedToPropagateTimes
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(final ActorPropagationInformation entity) {

        throw new IllegalAccessError("Could not use this method, you should not set any parameters in this DAO.");
    }
}

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_TABLE_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.CheckedInProfilesDao</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class CheckedInProfilesDao extends AbstractBaseDao<CheckedInProfile> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public CheckedInProfilesDao(Database dataBase) {
        super(
                dataBase,
                CHECKED_IN_PROFILES_TABLE_NAME                     ,
                CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME
        );
    }

    public final List<CheckedInProfile> findAll(final ProfileTypes type,
                                                final Map<String, Object> filters) throws CantReadRecordDataBaseException {

        if (filters == null || filters.isEmpty())
            throw new IllegalArgumentException("The filters are required, can not be null or empty.");

        try {

            // Prepare the filters
            final DatabaseTable table = getDatabaseTable();

            final List<DatabaseTableFilter> internalTableFilters = new ArrayList<>();

            for (String key : filters.keySet()) {

                DatabaseTableFilter newFilter = table.getEmptyTableFilter();
                newFilter.setType(DatabaseFilterType.EQUAL);
                newFilter.setColumn(key);
                newFilter.setValue((String) filters.get(key));

                internalTableFilters.add(newFilter);
            }


            // load the data base to memory with filters
            table.setFilterGroup(internalTableFilters, null, DatabaseFilterOperator.OR);

            List<DatabaseTableFilter> tableFilters = new ArrayList<>();

            if (type != null)
                tableFilters.add(table.getNewFilter(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME, DatabaseFilterType.EQUAL, type.getCode()));

            // load the data base to memory with filters
            List<DatabaseTableFilterGroup> internalFilterGroups = new ArrayList<>();

            if (!internalTableFilters.isEmpty())
                internalFilterGroups.add(table.getNewFilterGroup(internalTableFilters, null, DatabaseFilterOperator.OR));

            table.setFilterGroup(tableFilters, internalFilterGroups, DatabaseFilterOperator.AND);

            table.loadToMemory();

            final List<DatabaseTableRecord> records = table.getRecords();

            final List<CheckedInProfile> list = new ArrayList<>();

            // Convert into entity objects and add to the list.
            for (DatabaseTableRecord record : records)
                list.add(getEntityFromDatabaseTableRecord(record));

            return list;

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + this.getTableName(), "The data no exist");
        } catch (final InvalidParameterException e) {

            throw new CantReadRecordDataBaseException(e, "Table Name: " + this.getTableName(), "Invalid parameter found, maybe the enum is wrong.");
        }
    }


    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected CheckedInProfile getEntityFromDatabaseTableRecord(DatabaseTableRecord record) throws InvalidParameterException {

        String identityPublicKey = record.getStringValue(CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME);

        String clientPublicKey = record.getStringValue(CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME);

        String deviceType = record.getStringValue(CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME);

        ProfileTypes profileType = ProfileTypes.getByCode(record.getStringValue(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME));

        Timestamp checkedInTimestamp = getTimestampFromLongValue(record.getLongValue(CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME));

        return new CheckedInProfile(
                identityPublicKey,
                clientPublicKey,
                deviceType,
                profileType,
                null, // TODO ADD LOCATION,
                checkedInTimestamp
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(CheckedInProfile entity) {

        /*
         * Create the record to the entity
         */
        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setStringValue(CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME , entity.getIdentityPublicKey()           );
        databaseTableRecord.setStringValue(CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME   , entity.getClientPublicKey());
        databaseTableRecord.setStringValue(CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME, entity.getInformation());
        databaseTableRecord.setFermatEnum (CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME        , entity.getProfileType());
        databaseTableRecord.setLongValue  (CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME, getLongValueFromTimestamp(entity.getCheckedInTimestamp()));

        return databaseTableRecord;
    }
}


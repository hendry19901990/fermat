package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;

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
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.RegistrationHistoryDao</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class RegistrationHistoryDao extends AbstractBaseDao<ProfileRegistrationHistory> {

    /**
     * Constructor with parameter
     *
     * @param dataBase
     */
    public RegistrationHistoryDao(Database dataBase) {
        super(
                dataBase,
                PROFILES_REGISTRATION_HISTORY_TABLE_NAME,
                PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected ProfileRegistrationHistory getEntityFromDatabaseTableRecord(final DatabaseTableRecord record) throws InvalidParameterException {

        return new ProfileRegistrationHistory(
                record.getUUIDValue(PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME),
                record.getStringValue(PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                record.getStringValue(PROFILES_REGISTRATION_HISTORY_DEVICE_TYPE_COLUMN_NAME),
                ProfileTypes.getByCode(record.getStringValue(PROFILES_REGISTRATION_HISTORY_PROFILE_TYPE_COLUMN_NAME)),
                getTimestampFromLongValue(record.getLongValue(PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME)),
                RegistrationType.getByCode(record.getStringValue(PROFILES_REGISTRATION_HISTORY_TYPE_COLUMN_NAME)),
                RegistrationResult.getByCode(record.getStringValue(PROFILES_REGISTRATION_HISTORY_RESULT_COLUMN_NAME)),
                record.getStringValue(PROFILES_REGISTRATION_HISTORY_DETAIL_COLUMN_NAME)
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(final ProfileRegistrationHistory entity) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setStringValue(PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME, entity.getId());
        databaseTableRecord.setStringValue(PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME, entity.getIdentityPublicKey());
        databaseTableRecord.setStringValue(PROFILES_REGISTRATION_HISTORY_DEVICE_TYPE_COLUMN_NAME        , entity.getDeviceType()                );
        databaseTableRecord.setFermatEnum(PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME, entity.getProfileType());
        databaseTableRecord.setLongValue  (PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME  , getLongValueFromTimestamp(entity.getCheckedTimestamp()));
        databaseTableRecord.setFermatEnum (PROFILES_REGISTRATION_HISTORY_TYPE_COLUMN_NAME               , entity.getType()                      );
        databaseTableRecord.setFermatEnum (PROFILES_REGISTRATION_HISTORY_RESULT_COLUMN_NAME             , entity.getResult()                    );
        databaseTableRecord.setStringValue(PROFILES_REGISTRATION_HISTORY_DETAIL_COLUMN_NAME             , entity.getDetail()                    );

        return databaseTableRecord;

    }
}


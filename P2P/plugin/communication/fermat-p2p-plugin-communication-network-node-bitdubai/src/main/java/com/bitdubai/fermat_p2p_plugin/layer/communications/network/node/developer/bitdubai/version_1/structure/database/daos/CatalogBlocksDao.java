package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CatalogBlock;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.BlockStatus;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.BlockTypes;

import java.sql.Timestamp;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_GENERATION_TIME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_HASH_ID_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_NODE_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_PENDING_PROPAGATIONS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_SIGNATURE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_STATUS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_TABLE_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CATALOG_BLOCKS_TYPE_COLUMN_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.CatalogBlocksDao</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class CatalogBlocksDao extends AbstractBaseDao<CatalogBlock> {

    public CatalogBlocksDao(final Database dataBase) {
        super(
                dataBase,
                CATALOG_BLOCKS_TABLE_NAME,
                CATALOG_BLOCKS_HASH_ID_COLUMN_NAME
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getEntityFromDatabaseTableRecord(DatabaseTableRecord)
     */
    @Override
    protected CatalogBlock getEntityFromDatabaseTableRecord(final DatabaseTableRecord record) throws InvalidParameterException {

        String hashId        = record.getStringValue(CATALOG_BLOCKS_HASH_ID_COLUMN_NAME);
        String nodePublicKey = record.getStringValue(CATALOG_BLOCKS_NODE_PUBLIC_KEY_COLUMN_NAME);
        String signature     = record.getStringValue(CATALOG_BLOCKS_SIGNATURE_COLUMN_NAME);

        Timestamp generationTime = getTimestampFromLongValue(record.getLongValue(CATALOG_BLOCKS_GENERATION_TIME_COLUMN_NAME));

        BlockStatus status = BlockStatus.getByCode(record.getStringValue(CATALOG_BLOCKS_STATUS_COLUMN_NAME));
        BlockTypes  type   = BlockTypes .getByCode(record.getStringValue(CATALOG_BLOCKS_TYPE_COLUMN_NAME));

        Integer pendingPropagations = record.getIntegerValue(CATALOG_BLOCKS_PENDING_PROPAGATIONS_COLUMN_NAME);

        return new CatalogBlock(
                hashId             ,
                nodePublicKey      ,
                signature          ,
                generationTime     ,
                status             ,
                type               ,
                pendingPropagations
        );
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseDao#getDatabaseTableRecordFromEntity
     */
    @Override
    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(CatalogBlock entity) {

        DatabaseTableRecord databaseTableRecord = getDatabaseTable().getEmptyRecord();

        databaseTableRecord.setStringValue (CATALOG_BLOCKS_HASH_ID_COLUMN_NAME             , entity.getId());
        databaseTableRecord.setStringValue (CATALOG_BLOCKS_NODE_PUBLIC_KEY_COLUMN_NAME     , entity.getNodePublicKey());
        databaseTableRecord.setStringValue (CATALOG_BLOCKS_SIGNATURE_COLUMN_NAME           , entity.getSignature());
        databaseTableRecord.setLongValue   (CATALOG_BLOCKS_GENERATION_TIME_COLUMN_NAME     , getLongValueFromTimestamp(entity.getGenerationTime()));
        databaseTableRecord.setFermatEnum  (CATALOG_BLOCKS_STATUS_COLUMN_NAME              , entity.getStatus());
        databaseTableRecord.setFermatEnum  (CATALOG_BLOCKS_TYPE_COLUMN_NAME                , entity.getType());
        databaseTableRecord.setIntegerValue(CATALOG_BLOCKS_PENDING_PROPAGATIONS_COLUMN_NAME, entity.getPendingPropagations());

        return databaseTableRecord;
    }

}

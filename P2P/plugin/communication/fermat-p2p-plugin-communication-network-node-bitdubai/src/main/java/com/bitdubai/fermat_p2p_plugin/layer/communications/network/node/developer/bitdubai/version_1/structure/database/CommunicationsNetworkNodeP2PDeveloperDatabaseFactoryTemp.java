package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database;

import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CommunicationsNetworkNodeP2PDeveloperDatabaseFactoryTemp {

    public CommunicationsNetworkNodeP2PDeveloperDatabaseFactoryTemp(final PluginDatabaseSystem pluginDatabaseSystem,
                                                                    final UUID                 pluginId            ) {

    }

    public List<String> getTableList() {

        return new ArrayList<>();
    }

    public List<DatabaseTableRecord> getTableContent(final String tableName) {

        return new ArrayList<>();
    }

    public final List<DatabaseTableRecord> getTableContent(final String tableName, final Integer offset, final Integer max ){

        return new ArrayList<>();
    }

    public final long count(final String tableName){

        return 0;
    }


}
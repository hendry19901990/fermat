package com.bitdubai.fermat_osa_addon.layer.android.device_conectivity.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractAddon;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.ConnectionType;
import com.bitdubai.fermat_api.layer.osa_android.ConnectivityManager;
import com.bitdubai.fermat_api.layer.osa_android.NetworkStateReceiver;

/**
 * This addon handles a layer of Device Connectivity representation.
 * Encapsulates all the necessary functions for recovering the network to which the device is connected.
 *
 * * * *
 */

public class DeviceConnectivityAddonRoot extends AbstractAddon implements ConnectivityManager {

    public DeviceConnectivityAddonRoot() {
        super(new AddonVersionReference(new Version()));
    }

    @Override
    public FermatManager getManager() {
        return this;
    }

    @Override
    public void registerListener(NetworkStateReceiver networkStateReceiver) {

    }

    @Override
    public void unregisterListener(NetworkStateReceiver networkStateReceiver) {

    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public boolean isConnected(ConnectionType connectionType) throws Exception {
        return true;
    }
}

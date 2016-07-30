package com.fermat_p2p_layer.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkChannel;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.P2PLayerManager;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractNetworkService;

/**
 * Created by Matias Furszyfer on 2016.07.06..
 */
public class P2PLayerPluginRoot extends AbstractPlugin implements P2PLayerManager {

    private NetworkChannel client;

    public P2PLayerPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    @Override
    public void start() throws CantStartPluginException {
        super.start();
    }

    @Override
    public void register(AbstractNetworkService abstractNetworkService) {

    }

    @Override
    public void register(NetworkChannel NetworkChannel) {
        if(client!=null) throw new IllegalArgumentException("Client already registered");
        client = NetworkChannel;
        client.connect();
    }

    @Override
    public void registerReconnect(NetworkChannel networkChannel) {

    }
}

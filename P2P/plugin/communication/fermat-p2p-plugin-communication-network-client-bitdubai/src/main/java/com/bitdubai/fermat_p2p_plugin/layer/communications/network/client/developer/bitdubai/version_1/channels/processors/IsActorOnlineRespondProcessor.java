/*
* @#IsActorOnlineRespondProcessor.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.*;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.endpoints.NetworkClientCommunicationChannel;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.IsActorOnlineRespondProcessor</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 18/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class IsActorOnlineRespondProcessor extends PackageProcessor {

    /**
     * Constructor with parameter
     *
     * @param channel
     */
    public IsActorOnlineRespondProcessor(NetworkClientCommunicationChannel channel) {
        super(channel, PackageType.IS_ACTOR_ONLINE);
    }

    @Override
    public void processingPackage(Session session, Package packageReceived) {

    }

}

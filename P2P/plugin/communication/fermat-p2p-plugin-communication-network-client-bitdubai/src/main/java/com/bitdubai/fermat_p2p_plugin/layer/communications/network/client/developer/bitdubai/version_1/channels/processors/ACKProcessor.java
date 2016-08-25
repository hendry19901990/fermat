/*
* @#MessageTransmitRespondProcessor.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors;

import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientNewMessageDeliveredEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ACKRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MessageTransmitRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.endpoints.NetworkClientCommunicationChannel;

import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.ACKProcessor</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 15/05/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ACKProcessor extends PackageProcessor{

    /**
     * Represent the LOG
     */
    private static final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ACKProcessor.class));

    /**
     * Constructor whit parameter
     *
     * @param networkClientCommunicationChannel register
     */
    public ACKProcessor(final NetworkClientCommunicationChannel networkClientCommunicationChannel) {
        super(
                networkClientCommunicationChannel,
                PackageType.ACK
        );
    }


    @Override
    public void processingPackage(Session session, Package packageReceived) {

        LOG.info("Processing new package received, packageType: " + packageReceived.getPackageType());
        ACKRespond messageTransmitRespond = ACKRespond.parseContent(packageReceived.getContent());

        LOG.info(messageTransmitRespond.toJson());

        if (messageTransmitRespond.getStatus() == MsgRespond.STATUS.SUCCESS) {
            LOG.info("ACKRespond - Raised a event = P2pEventType.NETWORK_CLIENT_ACK");
            getChannel().getConnection().incrementTotalOfMessagesSentsSuccessfully();
        } else {
            getChannel().getConnection().incrementTotalOfMessagesSentsFails();
        }
    }

}

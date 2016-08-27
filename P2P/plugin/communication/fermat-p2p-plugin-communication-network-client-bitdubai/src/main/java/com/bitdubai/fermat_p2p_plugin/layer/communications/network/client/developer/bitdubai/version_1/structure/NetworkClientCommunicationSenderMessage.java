/*
* @#NetworkClientCommunicationSenderMessage.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.NetworkClientCommunicationSenderMessage</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 04/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkClientCommunicationSenderMessage implements Runnable {

    /*
     * Represent the NetworkClientCommunicationConnection
     */
    private NetworkClientCommunicationConnection communicationConnection;

    /*
     * Represent the list ofNetworkServiceMessage
     */
    private List<NetworkServiceMessage> listNetworkServiceMessages;


    /*
     * Constructor
     */
    public NetworkClientCommunicationSenderMessage(NetworkClientCommunicationConnection communicationConnection){
        this.communicationConnection = communicationConnection;
        this.listNetworkServiceMessages =  new ArrayList<>();
    }


    @Override
    public void run() {


        try {
            for(NetworkServiceMessage NSmessage : listNetworkServiceMessages)
                communicationConnection.sendPackageMessage(NSmessage, NetworkServiceType.UNDEFINED, NSmessage.getReceiverPublicKey());
        } catch (CantSendMessageException e) {
            e.printStackTrace();
        }


    }

    public void addNetworkServiceMessages(NetworkServiceMessage message){
        listNetworkServiceMessages.add(message);
    }

}

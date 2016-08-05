/*
* @#NetworkClientCommunicationSenderMessage.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage;

import java.util.Timer;
import java.util.TimerTask;

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
     * Represent the NetworkServiceMessage
     */
    private NetworkServiceMessage message;

    /*
     * Represent the NetworkServiceType
     */
    private  NetworkServiceType networkServiceTypeIntermediate;

    /*
     * Represent the identityPublicKey
     */
    private String identityPublicKey;

    /*
     * Constructor
     */
    public NetworkClientCommunicationSenderMessage(NetworkClientCommunicationConnection communicationConnection, NetworkServiceMessage message, NetworkServiceType networkServiceTypeIntermediate, String identityPublicKey){
        this.communicationConnection = communicationConnection;
        this.message = message;
        this.networkServiceTypeIntermediate = networkServiceTypeIntermediate;
        this.identityPublicKey = identityPublicKey;
    }


    @Override
    public void run() {

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask(){

            @Override
            public void run() {

                try {
                    communicationConnection.sendPackageMessage(message, networkServiceTypeIntermediate, identityPublicKey);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        };

        timer.schedule(timerTask, 0 , 5000);


    }

}

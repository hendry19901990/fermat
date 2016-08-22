package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.checkin;

import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientProfileRegisteredEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ACKRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.endpoints.NetworkClientCommunicationChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.PackageProcessor;

import javax.websocket.Session;

/**
 * The Class <code>CheckInActorRespondProcessor</code>
 * process all packages received the type <code>PackageType.CHECK_IN_ACTOR_RESPONSE</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/04/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class CheckInActorRespondProcessor extends PackageProcessor {

    /**
     * Constructor whit parameter
     *
     * @param networkClientCommunicationChannel register
     */
    public CheckInActorRespondProcessor(final NetworkClientCommunicationChannel networkClientCommunicationChannel) {
        super(
                networkClientCommunicationChannel,
                PackageType.CHECK_IN_ACTOR_RESPONSE
        );
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        System.out.println("Processing new package received, packageType: " + packageReceived.getPackageType());
        ACKRespond ackRespond = ACKRespond.parseContent(packageReceived.getContent());

        if(ackRespond.getStatus() == ACKRespond.STATUS.SUCCESS){

            getChannel().getConnection().incrementTotalOfProfileSuccessChecked();

            try {

//                getChannel().getConnection().sendApacheJMeterMessageTEST(checkInProfileMsjRespond.getIdentityPublicKey());

                String actorPublicKey = getChannel().getConnection().getActorProfileFromUUID(ackRespond.getPackageId());

                if(actorPublicKey != null) {

                    ActorProfile actorProfileSender = getChannel().getConnection().getActorProfileSender(actorPublicKey);
                    String publicKeyNS = getChannel().getConnection().getPublicKeyNSFromActorPK(actorPublicKey);

                    if (actorProfileSender != null && publicKeyNS != null) {

                        getChannel().getConnection().onlineActorsDiscoveryQuery(
                                new DiscoveryQueryParameters(null, NetworkServiceType.UNDEFINED,
                                        actorProfileSender.getActorType(), null, null, null, null, null, Boolean.TRUE, null, 20, 0, Boolean.FALSE),
                                publicKeyNS,
                                actorProfileSender.getIdentityPublicKey());

                    }

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            getChannel().getConnection().incrementTotalOfProfileFailureToCheckin();
        }

        /*
         * Raise the event
         */
        System.out.println("CheckInActorRespondProcessor - Raised a event = P2pEventType.NETWORK_CLIENT_ACTOR_PROFILE_REGISTERED");

    }

}

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.checkin;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ClientCheckInRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.CommunicationChannels;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.endpoints.NetworkClientCommunicationChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.processors.PackageProcessor;


import javax.websocket.Session;

/**
 * The Class <code>CheckInClientRespondProcessor</code>
 * process all packages received the type <code>PackageType.CHECK_IN_CLIENT_RESPONSE</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 07/04/2016.
 *
 * @author lnacosta
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInClientRespondProcessor extends PackageProcessor {


    /**
     * Constructor whit parameter
     *
     * @param networkClientCommunicationChannel register
     */
    public CheckInClientRespondProcessor(final NetworkClientCommunicationChannel networkClientCommunicationChannel) {
        super(
                networkClientCommunicationChannel,
                PackageType.CHECK_IN_CLIENT_RESPOND
        );
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(final Session session        ,
                                  final Package packageReceived) {

        System.out.println("Processing new package received, packageType: " + packageReceived.getPackageType());
        ClientCheckInRespond checkInProfileMsjRespond = ClientCheckInRespond.parseContent(packageReceived.getContent());

        if (checkInProfileMsjRespond.getStatus() == MsgRespond.STATUS.SUCCESS) {

            this.getChannel().getConnection().setNodesListPosition();
            this.getChannel().setIsRegistered(Boolean.TRUE.booleanValue());

            if (this.getChannel().getConnection().isExternalNode()) {

                String uriToNode = this.getChannel().getConnection().getUri().getHost() + ":" + this.getChannel().getConnection().getUri().getPort();
                System.out.println("CheckInClientRespondProcessor - Raised a event = P2pEventType.NETWORK_CLIENT_CONNECTION_SUCCESS");
            } else {

                System.out.println("CheckInClientRespondProcessor - Raised a event = P2pEventType.NETWORK_CLIENT_REGISTERED");
                getChannel().getConnection().incrementTotalOfProfileSuccessChecked();

                try {
                    getChannel().getConnection().sendCheckinAllNetworkServices();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else {
            getChannel().getConnection().incrementTotalOfProfileFailureToCheckin();
        }
    }


}

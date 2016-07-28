package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInActorRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_ACTOR_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInActorRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInActorRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInActorRequestProcessor() {
        super(PackageType.CHECK_IN_ACTOR_REQUEST);
    }


    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorProfile actorProfile = null;

        try {

            CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Obtain the profile of the actor
             */
            actorProfile = (ActorProfile) messageContent.getProfileToRegister();

            LOG.info("Actor public key: "+actorProfile.getClientIdentityPublicKey());

            Boolean exist = JPADaoFactory.getActorCatalogDao().exist(actorProfile.getIdentityPublicKey());

            LOG.info("Actor exist = "+exist);

            if (exist){

                /*
                 * Load the client associate whit the actor
                 */
                Client client = JPADaoFactory.getClientDao().findById(actorProfile.getClientIdentityPublicKey());

                LOG.info("client = "+client);

                /*
                 * Checked In Profile into data base
                 */
                JPADaoFactory.getActorSessionDao().checkIn(session, actorProfile, client);

                /*
                 * If all ok, respond whit success message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), actorProfile.getIdentityPublicKey());
                channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_ACTOR_RESPONSE, destinationIdentityPublicKey);

                LOG.info("Registered new Actor = "+actorProfile.getName());

            }else {
                throw new RuntimeException("Can't check in actor, is no in the actors catalog");
            }

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), actorProfile.getIdentityPublicKey());
                channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_ACTOR_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }

    }
}

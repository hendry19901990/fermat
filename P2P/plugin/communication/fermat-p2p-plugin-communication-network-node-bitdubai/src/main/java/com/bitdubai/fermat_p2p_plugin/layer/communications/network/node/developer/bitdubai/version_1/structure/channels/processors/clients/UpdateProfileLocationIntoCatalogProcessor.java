package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.UpdateProfileGeolocationMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.UpdateProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors.ActorsCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.sql.Timestamp;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.UpdateProfileLocationIntoCatalogProcessor</code>
 * process all packages received the type <code>MessageType.UPDATE_PROFILE_GEOLOCATION_REQUEST</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 28/06/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class UpdateProfileLocationIntoCatalogProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(UpdateProfileLocationIntoCatalogProcessor.class));

    /**
     * Constructor
     */
    public UpdateProfileLocationIntoCatalogProcessor() {
        super(PackageType.UPDATE_PROFILE_GEOLOCATION_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        UpdateProfileMsjRespond updateProfileMsjRespond;
        UpdateProfileGeolocationMsgRequest messageContent = UpdateProfileGeolocationMsgRequest.parseContent(packageReceived.getContent());

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            switch (messageContent.getType()) {
                case ACTOR:
                    updateProfileMsjRespond = updateActor(messageContent);
                    break;
                default:
                    updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, "Profile type not supported: "+messageContent.getType(), messageContent.getIdentityPublicKey());
            }

            channel.sendPackage(session, updateProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_ACTOR_PROFILE_RESPONSE, destinationIdentityPublicKey);

            LOG.info("Process finish");

        }catch (Exception exception){

            updateProfileMsjRespond = new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, exception.getMessage(), (messageContent != null && messageContent.getIdentityPublicKey() != null) ? messageContent.getIdentityPublicKey() : null);

            try {

                LOG.error(exception);
                channel.sendPackage(session, updateProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_ACTOR_PROFILE_RESPONSE, destinationIdentityPublicKey);

            }catch (Exception e) {
               LOG.error(e);
            }
        }

    }

    private UpdateProfileMsjRespond updateActor(UpdateProfileGeolocationMsgRequest messageContent) throws Exception {

        /*
         * Validate if exists
         */
        if (JPADaoFactory.getActorCatalogDao().exist(messageContent.getIdentityPublicKey())){

            LOG.info("Updating actor profile location");

            Timestamp currentMillis = new Timestamp(System.currentTimeMillis());

            //Actor update
            ActorCatalog actorCatalog = JPADaoFactory.getActorCatalogDao().findById(messageContent.getIdentityPublicKey());

            actorCatalog.setLocation(messageContent.getLocation().getLatitude(), messageContent.getLocation().getLongitude());
            actorCatalog.setLastConnection(currentMillis);
            actorCatalog.setLastUpdateTime(currentMillis);
            actorCatalog.setLastUpdateType(ActorCatalogUpdateTypes.GEO);
            actorCatalog.setVersion(actorCatalog.getVersion() + 1);
            actorCatalog.setTriedToPropagateTimes(0);
            actorCatalog.setPendingPropagations(ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

            JPADaoFactory.getActorCatalogDao().update(actorCatalog);

            /*
             * If all ok, respond whit success message
             */
            return new UpdateProfileMsjRespond(MsgRespond.STATUS.SUCCESS, MsgRespond.STATUS.SUCCESS.toString(), messageContent.getIdentityPublicKey());
        } else {
            return new UpdateProfileMsjRespond(MsgRespond.STATUS.FAIL, "The actor profile does not exist in the catalog.", messageContent.getIdentityPublicKey());
        }
    }

}

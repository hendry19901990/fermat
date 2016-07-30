package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.ActorCallMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorCallMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ResultDiscoveryTraceActor;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorCallRequestProcessor</code>
 * process all packages received the type <code>MessageType.ACTOR_CALL_REQUEST</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 19/05/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCallRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorCallRequestProcessor.class));

    /**
     * Constructor
     */
    public ActorCallRequestProcessor() {
        super(PackageType.ACTOR_CALL_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorCallMsgRespond actorCallMsgRespond;

        try {

            ActorCallMsgRequest messageContent = ActorCallMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            ResultDiscoveryTraceActor traceActor = getActor(messageContent.getActorTo().getIdentityPublicKey());

            if (traceActor != null)
                actorCallMsgRespond = new ActorCallMsgRespond(messageContent.getNetworkServiceType(), traceActor, ActorCallMsgRespond.STATUS.SUCCESS, ActorCallMsgRespond.STATUS.SUCCESS.toString());
            else
                actorCallMsgRespond = new ActorCallMsgRespond(null, null, ActorCallMsgRespond.STATUS.FAIL, "Actor data could not be found.");

            channel.sendPackage(session, actorCallMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_CALL_RESPONSE, destinationIdentityPublicKey);

        } catch (Exception exception){

            try {

                exception.printStackTrace();
                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                actorCallMsgRespond = new ActorCallMsgRespond(null, null, ActorCallMsgRespond.STATUS.FAIL, exception.getLocalizedMessage());

                channel.sendPackage(session, actorCallMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_CALL_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }

    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param publicKey
     * @return ActorProfile
     */
    private ResultDiscoveryTraceActor getActor(String publicKey) throws CantReadRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        ActorCatalog actorCatalog = JPADaoFactory.getActorCatalogDao().findById(publicKey);

        ActorProfile actorProfile = new ActorProfile();

        if (actorCatalog != null){
            actorProfile.setIdentityPublicKey(actorCatalog.getClient().getId());
            actorProfile.setAlias(actorCatalog.getAlias());
            actorProfile.setName(actorCatalog.getName());
            actorProfile.setActorType(actorCatalog.getActorType());
            actorProfile.setPhoto(actorCatalog.getPhoto());
            actorProfile.setExtraData(actorCatalog.getExtraData());

            //Client
            actorProfile.setClientIdentityPublicKey(actorCatalog.getClient().getId());

            //Location
            GeoLocation location = new GeoLocation();
            if (actorCatalog.getLocation() != null){
                location.setAccuracy(actorCatalog.getLocation().getAccuracy());
                location.setLatitude(actorCatalog.getLocation().getLatitude());
                location.setLongitude(actorCatalog.getLocation().getLongitude());

                actorProfile.setLocation(location);
            }
            NodeCatalog nodeCatalog = null;

            try {
                //Node Catalog
                nodeCatalog = JPADaoFactory.getNodeCatalogDao().findById(actorCatalog.getHomeNode().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(nodeCatalog != null) {

                NodeProfile nodeProfile = new NodeProfile();
                nodeProfile.setIdentityPublicKey(nodeCatalog.getId());
                nodeProfile.setName(nodeCatalog.getName());
                nodeProfile.setIp(nodeCatalog.getIp());
                nodeProfile.setDefaultPort(nodeCatalog.getDefaultPort());

                return new ResultDiscoveryTraceActor(nodeProfile, actorProfile);
            }
        }
        else
        {
            LOG.error("Error find Actor Catalog");
        }
        return null;
    }

}

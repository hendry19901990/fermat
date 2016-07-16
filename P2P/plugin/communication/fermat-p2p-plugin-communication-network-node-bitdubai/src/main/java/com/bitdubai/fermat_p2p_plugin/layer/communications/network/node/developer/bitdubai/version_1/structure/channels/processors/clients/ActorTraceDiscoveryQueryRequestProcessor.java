package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileDiscoveryQueryMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorsProfileListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ResultDiscoveryTraceActor;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorTraceDiscoveryQueryRequestProcessor</code>
 * process all packages received the type <code>MessageType.ACTOR_TRACE_DISCOVERY_QUERY_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 27/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ActorTraceDiscoveryQueryRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorTraceDiscoveryQueryRequestProcessor.class));

    /**
     * Constructor
     */
    public ActorTraceDiscoveryQueryRequestProcessor() {
        super(PackageType.ACTOR_TRACE_DISCOVERY_QUERY_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        List<ResultDiscoveryTraceActor> profileList = null;
        NetworkServiceType networkServiceTypeIntermediate = null;

        try {

            CheckInProfileDiscoveryQueryMsgRequest messageContent = CheckInProfileDiscoveryQueryMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent.getDiscoveryQueryParameters()), destinationIdentityPublicKey);

            /*
             * Validate if content type is the correct
             */
            if (messageContent.getMessageContentType() == MessageContentType.JSON) {

                /*
                 * Get the parameters to filters
                 */
                DiscoveryQueryParameters discoveryQueryParameters = messageContent.getDiscoveryQueryParameters();

                /*
                 * get the NetworkServiceIntermediate
                 */
                networkServiceTypeIntermediate = discoveryQueryParameters.getNetworkServiceTypeIntermediate();

                /*
                 * Validate if a network service search
                 */
                if (discoveryQueryParameters.getNetworkServiceType() == null){

                    /*
                     * Find in the data base
                     */
                    profileList = filterActors(discoveryQueryParameters);

                    if(profileList != null && profileList.size() == 0)
                        throw new Exception("Not Found row in the Table");

                }

                /*
                 * If all ok, respond whit success message
                 */
                ActorsProfileListMsgRespond actorsProfileListMsgRespond = new ActorsProfileListMsgRespond(ActorsProfileListMsgRespond.STATUS.SUCCESS, ActorsProfileListMsgRespond.STATUS.SUCCESS.toString(), profileList, networkServiceTypeIntermediate);
                Package packageRespond = Package.createInstance(actorsProfileListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_TRACE_DISCOVERY_QUERY_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            }

        }catch (Exception exception){

            try {

                LOG.error(exception.getMessage());

                /*
                 * Respond whit fail message
                 */
                ActorsProfileListMsgRespond actorsProfileListMsgRespond = new ActorsProfileListMsgRespond(ActorsProfileListMsgRespond.STATUS.FAIL, exception.getLocalizedMessage(), profileList, networkServiceTypeIntermediate);
                Package packageRespond = Package.createInstance(actorsProfileListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_TRACE_DISCOVERY_QUERY_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param discoveryQueryParameters
     * @return List<ActorProfile>
     */
    private List<ResultDiscoveryTraceActor> filterActors(DiscoveryQueryParameters discoveryQueryParameters) throws CantReadRecordDataBaseException, InvalidParameterException {

        List<ResultDiscoveryTraceActor> profileList = new ArrayList<>();

        int max    = 10;
        int offset =  0;

        if (discoveryQueryParameters.getMax() != null && discoveryQueryParameters.getMax() > 0)
            max = (discoveryQueryParameters.getMax() > 100) ? 100 : discoveryQueryParameters.getMax();

        if (discoveryQueryParameters.getOffset() != null && discoveryQueryParameters.getOffset() >= 0)
            offset = discoveryQueryParameters.getOffset();

        List<ActorsCatalog> actors = getDaoFactory().getActorsCatalogDao().findAll(discoveryQueryParameters, null, max, offset);

        for (ActorsCatalog actorsCatalog : actors) {

            ActorProfile actorProfile = new ActorProfile();
            actorProfile.setIdentityPublicKey(actorsCatalog.getIdentityPublicKey());
            actorProfile.setAlias(actorsCatalog.getAlias());
            actorProfile.setName(actorsCatalog.getName());
            actorProfile.setActorType(actorsCatalog.getActorType());
            actorProfile.setPhoto(actorsCatalog.getPhoto());
            actorProfile.setExtraData(actorsCatalog.getExtraData());
            actorProfile.setClientIdentityPublicKey(actorsCatalog.getClientIdentityPublicKey());
            actorProfile.setLocation(actorsCatalog.getLastLocation());

            NodesCatalog nodesCatalog = null;

            try {
                nodesCatalog = getDaoFactory().getNodesCatalogDao().findById(actorsCatalog.getNodeIdentityPublicKey());
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }

            if(nodesCatalog != null) {

                NodeProfile nodeProfile = new NodeProfile();
                nodeProfile.setIdentityPublicKey(nodesCatalog.getIdentityPublicKey());
                nodeProfile.setName(nodesCatalog.getName());
                nodeProfile.setIp(nodesCatalog.getIp());
                nodeProfile.setDefaultPort(nodesCatalog.getDefaultPort());

                ResultDiscoveryTraceActor resultDiscoveryTraceActor = new ResultDiscoveryTraceActor(nodeProfile, actorProfile);
                profileList.add(resultDiscoveryTraceActor);

            }

        }

        return profileList;
    }

}

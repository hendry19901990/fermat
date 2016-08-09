package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.ActorListMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorCallMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.google.gson.JsonObject;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorListRequestProcessor</code>
 * process all packages received the type <code>MessageType.ACTOR_LIST_REQUEST</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/06/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorListRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorListRequestProcessor.class));

    /**
     * Constructor
     */
    public ActorListRequestProcessor() {
        super(PackageType.ACTOR_LIST_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received " + packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorListMsgRequest messageContent = ActorListMsgRequest.parseContent(packageReceived.getContent());

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            List<ActorProfile> actorsList = filterActors(messageContent.getParameters(), messageContent.getClientPublicKey());

            /*
             * If all ok, respond whit success message
             */
            ActorListMsgRespond actorListMsgRespond = new ActorListMsgRespond(ActorCallMsgRespond.STATUS.SUCCESS, ActorCallMsgRespond.STATUS.SUCCESS.toString(), actorsList, messageContent.getNetworkServicePublicKey(), messageContent.getQueryId());

            channel.sendPackage(session, actorListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_LIST_RESPONSE, destinationIdentityPublicKey);

        } catch (Exception exception){

            try {

                LOG.error(exception.getMessage());
                exception.printStackTrace();

                /*
                 * Respond whit fail message
                 */
                ActorListMsgRespond actorListMsgRespond = new ActorListMsgRespond(
                        ActorListMsgRespond.STATUS.FAIL,
                        exception.getLocalizedMessage(),
                        null,
                        null,
                        (messageContent == null ? null : messageContent.getQueryId())
                );

                channel.sendPackage(session, actorListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_LIST_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

    }

    /**
     * Filter all actor component profiles from database that match with the given parameters.
     * We'll use the @clientIdentityPublicKey param to filter the actors who belongs to the client asking.
     *
     * @param discoveryQueryParameters parameters of the discovery done by the user.
     *
     * @return a list of actor profiles.
     */
    private List<ActorProfile> filterActors(final DiscoveryQueryParameters discoveryQueryParameters,
                                            final String                   clientIdentityPublicKey ) throws CantReadRecordDataBaseException {

        Map<String, ActorProfile> profileList = new HashMap<>();

        List<ActorCatalog> actorsList;

        int max    = 10;
        int offset =  0;

        if (discoveryQueryParameters.getMax() != null && discoveryQueryParameters.getMax() > 0)
            max = (discoveryQueryParameters.getMax() > 20) ? 20 : discoveryQueryParameters.getMax();

        if (discoveryQueryParameters.getOffset() != null && discoveryQueryParameters.getOffset() >= 0)
            offset = discoveryQueryParameters.getOffset();

        actorsList = JPADaoFactory.getActorCatalogDao().findAll(discoveryQueryParameters, clientIdentityPublicKey, max, offset);

        if (discoveryQueryParameters.isOnline())
            for (ActorCatalog actorsCatalog : actorsList)
                profileList.put(actorsCatalog.getId(), buildActorProfileFromActorCatalogRecordAndSetStatus(actorsCatalog));
        else
            for (ActorCatalog actorsCatalog : actorsList)
                profileList.put(actorsCatalog.getId(), actorsCatalog.getActorProfile());

        return new ArrayList<>(profileList.values());
    }

    /**
     * Build an Actor Profile from an Actor Catalog record and set its status.
     */
    private ActorProfile buildActorProfileFromActorCatalogRecordAndSetStatus(final ActorCatalog actor){

        ActorProfile actorProfile = new ActorProfile();

        actorProfile.setIdentityPublicKey(actor.getId());
        actorProfile.setAlias(actor.getAlias());
        actorProfile.setName(actor.getName());
        actorProfile.setActorType(actor.getActorType());
        actorProfile.setPhoto(actor.getPhoto());
        actorProfile.setExtraData(actor.getExtraData());
        actorProfile.setLocation(actor.getLocation());

        actorProfile.setStatus(isActorOnline(actor));

        return actorProfile;
    }

    /**
     * Through this method we're going to determine a status for the actor profile.
     * First we'll check if the actor belongs to this node:
     *   if it belongs we'll check directly if he is online in the check-ins table
     *   if not we'll call to the other node.
     *
     * @param actorsCatalog  the record of the profile from the actors catalog table.
     *
     * @return an element of the ProfileStatus enum.
     */
    private ProfileStatus isActorOnline(ActorCatalog actorsCatalog) {

        try {

            if(actorsCatalog.getId().equals(getNetworkNodePluginRoot().getIdentity().getPublicKey())) {

                if (actorsCatalog.getSession() != null)
                    return ProfileStatus.ONLINE;
                else
                    return ProfileStatus.OFFLINE;

            } else {

                return isActorOnlineInOtherNode(actorsCatalog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ProfileStatus.UNKNOWN;
        }
    }

    /**
     * Through this method we're going to determine a status for the actor profile calling another node.
     *
     * @param actorsCatalog  the record of the profile from the actors catalog table.
     *
     * @return an element of the ProfileStatus enum.
     */
    private ProfileStatus isActorOnlineInOtherNode(final ActorCatalog actorsCatalog) {

        try {

            String nodeUrl = actorsCatalog.getHomeNode().getIp()+":"+actorsCatalog.getHomeNode().getDefaultPort();

            URL url = new URL("http://" + nodeUrl + "/fermat/rest/api/v1/online/component/actor/" + actorsCatalog.getId());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String respond = reader.readLine();

            if (conn.getResponseCode() == 200 && respond != null && respond.contains("success")) {
                JsonObject respondJsonObject = (JsonObject) GsonProvider.getJsonParser().parse(respond.trim());
                return respondJsonObject.get("isOnline").getAsBoolean() ? ProfileStatus.ONLINE : ProfileStatus.OFFLINE;

            } else {
                return ProfileStatus.UNKNOWN;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ProfileStatus.UNKNOWN;
        }
    }

    private NetworkNodePluginRoot pluginRoot;

    private NetworkNodePluginRoot getNetworkNodePluginRoot() {

        if (pluginRoot == null)
            pluginRoot = (NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT);

        return pluginRoot;
    }
}

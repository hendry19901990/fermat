package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileDiscoveryQueryMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.Profile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.DistanceCalculator;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInProfileDiscoveryQueryRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 27/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInProfileDiscoveryQueryRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInProfileDiscoveryQueryRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInProfileDiscoveryQueryRequestProcessor() {
        super(PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        CheckInProfileDiscoveryQueryMsgRequest messageContent = CheckInProfileDiscoveryQueryMsgRequest.parseContent(packageReceived.getContent());
        List<Profile> profileList = null;
        DiscoveryQueryParameters discoveryQueryParameters = messageContent.getDiscoveryQueryParameters();

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Validate if a network service search
             */
            if (discoveryQueryParameters.getNetworkServiceType() != null && discoveryQueryParameters.getNetworkServiceType() !=  NetworkServiceType.UNDEFINED){

                /*
                 * Find in the data base
                 */
                profileList = filterNetworkServices(discoveryQueryParameters);

            }else{

                /*
                 * Find in the data base
                 */
                profileList = filterActors(discoveryQueryParameters);
            }

            if(profileList != null && profileList.size() == 0)
                throw new Exception("Not Found row in the Table");

            /*
             * Apply geolocation
             */
            if(discoveryQueryParameters.getLocation() != null)
                profileList = applyGeoLocationFilter(discoveryQueryParameters.getLocation(), profileList, discoveryQueryParameters.getDistance());

            /*
             * Apply pagination
             */
            if ((discoveryQueryParameters.getMax() != 0) && (discoveryQueryParameters.getOffset() != 0)){

                /*
                 * Apply pagination
                 */
                if (profileList.size() > discoveryQueryParameters.getMax() &&
                        profileList.size() > discoveryQueryParameters.getOffset()){
                    profileList =  profileList.subList(discoveryQueryParameters.getOffset(), discoveryQueryParameters.getMax());
                }else if (profileList.size() > 100) {
                    profileList = profileList.subList(discoveryQueryParameters.getOffset(), 100);
                }

            }else if (profileList.size() > 100) {
                profileList = profileList.subList(0, 100);
            }

            /*
             * If all ok, respond whit success message
             */
            CheckInProfileListMsgRespond checkInProfileListMsgRespond = new CheckInProfileListMsgRespond(CheckInProfileListMsgRespond.STATUS.SUCCESS, CheckInProfileListMsgRespond.STATUS.SUCCESS.toString(), profileList, discoveryQueryParameters);
            channel.sendPackage(session, checkInProfileListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_RESPONSE, destinationIdentityPublicKey);

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileListMsgRespond checkInProfileListMsgRespond = new CheckInProfileListMsgRespond(CheckInProfileListMsgRespond.STATUS.FAIL, exception.getLocalizedMessage(), profileList, discoveryQueryParameters);
                channel.sendPackage(session, checkInProfileListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }

        }

    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param discoveryQueryParameters
     * @return List<Profile>
     */
    private List<Profile> filterNetworkServices(DiscoveryQueryParameters discoveryQueryParameters) throws CantReadRecordDataBaseException, InvalidParameterException {

        List<Profile> profileList = new ArrayList<>();

        Map<String, Object> filters = constructFiltersNetworkServiceTable(discoveryQueryParameters);
        List<CheckedInProfile> networkServices = getDaoFactory().getCheckedInProfilesDao().findAll(ProfileTypes.NETWORK_SERVICE, filters);

        for (CheckedInProfile checkedInNetworkService : networkServices) {

            NetworkServiceProfile networkServiceProfile = new NetworkServiceProfile();
            networkServiceProfile.setIdentityPublicKey(checkedInNetworkService.getIdentityPublicKey());
            networkServiceProfile.setClientIdentityPublicKey(checkedInNetworkService.getClientPublicKey());
            networkServiceProfile.setNetworkServiceType(NetworkServiceType.getByCode(checkedInNetworkService.getInformation()));

            networkServiceProfile.setLocation(checkedInNetworkService.getLocation());

            profileList.add(networkServiceProfile);

        }

        return profileList;
    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param discoveryQueryParameters
     * @return List<Profile>
     */
    private List<Profile> filterActors(DiscoveryQueryParameters discoveryQueryParameters) throws CantReadRecordDataBaseException, InvalidParameterException {

        List<Profile> profileList = new ArrayList<>();

        Map<String, String> filters = constructFiltersActorTable(discoveryQueryParameters);
        List<ActorsCatalog> actores = getDaoFactory().getActorsCatalogDao().findAllActorCheckedIn(filters, null, null);

        if(actores != null) {
            for (ActorsCatalog checkedInActor : actores) {

                ActorProfile actorProfile = new ActorProfile();
                actorProfile.setIdentityPublicKey(checkedInActor.getIdentityPublicKey());
                actorProfile.setAlias(checkedInActor.getAlias());
                actorProfile.setName(checkedInActor.getName());
                actorProfile.setActorType(checkedInActor.getActorType());
                actorProfile.setPhoto(checkedInActor.getPhoto());
                actorProfile.setExtraData(checkedInActor.getExtraData());
                actorProfile.setClientIdentityPublicKey(checkedInActor.getClientIdentityPublicKey());

                actorProfile.setLocation(checkedInActor.getLastLocation());

                profileList.add(actorProfile);

            }
        }

        return profileList;
    }


    /**
     * Construct data base filter from discovery query parameters
     *
     * @param discoveryQueryParameters
     * @return Map<String, Object> filters
     */
    private Map<String, Object> constructFiltersNetworkServiceTable(DiscoveryQueryParameters discoveryQueryParameters){

        Map<String, Object> filters = new HashMap<>();

        if (discoveryQueryParameters.getIdentityPublicKey() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME, discoveryQueryParameters.getIdentityPublicKey());

        if (discoveryQueryParameters.getNetworkServiceType() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME, discoveryQueryParameters.getNetworkServiceType().getCode());

        return filters;
    }


    /**
     * Method that apply geo location filter to the list
     *
     * @param filterLocation
     * @param profileList
     * @param distance
     * @return List<Profile>
     */
    private List<Profile> applyGeoLocationFilter(Location filterLocation, List<Profile> profileList, Double distance) {

        /*
         * Hold the data ordered by distance
         */
        Map<Double, Profile> orderedByDistance = new TreeMap<>();

        /*
         * For each node
         */
        for (final Profile profile: profileList) {

            /*
             * If component have a geo location
             */
            if (profile.getLocation() != null){

                /*
                 * Calculate the distance between the two points
                 */
                Double componentDistance = DistanceCalculator.distance(filterLocation, profile.getLocation(), DistanceCalculator.KILOMETERS);

                if (distance != null){

                    if (componentDistance <= distance){

                        /*
                         * Add to the list
                         */
                        orderedByDistance.put(componentDistance, profile);
                    }

                }else {

                    /*
                     * Add to the list
                     */
                    orderedByDistance.put(componentDistance, profile);

                }

            }

        }

        return new ArrayList<>(orderedByDistance.values());
    }



    /**
     * Construct data base filter from discovery query parameters
     *
     * @param discoveryQueryParameters
     * @return Map<String, Object> filters
     */
    private Map<String, String> constructFiltersActorTable(DiscoveryQueryParameters discoveryQueryParameters){

        Map<String, String> filters = new HashMap<>();

        if (discoveryQueryParameters.getIdentityPublicKey() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME, discoveryQueryParameters.getIdentityPublicKey());

        if (discoveryQueryParameters.getName() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_NAME_COLUMN_NAME, discoveryQueryParameters.getName());

        if (discoveryQueryParameters.getAlias() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_ALIAS_COLUMN_NAME, discoveryQueryParameters.getAlias());

        if (discoveryQueryParameters.getActorType() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_ACTOR_TYPE_COLUMN_NAME, discoveryQueryParameters.getActorType());


        if (discoveryQueryParameters.getExtraData() != null)
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.ACTOR_CATALOG_EXTRA_DATA_COLUMN_NAME, discoveryQueryParameters.getExtraData());


        return filters;
    }

}

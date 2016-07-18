package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.rest.services;

import com.bitdubai.fermat_api.layer.all_definition.location_system.NetworkNodeCommunicationDeviceLocation;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.rest.NetworkData</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 16/06/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Path("/network")
public class NetworkData {


    /**
     * Represent the gson
     */
    private Gson gson;

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkData.class));

    /**
     * Represent the daoFactory
     */
    private DaoFactory daoFactory;

    /*
     * Represent the pluginRoot
     */
    private NetworkNodePluginRoot pluginRoot;

    /**
     * Constructor
     */
    public NetworkData(){
        daoFactory = (DaoFactory) NodeContext.get(NodeContextItem.DAO_FACTORY);
        pluginRoot = (NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT);
        this.gson = GsonProvider.getGson();
    }


    @GET
    @Path("/catalog")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNodes(){

        try {
            /*
             * Get the node catalog list
             */
            List<NodesCatalog> nodesCatalogs = daoFactory.getNodesCatalogDao().findAll();
            List<String> nodes = new ArrayList<>();

            if(nodesCatalogs != null){
                for(NodesCatalog node : nodesCatalogs){
                    nodes.add(node.getIp());
                }
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nodes", gson.toJson(nodes));

            return Response.status(200).entity(gson.toJson(jsonObject)).build();

        } catch (Exception e) {

            LOG.error("Error trying to list nodes.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code",e.hashCode());
            jsonObject.addProperty("message",e.getMessage());
            jsonObject.addProperty("details",gson.toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error",gson.toJson(jsonObject));

            return Response.status(200).entity(gson.toJson(jsonObjectError)).build();

        }


    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServerData(){

        try{

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("hash",pluginRoot.getIdentity().getPublicKey());

            if(pluginRoot.getNodeProfile() != null && pluginRoot.getNodeProfile().getLocation() != null){
                jsonObject.addProperty("location", gson.toJson(pluginRoot.getNodeProfile().getLocation()));
            }else{

                Location location = new NetworkNodeCommunicationDeviceLocation(
                        0.0 ,
                        0.0,
                        0.0     ,
                        0        ,
                        0.0     ,
                        System.currentTimeMillis(),
                        LocationSource.UNKNOWN
                );

                jsonObject.addProperty("location", gson.toJson(location));
            }

            jsonObject.addProperty("os","");
            jsonObject.addProperty("networkServices",gson.toJson(getNetworkServicesCount()));

            return Response.status(200).entity(gson.toJson(jsonObject)).build();

        }catch (Exception e){

            LOG.error("Error trying to get server data.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code",e.hashCode());
            jsonObject.addProperty("message",e.getMessage());
            jsonObject.addProperty("details",gson.toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error",gson.toJson(jsonObject));

            return Response.status(200).entity(gson.toJson(jsonObjectError)).build();

        }

    }

    @GET
    @Path("/clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(){

        try{

            List<String> listOfClients = new ArrayList<>();

            List<CheckedInProfile> listCheckedInProfileS = daoFactory.getCheckedInProfilesDao().findAll(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME, ProfileTypes.CLIENT.getCode());

            if(listCheckedInProfileS != null){

                for(CheckedInProfile CheckedInProfile : listCheckedInProfileS){

                    JsonObject jsonObjectClient = new JsonObject();
                    jsonObjectClient.addProperty("hash", CheckedInProfile.getIdentityPublicKey());
                    jsonObjectClient.addProperty("location", gson.toJson(CheckedInProfile.getLocation()));
                    jsonObjectClient.addProperty("networkServices",gson.toJson(getListOfNetworkServiceOfClientSpecific(CheckedInProfile.getIdentityPublicKey())));

                    listOfClients.add(gson.toJson(jsonObjectClient));

                }
            }

            return Response.status(200).entity(gson.toJson(listOfClients)).build();

        }catch (Exception e){
            LOG.error("Error trying to list clients.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code",e.hashCode());
            jsonObject.addProperty("message",e.getMessage());
            jsonObject.addProperty("details",gson.toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error",gson.toJson(jsonObject));

            return Response.status(200).entity(gson.toJson(jsonObjectError)).build();
        }
    }

    @GET
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActors(){

        try{

            List<String> actors = new ArrayList<>();

            List<ActorsCatalog> listOfCheckedInActor = daoFactory.getActorsCatalogDao().findAllActorCheckedIn(new HashMap<String, String>(), null, null);

            if(listOfCheckedInActor != null){

                for(ActorsCatalog checkedInActor :listOfCheckedInActor){

                    JsonObject jsonObjectActor = new JsonObject();
                    jsonObjectActor.addProperty("hash", checkedInActor.getIdentityPublicKey());
                    jsonObjectActor.addProperty("type", checkedInActor.getActorType());
                    jsonObjectActor.addProperty("links",gson.toJson(new ArrayList<>()));

                    jsonObjectActor.addProperty("location", gson.toJson(checkedInActor.getLastLocation()));

                    JsonObject jsonObjectActorProfile = new JsonObject();
                    jsonObjectActorProfile.addProperty("phrase", "There is not Phrase");
                    jsonObjectActorProfile.addProperty("picture", Base64.encodeBase64String(checkedInActor.getPhoto()));
                    jsonObjectActorProfile.addProperty("name", checkedInActor.getName());

                    jsonObjectActor.addProperty("profile", gson.toJson(jsonObjectActorProfile));


                    actors.add(gson.toJson(jsonObjectActor));
                }

            }

            return Response.status(200).entity(gson.toJson(actors)).build();

        }catch (Exception e){

            LOG.error("Error trying to list actors.", e);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code",e.hashCode());
            jsonObject.addProperty("message",e.getMessage());
            jsonObject.addProperty("details",gson.toJson(e.getCause()));


            JsonObject jsonObjectError = new JsonObject();
            jsonObjectError.addProperty("error",gson.toJson(jsonObject));

            return Response.status(200).entity(gson.toJson(jsonObjectError)).build();
        }

    }


    private Map<NetworkServiceType,Long> getNetworkServicesCount(){

        Map<NetworkServiceType,Long> listNetworkServicesCount = new HashMap<>();

        List<NetworkServiceType> listOfNetworkService = getListOfNetworkService();

        if(listOfNetworkService != null){

            for(NetworkServiceType networkServiceType : listOfNetworkService){

                try {
                    Map<String, String> filtersList = new HashMap<>();

                    filtersList.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME, networkServiceType.getCode());
                    filtersList.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME, ProfileTypes.NETWORK_SERVICE.getCode());

                    Long count = daoFactory.getCheckedInProfilesDao().getAllCount(filtersList);

                    listNetworkServicesCount.put(networkServiceType,count);
                } catch (CantReadRecordDataBaseException e) {

                    LOG.error("Error trying to get network services count.", e);
                }

            }

        }

        return listNetworkServicesCount;
    }

    private List<NetworkServiceType> getListOfNetworkService(){

        Map<NetworkServiceType, NetworkServiceType> listNetworkServices = new HashMap<>();

        try {
            List<CheckedInProfile> checkedInNetworkServiceList = daoFactory.getCheckedInProfilesDao().findAll(CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME, ProfileTypes.NETWORK_SERVICE.getCode());

            if(checkedInNetworkServiceList != null){

                for(CheckedInProfile CheckedInNetworkService : checkedInNetworkServiceList){

                    if(!listNetworkServices.containsKey(NetworkServiceType.getByCode(CheckedInNetworkService.getInformation())))
                        listNetworkServices.put(NetworkServiceType.getByCode(CheckedInNetworkService.getInformation()),NetworkServiceType.getByCode(CheckedInNetworkService.getInformation()));

                }

                return new ArrayList<>(listNetworkServices.values());

            }


        } catch (Exception e) {
            LOG.error("Error trying to list network services of a specific client.", e);
        }

        return new ArrayList<>();

    }

    private List<NetworkServiceType> getListOfNetworkServiceOfClientSpecific(String publicKeyClient){

        Map<NetworkServiceType, NetworkServiceType> listNetworkServices = new HashMap<>();

        try {

            Map<String, String> filtersList = new HashMap<>();

            filtersList.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME, publicKeyClient);
            filtersList.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME, ProfileTypes.NETWORK_SERVICE.getCode());

            List<CheckedInProfile> checkedInNetworkServiceList = daoFactory.getCheckedInProfilesDao().findAll(filtersList);

            if(checkedInNetworkServiceList != null){

                for(CheckedInProfile checkedInNetworkService : checkedInNetworkServiceList)
                    if(!listNetworkServices.containsKey(NetworkServiceType.getByCode(checkedInNetworkService.getInformation())))
                        listNetworkServices.put(NetworkServiceType.getByCode(checkedInNetworkService.getInformation()),NetworkServiceType.getByCode(checkedInNetworkService.getInformation()));

                return new ArrayList<>(listNetworkServices.values());
            }

        } catch (Exception e) {
            LOG.error("Error trying to list network services of a specific client.", e);
        }

        return new ArrayList<>();

    }



}

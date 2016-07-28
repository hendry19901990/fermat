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
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
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

    /*
     * Represent the pluginRoot
     */
    private NetworkNodePluginRoot pluginRoot;

    /**
     * Constructor
     */
    public NetworkData(){
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
            List<NodeCatalog> nodesCatalogs = JPADaoFactory.getNodeCatalogDao().list();
            List<String> nodes = new ArrayList<>();

            if(nodesCatalogs != null){
                for(NodeCatalog node : nodesCatalogs){
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

            List<ClientSession> listCheckedInProfileS = JPADaoFactory.getClientSessionDao().list();

            if(listCheckedInProfileS != null){

                for(ClientSession CheckedInProfile : listCheckedInProfileS){

                    JsonObject jsonObjectClient = new JsonObject();
                    jsonObjectClient.addProperty("hash", CheckedInProfile.getClient().getId());
                    jsonObjectClient.addProperty("location", gson.toJson(CheckedInProfile.getClient().getLocation()));
                    jsonObjectClient.addProperty("networkServices",gson.toJson(getListOfNetworkServiceOfClientSpecific(CheckedInProfile.getClient().getId())));

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

            List<ActorSession> listOfCheckedInActor = JPADaoFactory.getActorSessionDao().list();

            if(listOfCheckedInActor != null){

                for(ActorSession checkedInActor :listOfCheckedInActor){

                    JsonObject jsonObjectActor = new JsonObject();
                    jsonObjectActor.addProperty("hash", checkedInActor.getActor().getId());
                    jsonObjectActor.addProperty("type", checkedInActor.getActor().getActorType());
                    jsonObjectActor.addProperty("links",gson.toJson(new ArrayList<>()));

                    jsonObjectActor.addProperty("location", gson.toJson(checkedInActor.getActor().getLocation()));

                    JsonObject jsonObjectActorProfile = new JsonObject();
                    jsonObjectActorProfile.addProperty("phrase", "There is not Phrase");
                    jsonObjectActorProfile.addProperty("picture", Base64.encodeBase64String(checkedInActor.getActor().getPhoto()));
                    jsonObjectActorProfile.addProperty("name", checkedInActor.getActor().getName());

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
                    Map<String, Object> filtersList = new HashMap<>();

                    filtersList.put("networkService.networkServiceType", ProfileTypes.NETWORK_SERVICE.getCode());

                    Integer count = JPADaoFactory.getNetworkServiceSessionDao().count(filtersList);

                    listNetworkServicesCount.put(networkServiceType, Long.valueOf(count));
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
            List<NetworkServiceSession> checkedInNetworkServiceList = JPADaoFactory.getNetworkServiceSessionDao().list();

            if(checkedInNetworkServiceList != null){

                for(NetworkServiceSession CheckedInNetworkService : checkedInNetworkServiceList){

                    if(!listNetworkServices.containsKey(CheckedInNetworkService.getNetworkService().getNetworkServiceType()))
                        listNetworkServices.put(CheckedInNetworkService.getNetworkService().getNetworkServiceType(),CheckedInNetworkService.getNetworkService().getNetworkServiceType());

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

            Map<String, Object> filtersList = new HashMap<>();

            filtersList.put("networkService.client.id", publicKeyClient);
            filtersList.put("networkService.networkServiceType", ProfileTypes.NETWORK_SERVICE.getCode());

            List<NetworkServiceSession> checkedInNetworkServiceList = JPADaoFactory.getNetworkServiceSessionDao().list(filtersList);

            if(checkedInNetworkServiceList != null){

                for(NetworkServiceSession checkedInNetworkService : checkedInNetworkServiceList)
                    if(!listNetworkServices.containsKey(checkedInNetworkService.getNetworkService().getNetworkServiceType()))
                        listNetworkServices.put(checkedInNetworkService.getNetworkService().getNetworkServiceType(),checkedInNetworkService.getNetworkService().getNetworkServiceType());

                return new ArrayList<>(listNetworkServices.values());
            }

        } catch (Exception e) {
            LOG.error("Error trying to list network services of a specific client.", e);
        }

        return new ArrayList<>();

    }



}

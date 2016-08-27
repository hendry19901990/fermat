package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.location_system.NetworkNodeCommunicationDeviceLocation;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;
import com.bitdubai.fermat_api.layer.osa_android.location_system.exceptions.CantGetDeviceLocationException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientActorUnreachableEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientCallConnectedEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientConnectionLostEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRegisterProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRequestActorFullPhotoException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRequestProfileListException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantUnregisterProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantUpdateRegisteredProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientCall;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientConnection;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.BlockPackages;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.ActorCallMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.ActorListMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileDiscoveryQueryMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckOutProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.NearNodeListMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.UpdateActorProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.UpdateProfileGeolocationMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.UpdateTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.Profile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.CommunicationChannels;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.NetworkClientCommunicationPluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.channels.endpoints.NetworkClientCommunicationChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.conf.ClientChannelConfigurator;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.context.ClientContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.context.ClientContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.exceptions.CantSendPackageException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.network_calls.NetworkClientCommunicationCall;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.ActorOnlineHelper;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.ActorOnlineInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.BlockEncoder;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.HardcodeConstants;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.PackageDecoder;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.PackageEncoder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.NetworkClientCommunicationConnection</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 14/04/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NetworkClientCommunicationConnection implements NetworkClientConnection {

    /**
     * Represent the LOG
     */
    private static final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkClientCommunicationConnection.class));

    private String                 nodeUrl               ;
    private URI                    uri                   ;
//    private EventManager           eventManager          ;
    private LocationManager        locationManager       ;
    private ECCKeyPair             clientIdentity        ;

    private CopyOnWriteArrayList<NetworkClientCall> activeCalls;

    /**
     * Represent if it must reconnect to the server
     */
    private boolean tryToReconnect;

    /**
     * Represent the webSocketContainer
     */
    private ClientManager container;

    /**
     * Represent the serverIdentity
     */
    private String serverIdentity;

    /*
     * Represent the pluginRoot
     */
    private NetworkClientCommunicationPluginRoot pluginRoot;

    /*
     * Represent the nodesListPosition
     */
    private Integer nodesListPosition;

    /*
     * Represent the networkClientCommunicationChannel
     */
    private NetworkClientCommunicationChannel networkClientCommunicationChannel;

   /*
    * is used to validate if it is connection to an external node
    * when receive check-in-client then send register all profile
    */
    private boolean isExternalNode;

    /*
     * Represent the nodeProfile, it is used to be save
     * into table NodeConnectionHistory when the client is connected
     */
    private NodeProfile nodeProfile;

    /* JMeter */

    private int totalOfProfileSendToCheckin;
    private int totalOfProfileSuccessChecked;
    private int totalOfProfileFailureToCheckin;

    private int totalOfMessagesSents;
    private int totalOfMessagesSentsSuccessfully;
    private int totalOfMessagesSentsFails;

    private Map<String, String> listPublicKeyProfiles;

    private Map<String, String> listRequestListDiscovery;


    private Map<String, NetworkServiceProfile> listNetworkServiceProfileToCheckin;
    private Map<NetworkServiceType, ActorProfile> listActorProfileToCheckin;
    private static final NetworkServiceType[] networkServiceTypeNames = new NetworkServiceType[]{
            NetworkServiceType.ACTOR_CHAT, NetworkServiceType.ASSET_USER_ACTOR,
            NetworkServiceType.ASSET_ISSUER_ACTOR, NetworkServiceType.ASSET_REDEEM_POINT_ACTOR,
            NetworkServiceType.ASSET_TRANSMISSION, NetworkServiceType.ARTIST_ACTOR,
            NetworkServiceType.FAN_ACTOR, NetworkServiceType.CHAT, NetworkServiceType.CRYPTO_ADDRESSES,
            NetworkServiceType.CRYPTO_BROKER, NetworkServiceType.CRYPTO_CUSTOMER,
            NetworkServiceType.CRYPTO_PAYMENT_REQUEST, NetworkServiceType.CRYPTO_TRANSMISSION,
            NetworkServiceType.INTRA_USER, NetworkServiceType.FERMAT_MONITOR, NetworkServiceType.TRANSACTION_TRANSMISSION,
            NetworkServiceType.NEGOTIATION_TRANSMISSION};

    private ScheduledExecutorService executorServiceToSenderMessage;
    private NetworkClientCommunicationSenderMessage communicationSenderMessage;

    private static final byte[] imageInByteActor = HardcodeConstants.photoActor();

    private boolean isStartedSenderMessage;

    /* JMeter */

    /*
     * Constructor
     */
    public NetworkClientCommunicationConnection(final String                               nodeUrl          ,
                                                final EventManager                         eventManager     ,
                                                final LocationManager                      locationManager  ,
                                                final ECCKeyPair                           clientIdentity   ,
                                                final NetworkClientCommunicationPluginRoot pluginRoot       ,
                                                final Integer                              nodesListPosition,
                                                final boolean                              isExternalNode   ,
                                                final NodeProfile                          nodeProfile      ){

        URI uri = null;
        try {
            uri = new URI(HardcodeConstants.WS_PROTOCOL + nodeUrl + "/iop-node/ws/client-channel");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.nodeUrl                = nodeUrl                     ;
        this.uri                    = uri                         ;
        this.locationManager        = locationManager             ;
        this.clientIdentity         = clientIdentity              ;
        this.isExternalNode         = isExternalNode              ;
        this.pluginRoot             = pluginRoot                  ;
        this.nodesListPosition      = nodesListPosition           ;
        this.nodeProfile            = nodeProfile                 ;

        this.tryToReconnect         = Boolean.TRUE                ;

        this.activeCalls            = new CopyOnWriteArrayList<>();
        this.container              = ClientManager.createClient();
        this.networkClientCommunicationChannel = new NetworkClientCommunicationChannel(this, isExternalNode);
        this.totalOfProfileSendToCheckin = 0;
        this.totalOfProfileSuccessChecked = 0;
        this.totalOfProfileFailureToCheckin = 0;
        this.totalOfMessagesSents = 0;
        this.totalOfMessagesSentsSuccessfully = 0;
        this.totalOfMessagesSentsFails = 0;
        this.listNetworkServiceProfileToCheckin = new HashMap<String, NetworkServiceProfile>();
        this.listActorProfileToCheckin = new HashMap<NetworkServiceType, ActorProfile>();
        this.listPublicKeyProfiles = new HashMap<String,String>();
        this.listRequestListDiscovery = new HashMap<String,String>();

        this.executorServiceToSenderMessage = Executors.newSingleThreadScheduledExecutor();
        this.communicationSenderMessage =  new NetworkClientCommunicationSenderMessage(this);
        this.isStartedSenderMessage         = Boolean.FALSE;
    }

    /*
     * initialize And Connect to Network Node
     */
    public void initializeAndConnect() {

        LOG.info("*****************************************************************");
        LOG.info("Connecting To Server: " + uri);
        LOG.info("*****************************************************************");

        ClientChannelConfigurator clientConfigurator = new ClientChannelConfigurator(this,clientIdentity);

        ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
                .configurator(clientConfigurator)
                .decoders(Arrays.<Class<? extends Decoder>>asList(PackageDecoder.class))
                .encoders(Arrays.<Class<? extends Encoder>>asList(PackageEncoder.class))
                .build();

        /*
         * Create a ReconnectHandler
         */
        ClientManager.ReconnectHandler reconnectHandler = new ClientManager.ReconnectHandler() {

            int i = 0;

            @Override
            public boolean onDisconnect(CloseReason closeReason) {
                if(nodesListPosition >= 0){
                    i++;

                    if(i > 1){

                        pluginRoot.intentToConnectToOtherNode(nodesListPosition);
                        return Boolean.FALSE;

                    }else{
                        return tryToReconnect;
                    }

                }else {
                    LOG.info("##########################################################################");
                    LOG.info("#  NetworkClientCommunicationConnection  - Disconnect -> Reconnecting... #");
                    LOG.info("##########################################################################");
                    return tryToReconnect;
                }
            }

            @Override
            public boolean onConnectFailure(Exception exception) {
                if(nodesListPosition >= 0){
                    i++;

                    if(i > 1){

                        pluginRoot.intentToConnectToOtherNode(nodesListPosition);
                        return Boolean.FALSE;

                    }else{

                        try {

                            //System.out.println("# NetworkClientCommunicationConnection - Reconnect Failure Message: "+exception.getMessage()+" Cause: "+exception.getCause());
                            // To avoid potential DDoS when you don't limit number of reconnects, wait to the next try.
                            Thread.sleep(5000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return tryToReconnect;
                    }

                }else {
                    try {

                        //System.out.println("# NetworkClientCommunicationConnection - Reconnect Failure Message: "+exception.getMessage()+" Cause: "+exception.getCause());
                        // To avoid potential DDoS when you don't limit number of reconnects, wait to the next try.
                        Thread.sleep(5000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LOG.info("###############################################################################");
                    LOG.info("#  NetworkClientCommunicationConnection  - Connect Failure -> Reconnecting... #");
                    LOG.info("###############################################################################");
                    return tryToReconnect;
                }
            }

        };

        /*
         * Register the ReconnectHandler
         */
        container.getProperties().put(ClientProperties.RECONNECT_HANDLER, reconnectHandler);

        try {

      /*      ArrayList extensions = new ArrayList();
            extensions.add(new PerMessageDeflateExtension());
            final ClientEndpointConfig clientConfiguration = ClientEndpointConfig.Builder.create().extensions(extensions).configurator(new ClientChannelConfigurator()).build();

            NewNetworkClientCommunicationChannel newNetworkClientCommunicationChannel = new NewNetworkClientCommunicationChannel(this, isExternalNode); */

            container.connectToServer(networkClientCommunicationChannel, clientConfig, uri);

        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    /*
    * initialize And Connect to Network Node
    */
    public void initializeAndConnectToExternalNode(final NetworkServiceType networkServiceType,
                                                   final ActorProfile       actorProfile      ) {

        NetworkClientCommunicationCall actorCall = new NetworkClientCommunicationCall(
                networkServiceType,
                actorProfile,
                this
        );

        this.addCall(actorCall);

        System.out.println("*****************************************************************");
        System.out.println("Connecting To Server: " + uri);
        System.out.println("*****************************************************************");

        try {

            container.asyncConnectToServer(networkClientCommunicationChannel, uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {

        try {
            if (networkClientCommunicationChannel.getClientConnection() != null)
                return networkClientCommunicationChannel.getClientConnection().isOpen();
        }catch (Exception e){
            return Boolean.FALSE;
        }

        return Boolean.FALSE;
    }

    public void setTryToReconnect(boolean tryToReconnect) {
        this.tryToReconnect = tryToReconnect;
    }

    @Override
    public boolean isRegistered() {

        return networkClientCommunicationChannel.isRegistered();
    }

    /*
     * is used to validate if it is a connection to an external node
     */
    public boolean isExternalNode() {
        return isExternalNode;
    }

    /*
     * Register the client in the Network Node
     */
    public synchronized void registerInNode(){

        // if it is not an external node, then i register it
        if (!isExternalNode) {
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setIdentityPublicKey(clientIdentity.getPublicKey());
            clientProfile.setDeviceType("");

            try {

                Location locationSource = new NetworkNodeCommunicationDeviceLocation(
                        1.1 ,
                        2.2,
                        null     ,
                        0        ,
                        null     ,
                        System.currentTimeMillis(),
                        LocationSource.UNKNOWN
                );

               clientProfile.setLocation(locationSource);

               registerProfile(clientProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // if it is an external node, i will raise the event for all the calls done to this connection.

            for (NetworkClientCall networkClientCall : activeCalls) {

                /*
                 * Create a raise a new event whit the NETWORK_CLIENT_CALL_CONNECTED
                 */

                /*
                 * Raise the event
                 */
                System.out.println("NetworkClientCommunicationConnection - Raised a event = P2pEventType.NETWORK_CLIENT_CALL_CONNECTED");

            }
        }
    }

    public void getNearbyNodes(final Location location) throws CantRegisterProfileException {

        NearNodeListMsgRequest nearNodeListMsgRequest = new NearNodeListMsgRequest(location);
        nearNodeListMsgRequest.setMessageContentType(MessageContentType.JSON);

        try {

            sendPackage(nearNodeListMsgRequest, PackageType.NEAR_NODE_LIST_REQUEST);

        } catch (CantSendPackageException cantSendPackageException) {

            CantRegisterProfileException fermatException = new CantRegisterProfileException(
                    cantSendPackageException,
                    "location:" + location,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    @Override
    public UUID registerProfile(final Profile profile) throws CantRegisterProfileException {

        CheckInProfileMsgRequest profileCheckInMsgRequest = new CheckInProfileMsgRequest(profile);
        profileCheckInMsgRequest.setMessageContentType(MessageContentType.JSON);

        PackageType packageType;

        if (profile instanceof ActorProfile) {
            packageType = PackageType.CHECK_IN_ACTOR_REQUEST;
            ((ActorProfile) profile).setClientIdentityPublicKey(clientIdentity.getPublicKey());
        }else if (profile instanceof ClientProfile) {
            packageType = PackageType.CHECK_IN_CLIENT_REQUEST;
        }else if (profile instanceof NetworkServiceProfile) {
            packageType = PackageType.CHECK_IN_NETWORK_SERVICE_REQUEST;
            ((NetworkServiceProfile) profile).setClientIdentityPublicKey(clientIdentity.getPublicKey());
        } else {
            CantRegisterProfileException fermatException = new CantRegisterProfileException(
                    "profile:" + profile,
                    "Unsupported profile type."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }

        try {

            totalOfProfileSendToCheckin++;
//            sendPackage(profileCheckInMsgRequest, packageType);
            return sendPackage(profileCheckInMsgRequest, packageType);

        } catch (CantSendPackageException cantSendPackageException) {

            CantRegisterProfileException fermatException = new CantRegisterProfileException(
                    cantSendPackageException,
                    "profile:" + profile,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    @Override
    public void updateRegisteredProfile(final Profile     profile,
                                        final UpdateTypes type   ) throws CantUpdateRegisteredProfileException {

        switch (type) {
            case FULL:
                fullUpdateRegisteredProfile(profile);
                break;
            case GEOLOCATION:
                geolocationUpdateRegisteredProfile(profile);
                break;
        }
    }

    private void geolocationUpdateRegisteredProfile(Profile profile) throws CantUpdateRegisteredProfileException {

        PackageType packageType = PackageType.UPDATE_PROFILE_GEOLOCATION_REQUEST;

        PackageContent profileUpdateMsgRequest = new UpdateProfileGeolocationMsgRequest(
                profile.getIdentityPublicKey(),
                profile.getType(),
                profile.getLocation()
        );
        profileUpdateMsgRequest.setMessageContentType(MessageContentType.JSON);

        try {

            sendPackage(profileUpdateMsgRequest, packageType);

        } catch (CantSendPackageException cantSendPackageException) {

            CantUpdateRegisteredProfileException fermatException = new CantUpdateRegisteredProfileException(
                    cantSendPackageException,
                    "profile:" + profile,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    private void fullUpdateRegisteredProfile(Profile profile) throws CantUpdateRegisteredProfileException {

        PackageContent profileUpdateMsgRequest;

        PackageType packageType;

        if (profile instanceof ActorProfile) {
            packageType = PackageType.UPDATE_ACTOR_PROFILE_REQUEST;
            ((ActorProfile) profile).setClientIdentityPublicKey(clientIdentity.getPublicKey());
            profileUpdateMsgRequest = new UpdateActorProfileMsgRequest(profile);
            profileUpdateMsgRequest.setMessageContentType(MessageContentType.JSON);

        } else {
            CantUpdateRegisteredProfileException fermatException = new CantUpdateRegisteredProfileException(
                    "profile:" + profile,
                    "Unsupported profile type."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }

        try {

            sendPackage(profileUpdateMsgRequest, packageType);

        } catch (CantSendPackageException cantSendPackageException) {

            CantUpdateRegisteredProfileException fermatException = new CantUpdateRegisteredProfileException(
                    cantSendPackageException,
                    "profile:" + profile,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    @Override
    public void unregisterProfile(final Profile profile) throws CantUnregisterProfileException {

        CheckOutProfileMsgRequest checkOutProfileMsgRequest = new CheckOutProfileMsgRequest(profile);
        checkOutProfileMsgRequest.setMessageContentType(MessageContentType.JSON);

        PackageType packageType;

        if (profile instanceof ActorProfile)
            packageType = PackageType.CHECK_OUT_ACTOR_REQUEST;
        else if (profile instanceof ClientProfile)
            packageType = PackageType.CHECK_OUT_CLIENT_REQUEST;
        else if (profile instanceof NetworkServiceProfile)
            packageType = PackageType.CHECK_OUT_NETWORK_SERVICE_REQUEST;
        else {
            CantUnregisterProfileException fermatException = new CantUnregisterProfileException(
                    "profile:" + profile,
                    "Unsupported profile type."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }


        try {

            sendPackage(checkOutProfileMsgRequest, packageType);

        } catch (CantSendPackageException cantSendPackageException) {

            CantUnregisterProfileException fermatException = new CantUnregisterProfileException(
                    cantSendPackageException,
                    "profile:" + profile,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    @Override
    public UUID onlineActorsDiscoveryQuery(final DiscoveryQueryParameters discoveryQueryParameters,
                                           final String                   networkServiceType,
                                           final String                   requesterPublicKey     ) throws CantRequestProfileListException {

        UUID queryId = null;

        ActorListMsgRequest actorListMsgRequest = new ActorListMsgRequest(
                networkServiceType,
                discoveryQueryParameters,
                requesterPublicKey
        );

        actorListMsgRequest.setMessageContentType(MessageContentType.JSON);

//        System.out.println("requesterPublicKey " + requesterPublicKey);

        try {

            queryId = sendPackage(actorListMsgRequest, PackageType.ACTOR_LIST_REQUEST);

        } catch (CantSendPackageException cantSendPackageException) {

            CantRequestProfileListException fermatException = new CantRequestProfileListException(
                    cantSendPackageException,
                    "discoveryQueryParameters:" + discoveryQueryParameters+" - networkServiceType:" + networkServiceType,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }

        return queryId;
    }

    @Override
    public void registeredProfileDiscoveryQuery(final DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestProfileListException {

        CheckInProfileDiscoveryQueryMsgRequest checkInProfileDiscoveryQueryMsgRequest = new CheckInProfileDiscoveryQueryMsgRequest(discoveryQueryParameters);
        checkInProfileDiscoveryQueryMsgRequest.setMessageContentType(MessageContentType.JSON);

        try {

            sendPackage(checkInProfileDiscoveryQueryMsgRequest, PackageType.CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST);

        } catch (CantSendPackageException cantSendPackageException) {

            CantRequestProfileListException fermatException = new CantRequestProfileListException(
                    cantSendPackageException,
                    "discoveryQueryParameters:" + discoveryQueryParameters,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    @Override
    public void actorTraceDiscoveryQuery(final DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestProfileListException {

        CheckInProfileDiscoveryQueryMsgRequest checkInProfileDiscoveryQueryMsgRequest = new CheckInProfileDiscoveryQueryMsgRequest(discoveryQueryParameters);
        checkInProfileDiscoveryQueryMsgRequest.setMessageContentType(MessageContentType.JSON);

        try {

            sendPackage(checkInProfileDiscoveryQueryMsgRequest, PackageType.ACTOR_TRACE_DISCOVERY_QUERY_REQUEST);

        } catch (CantSendPackageException cantSendPackageException) {

            CantRequestProfileListException fermatException = new CantRequestProfileListException(
                    cantSendPackageException,
                    "discoveryQueryParameters:" + discoveryQueryParameters,
                    "Cant send package."
            );

            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    fermatException
            );

            throw fermatException;
        }
    }

    public void sendPackageMessage(final PackageContent     packageContent              ,
                                   final NetworkServiceType networkServiceType          ,
                                   final String             destinationIdentityPublicKey) throws CantSendMessageException {

        LOG.info("******* IS CONNECTED: " + isConnected());

        if (isConnected()){

            try {

                LOG.info("TRYING TO SEND = " + packageContent.toJson());

                /*
                BlockPackages blockToSend = new BlockPackages();
                blockToSend.add( Package.createInstance(
                        packageContent.toJson(),
                        networkServiceType,
                        PackageType.MESSAGE_TRANSMIT,
                        clientIdentity.getPrivateKey(),
                        destinationIdentityPublicKey
                ));
                */

                networkClientCommunicationChannel.getClientConnection().getBasicRemote().sendObject(Package.createInstance(
                        packageContent.toJson(),
                        networkServiceType,
                        PackageType.MESSAGE_TRANSMIT,
                        clientIdentity.getPrivateKey(),
                        destinationIdentityPublicKey
                ));

                totalOfMessagesSents++;

            } catch (IOException | EncodeException exception){

                throw new CantSendMessageException(
                        exception,
                        "packageContent:"+packageContent,
                        "Error trying to send the message through the session."
                );

            } catch (Exception exception) {

                throw new CantSendMessageException(
                        exception,
                        "packageContent:"+packageContent,
                        "Unhandled error trying to send the message through the session."
                );
            }
        }

    }

    private UUID sendPackage(final PackageContent packageContent,
                             final PackageType    packageType   ) throws CantSendPackageException {

        if (isConnected()){

            try {

                Package packagea = Package.createInstance(
                        packageContent.toJson(),
                        NetworkServiceType.UNDEFINED,
                        packageType,
                        clientIdentity.getPrivateKey(),
                        serverIdentity
                );

//                BlockPackages blockToSend = new BlockPackages();
//                blockToSend.add(packagea);

                networkClientCommunicationChannel.getClientConnection().getAsyncRemote().sendObject(packagea);

                return packagea.getPackageId();

            } catch (Exception exception) {

                throw new CantSendPackageException(
                        exception,
                        "packageContent:"+packageContent,
                        "Unhandled error trying to send the message through the session."
                );
            }

        } else {

            raiseClientConnectionLostNotificationEvent();

            throw new CantSendPackageException(
                    "packageContent: "+packageContent+" - packageType: "+packageType,
                    "Client Connection is Closed."
            );
        }

    }

    @Override
    public List<ActorProfile> listRegisteredActorProfiles(DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestProfileListException {

        System.out.println("NetworkClientCommunicationConnection - new listRegisteredActorProfiles");
        List<ActorProfile> resultList = new ArrayList<>();

        /*
         * Validate parameter
         */
        if (discoveryQueryParameters == null){
            throw new IllegalArgumentException("The discoveryQueryParameters is required, can not be null");
        }

        HttpURLConnection conn = null;

        OutputStream os = null;
        BufferedReader reader = null;
        try {

            URL url = new URL("http://" + nodeUrl + "/fermat/rest/api/v1/profiles/actors");

            String formParameters = "client_public_key=" + URLEncoder.encode(clientIdentity.getPublicKey(), "UTF-8") + "&discovery_params=" + URLEncoder.encode(discoveryQueryParameters.toJson(), "UTF-8");

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(formParameters.length()));
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Encoding", "gzip");

            os = conn.getOutputStream();
            os.write(formParameters.getBytes());
            os.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String respond = reader.readLine();

            /*
             * if respond have the result list
             */
            if (respond.contains("data")){

                /*
                 * Decode into a json object
                 */
                JsonObject respondJsonObject = (JsonObject) GsonProvider.getJsonParser().parse(respond);

                 /*
                 * Get the receivedList
                 */
                resultList = GsonProvider.getGson().fromJson(respondJsonObject.get("data").getAsString(), new TypeToken<List<ActorProfile>>() {
                }.getType());

                System.out.println("NetworkClientCommunicationConnection - resultList.size() = " + resultList.size());

            }else {
                System.out.println("NetworkClientCommunicationConnection - Requested list is not available, resultList.size() = " + resultList.size());
            }

        }catch (Exception e){
            e.printStackTrace();
            CantRequestProfileListException cantRequestListException = new CantRequestProfileListException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
            throw cantRequestListException;

        }finally {
            if (conn != null)
                conn.disconnect();
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return resultList;
    }

    @Override
    public String getActorFullPhoto(final String publicKey) throws CantRequestActorFullPhotoException {

        String actorFullPhoto = null;
        HttpURLConnection conn = null;

        try{

            if(publicKey == null)
                throw new Exception("The publicKey must not be null");

            URL url = new URL("http://" + nodeUrl +  "/fermat/rest/api/v1/profiles/actor/photo/"+publicKey);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Encoding", "gzip");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String respond = reader.readLine();

            if (respond.contains("success")) {

                 /*
                * Decode into a json Object
                */
                JsonObject respondJsonObject = (JsonObject) GsonProvider.getJsonParser().parse(respond.trim());
                Boolean isSuccess = respondJsonObject.get("success").getAsBoolean();

                if(isSuccess) {

                    actorFullPhoto = respondJsonObject.get("photo").getAsString();
                    System.out.println("NetworkClientCommunicationConnection - Successfully get Actor Photo from " + publicKey);
                    System.out.println("NetworkClientCommunicationConnection - Actor Photo \n" + actorFullPhoto);

                }else {
                    System.out.println("NetworkClientCommunicationConnection - " + respondJsonObject.get("failure").getAsString());
                }

            }else{
                System.out.println("NetworkClientCommunicationConnection - There is a problem when call restfull get Actor Photo");
            }

        }catch (Exception e){

            e.printStackTrace();
            CantRequestActorFullPhotoException cantRequestActorFullPhotoException = new CantRequestActorFullPhotoException(e, e.getLocalizedMessage(), e.getLocalizedMessage());
            throw cantRequestActorFullPhotoException;

        }finally {
            if (conn != null)
                conn.disconnect();
        }

        return actorFullPhoto;
    }

    /**
     * Notify when the network client connection is lost.
     */
    public void raiseClientConnectionLostNotificationEvent() {

        LOG.info("CommunicationsNetworkClientConnection - raiseClientConnectionLostNotificationEvent");
        LOG.info("CommunicationsNetworkClientConnection - Raised Event = P2pEventType.NETWORK_CLIENT_CONNECTION_LOST");
    }

    @Override
    public final Boolean isActorOnline(final String publicKey) {

        try {
            URL url = new URL("http://" + nodeUrl + "/fermat/rest/api/v1/online/component/actor/" + publicKey);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String respond = reader.readLine();

            if (conn.getResponseCode() == 200 && respond != null && respond.contains("success")) {

                JsonObject respondJsonObject = (JsonObject) GsonProvider.getJsonParser().parse(respond.trim());
                return respondJsonObject.get("isOnline").getAsBoolean();

            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void callActor(final NetworkServiceProfile networkServiceProfile, final ActorProfile actorProfile) {

        try {

            ActorOnlineInformation actorOnlineInformation = ActorOnlineHelper.isActorOnlineInTheSameNode(actorProfile, nodeUrl);

            if (actorOnlineInformation.isOnline() && actorOnlineInformation.isSameNode()) {

                NetworkClientCommunicationCall actorCall = new NetworkClientCommunicationCall(
                        networkServiceProfile.getNetworkServiceType(),
                        actorProfile,
                        this
                );

                this.addCall(actorCall);

                /*
                 * Raise the event
                 */
                System.out.println("NetworkClientCommunication.callActor() - Raised a event = P2pEventType.NETWORK_CLIENT_CALL_CONNECTED");

            } else if (actorOnlineInformation.isOnline()) {
                System.out.println("***** ACTOR CALL METHOD: the actor is not in the same node");

                ActorCallMsgRequest actorCallMsgRequest = new ActorCallMsgRequest(
                        networkServiceProfile.getNetworkServiceType(),
                        actorProfile
                );

                System.out.println("***** ACTOR CALL METHOD:  SENDING ACTOR CALL REQUEST TO NODE");

                try {

                    sendPackage(actorCallMsgRequest, PackageType.ACTOR_CALL_REQUEST);

                } catch (CantSendPackageException cantSendPackageException) {

                    CantSendPackageException fermatException = new CantSendPackageException(
                            cantSendPackageException,
                            "actorCallMsgRequest:" + actorCallMsgRequest,
                            "Cant send package."
                    );

                    pluginRoot.reportError(
                            UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                            fermatException
                    );
                }
            } else {
                FermatEvent actorUnreachable = pluginRoot.getEventManager().getNewEvent(P2pEventType.NETWORK_CLIENT_ACTOR_UNREACHABLE);
                actorUnreachable.setSource(EventSource.NETWORK_CLIENT);

                ((NetworkClientActorUnreachableEvent) actorUnreachable).setActorProfile(actorProfile);
                ((NetworkClientActorUnreachableEvent) actorUnreachable).setNetworkServiceType(networkServiceProfile.getNetworkServiceType());

                    /*
                     * Raise the event
                     */
                System.out.println("ActorCallRespondProcessor - Raised a event = P2pEventType.NETWORK_CLIENT_ACTOR_UNREACHABLE");
                pluginRoot.getEventManager().raiseEvent(actorUnreachable);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public CommunicationChannels getCommunicationChannelType() {

        return CommunicationChannels.P2P_SERVERS;
    }

    /*
     * set nodesListPosition to -1 when the client is checkIn to avoid connecting to other node if this fails
     */
    public void setNodesListPosition() {
        this.nodesListPosition = -1;
    }

    public void setServerIdentity(String serverIdentity) {
        this.serverIdentity = serverIdentity;
    }

    public URI getUri() {
        return uri;
    }

    public NetworkClientCommunicationChannel getNetworkClientCommunicationChannel() {
        return networkClientCommunicationChannel;
    }

    public synchronized void addCall(NetworkClientCall networkClientCall) {

        this.activeCalls.add(networkClientCall);
    }

    public synchronized void hangUp(NetworkClientCall networkClientCall) {

        this.activeCalls.remove(networkClientCall);

        if (this.activeCalls.isEmpty() && isExternalNode) {

            NetworkClientConnectionsManager networkClientConnectionsManager =  (NetworkClientConnectionsManager) ClientContext.get(ClientContextItem.CLIENTS_CONNECTIONS_MANAGER);

            try {
                // if I can, i will close the session of the connection.
                this.networkClientCommunicationChannel.getClientConnection().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            networkClientConnectionsManager.getActiveConnectionsToExternalNodes().remove(this.nodeUrl);
        }
    }

    public ECCKeyPair getClientIdentity() {
        return clientIdentity;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public NodeProfile getNodeProfile() {
        return nodeProfile;
    }

    public void close() throws IOException {
        networkClientCommunicationChannel.getClientConnection().close();
        container.shutdown();
        executorServiceToSenderMessage.shutdown();
    }

    @Override
    public void closeConnection() {
        try {

            tryToReconnect = Boolean.FALSE;
            executorServiceToSenderMessage.shutdownNow();
            networkClientCommunicationChannel.getClientConnection().close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTotalOfProfileSendToCheckin() {
        return totalOfProfileSendToCheckin;
    }

    @Override
    public int getTotalOfProfileSuccessChecked() {
        return totalOfProfileSuccessChecked;
    }

    @Override
    public void incrementTotalOfProfileSuccessChecked() {
        totalOfProfileSuccessChecked++;
    }

    @Override
    public int getTotalOfProfileFailureToCheckin() {
        return totalOfProfileFailureToCheckin;
    }

    @Override
    public void incrementTotalOfProfileFailureToCheckin() {
        totalOfProfileFailureToCheckin++;
    }

    @Override
    public int getTotalOfMessagesSentsSuccessfully() {
        return totalOfMessagesSentsSuccessfully;
    }

    @Override
    public void incrementTotalOfMessagesSentsSuccessfully() {
        totalOfMessagesSentsSuccessfully++;
    }

    @Override
    public int getTotalOfMessagesSentsFails() {
        return totalOfMessagesSentsFails;
    }

    @Override
    public void incrementTotalOfMessagesSentsFails() {
        totalOfMessagesSentsFails++;
    }

    @Override
    public int getTotalOfMessagesSents() {
        return totalOfMessagesSents;
    }

    @Override
    public void incrementTotalOfMessagesSents() {
        totalOfMessagesSents++;
    }

    public void sendCheckinAllNetworkServices() throws CantRegisterProfileException {

        for (NetworkServiceType ns : networkServiceTypeNames) {
            NetworkServiceProfile networkServiceProfile = new NetworkServiceProfile();
            networkServiceProfile.setIdentityPublicKey(new ECCKeyPair().getPublicKey());
            networkServiceProfile.setNetworkServiceType(ns);
            this.listNetworkServiceProfileToCheckin.put(networkServiceProfile.getIdentityPublicKey(), networkServiceProfile);
            this.registerProfile(networkServiceProfile);
        }

        int identifieRandomActorChat = new Random().nextInt(1000);
        int identifieRandomActorIUS = new Random().nextInt(1000);

        ActorProfile actorProfileCHAT = new ActorProfile();
        actorProfileCHAT.setIdentityPublicKey(new ECCKeyPair().getPublicKey());
        actorProfileCHAT.setName("nameActorCHAT" + identifieRandomActorChat);
        actorProfileCHAT.setAlias("aliasActorCHAT" + identifieRandomActorChat);
        actorProfileCHAT.setPhoto(imageInByteActor);
        actorProfileCHAT.setActorType(Actors.CHAT.getCode());
        this.listActorProfileToCheckin.put(NetworkServiceType.CHAT, actorProfileCHAT);

        listPublicKeyProfiles.put(registerProfile(actorProfileCHAT).toString(), actorProfileCHAT.getIdentityPublicKey());

        ActorProfile actorProfileIUS = new ActorProfile();
        actorProfileIUS.setIdentityPublicKey(new ECCKeyPair().getPublicKey());
        actorProfileIUS.setName("nameActorIUS" + identifieRandomActorIUS);
        actorProfileIUS.setAlias("aliasActorIUS" + identifieRandomActorIUS);
        actorProfileIUS.setPhoto(imageInByteActor);
        actorProfileIUS.setActorType(Actors.INTRA_USER.getCode());
        this.listActorProfileToCheckin.put(NetworkServiceType.INTRA_USER, actorProfileIUS);

        listPublicKeyProfiles.put(registerProfile(actorProfileIUS).toString(), actorProfileIUS.getIdentityPublicKey());

    }

    public synchronized void sendApacheJMeterMessageTEST(String identityPublicKey, List<ActorProfile> listActors) throws Exception {

        NetworkServiceType networkServiceTypeIntermediate = (listNetworkServiceProfileToCheckin.containsKey(identityPublicKey)) ? listNetworkServiceProfileToCheckin.get(identityPublicKey).getNetworkServiceType() : null;

//        NetworkServiceType networkServiceTypeIntermediate = NetworkServiceType.getByCode(networkServiceTypeListReceiver);
        List<ActorProfile> listOfActorProfileRest =  listActors;
        ActorProfile actorProfileSender = (listActorProfileToCheckin.containsKey(networkServiceTypeIntermediate)) ?  listActorProfileToCheckin.get(networkServiceTypeIntermediate) : null;

        if(!isStartedSenderMessage){
            isStartedSenderMessage = Boolean.TRUE;
            executorServiceToSenderMessage.scheduleAtFixedRate(communicationSenderMessage, 20, 7, TimeUnit.SECONDS);
        }


        if (actorProfileSender != null && (listOfActorProfileRest != null && listOfActorProfileRest.size() > 0)) {


            for (ActorProfile actorProfileDestination : listOfActorProfileRest) {

                NetworkServiceMessage message = new NetworkServiceMessage();
                message.setContent(" ID_CONTENT: " + UUID.randomUUID().toString() + " TEST MESSAGESSSSSSSSSSS send to do testing with JMETER ");
                message.setNetworkServiceType(networkServiceTypeIntermediate);
                message.setSenderPublicKey(actorProfileSender.getIdentityPublicKey());
                message.setReceiverPublicKey(actorProfileDestination.getIdentityPublicKey());
                message.setShippingTimestamp(new Timestamp(System.currentTimeMillis()));
                message.setIsBetweenActors(Boolean.TRUE);
                message.setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);
                message.setMessageContentType(MessageContentType.TEXT);


                communicationSenderMessage.addNetworkServiceMessages(message);

            }



        }

//            if (actorProfileDestinationSecond != null) {
//
//                NetworkServiceMessage message = new NetworkServiceMessage();
//                message.setContent(" ID_CONTENT: " + UUID.randomUUID().toString() +" TEST MESSAGESSSSSSSSSSS send to do testing with JMETER ");
//                message.setNetworkServiceType(networkServiceTypeIntermediate);
//                message.setSenderPublicKey(actorProfileSender.getIdentityPublicKey());
//                message.setReceiverPublicKey(actorProfileDestinationSecond.getIdentityPublicKey());
//                message.setShippingTimestamp(new Timestamp(System.currentTimeMillis()));
//                message.setIsBetweenActors(Boolean.TRUE);
//                message.setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);
//                message.setMessageContentType(MessageContentType.TEXT);
//
//                NetworkClientCommunicationSenderMessage senderAgentMessageTwo = new NetworkClientCommunicationSenderMessage(
//                        this,
//                        message,
//                        networkServiceTypeIntermediate,
//                        actorProfileDestinationSecond.getIdentityPublicKey());
//
//                executorServiceToSenderMessage.scheduleAtFixedRate(senderAgentMessageTwo, 0, 5, TimeUnit.SECONDS);
//
//            }


    }

    public String getPublicKeyNSFromActorPK(String pk){

        String publicKeyNS = null;
        NetworkServiceType networkServiceTypeIntermediate = null;

        for (Map.Entry<NetworkServiceType, ActorProfile> actorProfile : this.listActorProfileToCheckin.entrySet()) {
            if (actorProfile.getValue().getIdentityPublicKey().equals(pk)) {
                networkServiceTypeIntermediate = actorProfile.getKey();
                break;
            }
        }

        if(networkServiceTypeIntermediate != null) {
            for (Map.Entry<String, NetworkServiceProfile> NS : listNetworkServiceProfileToCheckin.entrySet()) {

                if (NS.getValue().getNetworkServiceType() == networkServiceTypeIntermediate) {
                    publicKeyNS = NS.getKey();
                    break;
                }

            }
        }

        return publicKeyNS;

    }

    public NetworkServiceType getNetworkServiceTypeFromActorPK(String pk){

        NetworkServiceType networkServiceTypeIntermediate = null;

        for (Map.Entry<NetworkServiceType, ActorProfile> actorProfile : this.listActorProfileToCheckin.entrySet()) {
            if (actorProfile.getValue().getIdentityPublicKey().equals(pk)) {
                networkServiceTypeIntermediate = actorProfile.getKey();
                break;
            }
        }

        return networkServiceTypeIntermediate;

    }

    public ActorProfile getActorProfileSender(String identityPublicKey){

        ActorProfile actorProfileSender = null;

        for (Map.Entry<NetworkServiceType, ActorProfile> actorProfile : this.listActorProfileToCheckin.entrySet()) {
            if (actorProfile.getValue().getIdentityPublicKey().equals(identityPublicKey)) {
                actorProfileSender = actorProfile.getValue();
                break;
            }
        }

        return actorProfileSender;

    }

    public String getActorProfileFromUUID(UUID id){

        if(listPublicKeyProfiles.containsKey(id.toString()))
           return listPublicKeyProfiles.get(id.toString());
        else
            return null;

    }

    public String getPublickeyNetworkServicefromUUID(UUID id){

        if(listRequestListDiscovery.containsKey(id.toString()))
            return listRequestListDiscovery.get(id.toString());
        else
            return null;

    }

    public void addlistRequestListDiscovery(String uuid, String pk){
        listRequestListDiscovery.put(uuid,pk);
    }


}

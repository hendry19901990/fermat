package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorCallRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorListRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorTraceDiscoveryQueryRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.AddActorIntoCatalogProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInActorRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInClientRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInNetworkServiceRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInProfileDiscoveryQueryRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutActorRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutClientRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutNetworkServiceRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.MessageTransmitProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.NearNodeListRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.UpdateActorProfileIntoCatalogProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.UpdateProfileLocationIntoCatalogProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.ActorCatalogToPropagateRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.AddNodeToCatalogRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.GetActorCatalogRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.GetNodeCatalogRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.NodesCatalogToAddOrUpdateRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.NodesCatalogToPropagateRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.UpdateNodeInCatalogRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.ActorCatalogToPropagateResponseProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.AddNodeToCatalogResponseProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.GetActorCatalogResponseProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.GetNodeCatalogResponseProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.NodesCatalogToPropagateResponseProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.UpdateNodeInCatalogResponseProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rrequena on 15/07/16.
 */
public class PackageProcessorFactory {

    /**
     * Represent the list of package Processors for the FermatWebSocketClientChannelServerEndpoint
     */
    private Map<PackageType, List<PackageProcessor>> packagesProcessorsFermatWebSocketClientChannelServerEndpoint;

    /**
     * Represent the list of package Processors for the FermatWebSocketNodeChannelServerEndpoint
     */
    private Map<PackageType, List<PackageProcessor>> packagesProcessorsFermatWebSocketNodeChannelServerEndpoint;

    /**
     * Represent the list of package Processors for the FermatWebSocketClientNodeChannel
     */
    private Map<PackageType, List<PackageProcessor>> packagesProcessorsFermatWebSocketClientNodeChannel;

    /**
     * Represent the actual instance
     */
    private static PackageProcessorFactory instance = new PackageProcessorFactory();

    /**
     * Constructor
     */
    private PackageProcessorFactory() {
        super();
        packagesProcessorsFermatWebSocketClientChannelServerEndpoint = new ConcurrentHashMap<>();
        packagesProcessorsFermatWebSocketNodeChannelServerEndpoint = new ConcurrentHashMap<>();
        packagesProcessorsFermatWebSocketClientNodeChannel = new ConcurrentHashMap<>();

        initPackageProcessorsRegistrationFermatWebSocketClientChannelServerEndpoint();
        initPackageProcessorsRegistrationFermatWebSocketNodeChannelServerEndpoint();
        initPackageProcessorsRegistrationFermatWebSocketClientNodeChannel();
    }

    /**
     * Get the actual instance
     * @return PackageProcessorFactory
     */
    public static PackageProcessorFactory getInstance() {
        return instance;
    }

    /**
     * This method register a PackageProcessor object with this
     * channel
     */
    public void registerMessageProcessor( Map<PackageType, List<PackageProcessor>> packageProcessors,PackageProcessor packageProcessor) {

        /*
         * Set server reference
         */

        //Validate if a previous list created
        if (packageProcessors.containsKey(packageProcessor.getPackageType())){

            /*
             * Add to the existing list
             */
            packageProcessors.get(packageProcessor.getPackageType()).add(packageProcessor);

        }else{

            /*
             * Create a new list
             */
            List<PackageProcessor> packageProcessorList = Collections.synchronizedList(new ArrayList());
            packageProcessorList.add(packageProcessor);

            /*
             * Add to the packageProcessor
             */
            packageProcessors.put(packageProcessor.getPackageType(), packageProcessorList);
        }

    }

    /**
     * Initialize all package Processors for the FermatWebSocketClientChannelServerEndpoint
     */
    private void initPackageProcessorsRegistrationFermatWebSocketClientChannelServerEndpoint(){

        /*
         * Register all messages processor for this
         * channel
         */
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new ActorCallRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new ActorListRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new ActorTraceDiscoveryQueryRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new AddActorIntoCatalogProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckInActorRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckInClientRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckInNetworkServiceRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckInProfileDiscoveryQueryRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckOutActorRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckOutClientRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new CheckOutNetworkServiceRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new MessageTransmitProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new NearNodeListRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new UpdateActorProfileIntoCatalogProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientChannelServerEndpoint, new UpdateProfileLocationIntoCatalogProcessor());

    }

    /**
     * Initialize all package Processors for the FermatWebSocketNodeChannelServerEndpoint
     */
    private void initPackageProcessorsRegistrationFermatWebSocketNodeChannelServerEndpoint(){

         /*
         * Register all messages processor for this
         * channel
         */
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new AddNodeToCatalogRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new ActorCatalogToPropagateRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new GetActorCatalogRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new GetNodeCatalogRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new NodesCatalogToAddOrUpdateRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new NodesCatalogToPropagateRequestProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketNodeChannelServerEndpoint, new UpdateNodeInCatalogRequestProcessor());

    }

    /**
     * Initialize all package Processors for the FermatWebSocketClientNodeChannel
     */
    private void initPackageProcessorsRegistrationFermatWebSocketClientNodeChannel(){


        /*
         * Register all messages processor for this
         * channel
         */
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientNodeChannel, new AddNodeToCatalogResponseProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientNodeChannel, new ActorCatalogToPropagateResponseProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientNodeChannel, new GetActorCatalogResponseProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientNodeChannel, new GetNodeCatalogResponseProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientNodeChannel, new NodesCatalogToPropagateResponseProcessor());
        registerMessageProcessor(packagesProcessorsFermatWebSocketClientNodeChannel, new UpdateNodeInCatalogResponseProcessor());
    }

    /**
     * Get the Packages Processors for FermatWebSocketClientChannelServerEndpoint
     * @return Map<PackageType, List<PackageProcessor>>
     */
    public static Map<PackageType, List<PackageProcessor>> getPackagesProcessorsFermatWebSocketClientChannelServerEndpoint() {
        return instance.packagesProcessorsFermatWebSocketClientChannelServerEndpoint;
    }

    /**
     * Get the Packages Processors for FermatWebSocketNodeChannelServerEndpoint
     * @return Map<PackageType, List<PackageProcessor>>
     */
    public static Map<PackageType, List<PackageProcessor>> getPackagesProcessorsFermatWebSocketNodeChannelServerEndpoint() {
        return instance.packagesProcessorsFermatWebSocketNodeChannelServerEndpoint;
    }

    /**
     * Get the Packages Processors for FermatWebSocketClientNodeChannel
     * @return Map<PackageType, List<PackageProcessor>>
     */
    public  static Map<PackageType, List<PackageProcessor>> getPackagesProcessorsFermatWebSocketClientNodeChannel() {
        return instance.packagesProcessorsFermatWebSocketClientNodeChannel;
    }
}

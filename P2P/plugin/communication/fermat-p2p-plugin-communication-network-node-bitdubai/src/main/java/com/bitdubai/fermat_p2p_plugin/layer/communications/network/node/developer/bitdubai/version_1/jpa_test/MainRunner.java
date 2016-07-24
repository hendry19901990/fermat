package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ClientCheckInDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NetworkServiceCheckInDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NodeCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rrequena on 21/07/16.
 */

public class MainRunner {

    public static void main(String[] args) {

        try {

            Stopwatch timer = Stopwatch.createStarted();

            NodeCatalog nodeCatalog = testNodeCatalog();

            ClientCheckIn clientCheckIn = testClientCheckIn();

            NetworkServiceCheckIn networkServiceCheckIn = testNetworkServiceCheckIn(clientCheckIn);

            DatabaseManager.closeDataBase();

            System.out.println("TOTAL TEST TOOK: " + timer.stop());


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static NodeCatalog testNodeCatalog() throws CantReadRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testNodeCatalog()");

        Stopwatch timer = Stopwatch.createStarted();
        List<NodeCatalog> list = new ArrayList<>();
        NodeCatalogDao dao = new NodeCatalogDao();

        ECCKeyPair eccKeyPair = null;

        for (int i = 0; i < 100; i++) {

            eccKeyPair = new ECCKeyPair();
            NodeCatalog nodeCatalog = new NodeCatalog();
            nodeCatalog.setId(eccKeyPair.getPublicKey());
            nodeCatalog.setDefaultPort(8080);
            nodeCatalog.setName("Node_" + i);
            nodeCatalog.setIp("10.1.1." + i);
            nodeCatalog.setStatus(ProfileStatus.OFFLINE);
            nodeCatalog.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));

            list.add(nodeCatalog);

        }

        for (NodeCatalog item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + eccKeyPair.getPublicKey());
        System.out.println("Total entities: " + dao.count());

        NodeCatalog entity = dao.findById(eccKeyPair.getPublicKey());

        System.out.println("Load entity:" +entity);
        System.out.println("Method testNodeCatalog() took: " + timer.stop());

        return entity;

    }


    public static ClientCheckIn testClientCheckIn() throws CantReadRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testNodeCatalog()");

        Stopwatch timer = Stopwatch.createStarted();
        List<ClientCheckIn> list = new ArrayList<>();
        ClientCheckInDao dao = new ClientCheckInDao();

        String id = null;

        for (int i = 0; i < 100; i++) {

            id = UUID.randomUUID().toString();
            Client profile = new Client();
            profile.setDeviceType("device " + i);
            profile.setId(new ECCKeyPair().getPublicKey());
            profile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.ONLINE);

            ClientCheckIn clientCheckIn = new ClientCheckIn();
            clientCheckIn.setClient(profile);
            clientCheckIn.setId(id);

            list.add(clientCheckIn);

        }

        for (ClientCheckIn item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + id);
        System.out.println("Total entities: " + dao.count());

        ClientCheckIn entity = dao.findById(id);
        System.out.println("Load entity:" +entity);
        System.out.println("Method testClientCheckIn() took: " + timer.stop());

        return entity;

    }


    public static NetworkServiceCheckIn testNetworkServiceCheckIn(ClientCheckIn clientCheckIn) throws CantReadRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testNodeCatalog()");

        Stopwatch timer = Stopwatch.createStarted();
        List<NetworkServiceCheckIn> list = new ArrayList<>();
        NetworkServiceCheckInDao dao = new NetworkServiceCheckInDao();

        String id = null;

        for (int i = 0; i < 100; i++) {

            id = UUID.randomUUID().toString();
            NetworkServiceProfile profile = new NetworkServiceProfile();
            profile.setIdentityPublicKey(new ECCKeyPair().getPublicKey());
            profile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.ONLINE);
            profile.setClientIdentityPublicKey(clientCheckIn.getClient().getId());
            profile.setNetworkServiceType(NetworkServiceType.NEGOTIATION_TRANSMISSION);

            NetworkService networkService = new NetworkService(profile);

            NetworkServiceCheckIn networkServiceCheckIn = new NetworkServiceCheckIn();
            networkServiceCheckIn.setId(id);
            networkServiceCheckIn.setNetworkService(networkService);

            list.add(networkServiceCheckIn);

        }

        for (NetworkServiceCheckIn item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + id);
        System.out.println("Total entities: " + dao.count());

        NetworkServiceCheckIn entity = dao.findById(id);
        System.out.println("Load entity:" +entity);
        System.out.println("Method testClientCheckIn() took: " + timer.stop());

        return entity;

    }
}

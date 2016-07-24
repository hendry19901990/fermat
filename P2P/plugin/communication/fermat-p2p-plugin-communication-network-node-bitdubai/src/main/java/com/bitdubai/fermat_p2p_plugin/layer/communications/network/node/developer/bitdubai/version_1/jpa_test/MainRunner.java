package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ClientCheckInDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NodeCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
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


            testNodeCatalog();

            testClientCheckIn();

            DatabaseManager.closeDataBase();


            ClientProfile clientProfile = new ClientProfile();

            NetworkServiceProfile networkService = new NetworkServiceProfile();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void testNodeCatalog() throws CantReadRecordDataBaseException {

        Stopwatch timer = Stopwatch.createStarted();
        List<NodeCatalog> nodeCatalogs = new ArrayList<>();
        NodeCatalogDao nodeCatalogDao = new NodeCatalogDao();

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

            nodeCatalogs.add(nodeCatalog);

        }

        for (NodeCatalog nodeCatalog: nodeCatalogs) {
            nodeCatalogDao.save(nodeCatalog);
        }

        System.out.println("Total nodeCatalog: " + nodeCatalogDao.count());

        System.out.println("Load entity:" +nodeCatalogDao.findById(eccKeyPair.getPublicKey()));

        // Retrieve all the Point objects from the database:
        List<NodeCatalog> results = nodeCatalogDao.list();
        for (NodeCatalog nodeCatalog : results) {
            System.out.println(nodeCatalog);
        }

        System.out.println("Method testNodeCatalog() took: " + timer.stop());

    }


    public static void testClientCheckIn() throws CantReadRecordDataBaseException {

        Stopwatch timer = Stopwatch.createStarted();
        List<ClientCheckIn> clientCheckInList = new ArrayList<>();
        ClientCheckInDao clientCheckInDao = new ClientCheckInDao();

        ECCKeyPair eccKeyPair = null;

        for (int i = 0; i < 100; i++) {

            eccKeyPair = new ECCKeyPair();

            Client clientProfile = new Client();
            clientProfile.setDeviceType("device " + i);
            clientProfile.setId(eccKeyPair.getPublicKey());
            clientProfile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            clientProfile.setStatus(ProfileStatus.ONLINE);

            ClientCheckIn clientCheckIn = new ClientCheckIn();
            clientCheckIn.setClientProfile(clientProfile);
            clientCheckIn.setId(UUID.randomUUID().toString());

            clientCheckInList.add(clientCheckIn);

        }

        for (ClientCheckIn clientCheckIn: clientCheckInList) {
            clientCheckInDao.save(clientCheckIn);
        }

        System.out.println("Total nodeCatalog: " + clientCheckInDao.count());

        System.out.println("Load entity:" +clientCheckInDao.findById(eccKeyPair.getPublicKey()));

        // Retrieve all the Point objects from the database:
        List<ClientCheckIn> results = clientCheckInDao.list();
        for (ClientCheckIn checkIn : results) {
            System.out.println(checkIn);
        }

        System.out.println("Method testClientCheckIn() took: " + timer.stop());

    }
}

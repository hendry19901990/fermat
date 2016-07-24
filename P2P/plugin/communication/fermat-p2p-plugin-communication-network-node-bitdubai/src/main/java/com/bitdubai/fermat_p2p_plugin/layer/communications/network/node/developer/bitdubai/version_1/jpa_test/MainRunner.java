package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
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

            Stopwatch timer = Stopwatch.createStarted();

            NodeCatalog nodeCatalog = testNodeCatalog();

            ClientCheckIn clientCheckIn = testClientCheckIn();

            DatabaseManager.closeDataBase();

            System.out.println("TOTAL TEST TOOK: " + timer.stop());


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static NodeCatalog testNodeCatalog() throws CantReadRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");

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

        System.out.println("Last id: " + eccKeyPair.getPublicKey());
        System.out.println("Total nodeCatalog: " + nodeCatalogDao.count());

        NodeCatalog nodeCatalog = nodeCatalogDao.findById(eccKeyPair.getPublicKey());

        System.out.println("Load entity:" +nodeCatalog);
        System.out.println("Method testNodeCatalog() took: " + timer.stop());

        return nodeCatalog;

    }


    public static ClientCheckIn testClientCheckIn() throws CantReadRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");

        Stopwatch timer = Stopwatch.createStarted();
        List<ClientCheckIn> clientCheckInList = new ArrayList<>();
        ClientCheckInDao clientCheckInDao = new ClientCheckInDao();

        String id = null;

        for (int i = 0; i < 100; i++) {

            id = UUID.randomUUID().toString();
            Client clientProfile = new Client();
            clientProfile.setDeviceType("device " + i);
            clientProfile.setId(new ECCKeyPair().getPublicKey());
            clientProfile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            clientProfile.setStatus(ProfileStatus.ONLINE);

            ClientCheckIn clientCheckIn = new ClientCheckIn();
            clientCheckIn.setClient(clientProfile);
            clientCheckIn.setId(id);

            clientCheckInList.add(clientCheckIn);

        }

        for (ClientCheckIn clientCheckIn: clientCheckInList) {
            clientCheckInDao.save(clientCheckIn);
        }

        System.out.println("Last id: " + id);
        System.out.println("Total ClientCheckIn: " + clientCheckInDao.count());

        ClientCheckIn clientCheckIn = clientCheckInDao.findById(id);
        System.out.println("Load entity:" +clientCheckIn);
        System.out.println("Method testClientCheckIn() took: " + timer.stop());

        return clientCheckIn;

    }
}

package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.location_system.NetworkNodeCommunicationDeviceLocation;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NodeCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.google.common.base.Stopwatch;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrequena on 21/07/16.
 */

public class MainRunner {

    public static void main(String[] args) {

        try {


            testNodeCatalog();



            DatabaseManager.closeDataBase();

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
            NodeProfile nodeProfile = new NodeProfile();
            nodeProfile.setIdentityPublicKey(eccKeyPair.getPublicKey());
            nodeProfile.setDefaultPort(8080);
            nodeProfile.setName("Node_" + i);
            nodeProfile.setIp("10.1.1." + i);
            nodeProfile.setStatus(ProfileStatus.OFFLINE);
            nodeProfile.setLocation(NetworkNodeCommunicationDeviceLocation.getInstance((10.1 + i), (8.9 + i)));

            nodeCatalogs.add(new NodeCatalog(nodeProfile));

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
}

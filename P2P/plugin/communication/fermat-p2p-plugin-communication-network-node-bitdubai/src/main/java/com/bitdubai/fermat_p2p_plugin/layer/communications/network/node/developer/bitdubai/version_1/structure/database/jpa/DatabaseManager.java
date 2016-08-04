/*
 * @#DatabaseManager.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ProviderResourcesFilesPath;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager</code> are the
 * database manager class
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class DatabaseManager {

    /**
     * Represent the value of DIR_NAME
     */
    public static final String DIR_NAME = "database";

    /**
     * Represent the value of DIR_NAME
     */
    public static final String DATA_BASE_NAME = "network_node.odb";

    /**
     * Represent the entityManagerFactory instance
     */
    private static final EntityManagerFactory entityManagerFactory;

    static {

        /*
         * Configure the database properties
         * TODO: GET THIS VALUES FROM CONFIGURATION FILE
         */
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.user", "admin");
        properties.put("javax.persistence.jdbc.password", "admin");

        // Open a database connection (create a new database if it doesn't exist yet):
        entityManagerFactory = Persistence.createEntityManagerFactory(ProviderResourcesFilesPath.createNewFilesPath(DIR_NAME).concat(DATA_BASE_NAME), properties);

    }


    /**
     * Get a new instance to a Entity manager that
     * represent a connection with the data base.
     *
     * @return EntityManager
     */
    public static EntityManager getConnection() {

        if (entityManagerFactory != null){
            return entityManagerFactory.createEntityManager();
        }else {
            throw new RuntimeException("Cant get Connection, entityManagerFactory = "+ entityManagerFactory);
        }

    }

    /**
     * Close de data base
     */
    public static void closeDataBase(){

        if (entityManagerFactory != null && entityManagerFactory.isOpen()){
            entityManagerFactory.close();
        }
    }

    /**
     * Get the EntityManagerFactory value
     *
     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}

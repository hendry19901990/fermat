/*
 * @#ClientDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ClientDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientDao extends AbstractBaseDao<Client>{

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ClientDao.class));

    /**
     * Constructor
     */
    public ClientDao(){
        super(Client.class);
    }

    /**
     * Save the entity into the data base, verify is exist; if exist make a update
     * if no make a persist
     *
     * @param entity
     * @throws CantReadRecordDataBaseException
     */
    public void save(Client entity) throws
            CantReadRecordDataBaseException,
            CantUpdateRecordDataBaseException,
            CantInsertRecordDataBaseException {


        LOG.debug("Executing save("+entity+")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            if ((entity.getId() != null) &&
                    (exist(connection, entity.getId()))){

                try {

                    transaction.begin();
                    connection.merge(entity);
                    transaction.commit();
                    connection.flush();

                } catch (Exception e){
                    LOG.error(e);
                    transaction.rollback();
                    throw new CantUpdateRecordDataBaseException(CantUpdateRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
                }

            }else {

                try {

                    transaction.begin();

                    if(JPADaoFactory.getGeoLocationDao().exist(connection, entity.getId())){
                        deleteGeolocation(entity.getId());
                    }

                    connection.persist(entity);
                    connection.flush();
                    transaction.commit();

                }catch (Exception e){
                    LOG.error(e);
                    transaction.rollback();
                    throw new CantInsertRecordDataBaseException(CantInsertRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
                }

            }

        }finally {
            connection.close();
        }

    }

    /**
     * This method checks if exists a record in Geolocation table
     * @param clientId
     * @return
     * @throws CantReadRecordDataBaseException
     */
    private boolean existsGeoLocation(String clientId) throws CantReadRecordDataBaseException {
        return JPADaoFactory.getGeoLocationDao().exist(clientId);
    }

    /**
     * This method deletes a geolocation record by client id
     * @param clientId
     * @throws CantReadRecordDataBaseException
     * @throws CantDeleteRecordDataBaseException
     */
    private void deleteGeolocation(String clientId) throws CantReadRecordDataBaseException, CantDeleteRecordDataBaseException {
        GeoLocation geoLocation = JPADaoFactory.getGeoLocationDao().findById(clientId);
        JPADaoFactory.getGeoLocationDao().delete(geoLocation);
    }
}

/*
 * @#NetworkServiceDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.NetworkServiceDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkServiceDao extends AbstractBaseDao<NetworkService> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkServiceDao.class));

    /**
     * Constructor
     */
    public NetworkServiceDao(){
        super(NetworkService.class);
    }


    /**
     * Save the entity into the data base, verify is exist; if exist make a update
     * if no make a persist
     *
     * @param entity
     * @throws CantReadRecordDataBaseException
     */
    public void save(NetworkService entity) throws
            CantReadRecordDataBaseException,
            CantUpdateRecordDataBaseException,
            CantInsertRecordDataBaseException {

        LOG.debug("Executing save("+entity+")");
        EntityManager connection = getConnection();
        try {

            if ((entity.getId() != null) &&
                    (exist(entity.getId()))){
                update(entity);
            }else {
                if(existsGeoLocation(entity.getId())){
                    deleteGeolocation(entity.getId());
                }
                persist(entity);
            }

        } catch (CantDeleteRecordDataBaseException e) {
            LOG.error(e);
            throw new CantInsertRecordDataBaseException(
                    e,
                    "Persisting new client",
                    "Cannot delete a record");
        } finally {
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

/*
 * @#NetworkServiceCheckInDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.NetworkServiceCheckInDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceCheckIn</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkServiceCheckInDao extends AbstractBaseDao<NetworkServiceCheckIn> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkServiceCheckInDao.class));

    /**
     * Constructor
     */
    public NetworkServiceCheckInDao(){
        super(NetworkServiceCheckIn.class);
    }


    /**
     * Check in a Network Service and associate with the session
     *
     * @param session
     * @param networkServiceProfile
     */
    public void checkIn(Session session, NetworkServiceProfile networkServiceProfile, Client client) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        LOG.info("Executing checkIn(" + session.getId() + ", " + networkServiceProfile.getIdentityPublicKey() + ")");
        LOG.info("type = " + networkServiceProfile.getNetworkServiceType() + ")");


        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();
            NetworkServiceCheckIn networkServiceCheckIn;

            Map<String, Object> filters = new HashMap<>();
            filters.put("sessionId", session.getId());
            filters.put("networkService.id", networkServiceProfile.getIdentityPublicKey());
            filters.put("networkService.client.id", networkServiceProfile.getClientIdentityPublicKey());
            List<NetworkServiceCheckIn> list = list(filters);

            if ((list != null) && (!list.isEmpty())){
                networkServiceCheckIn = list.get(0);
                networkServiceCheckIn.setNetworkService(new NetworkService(networkServiceProfile));
                connection.merge(networkServiceCheckIn);
            }else {
                networkServiceCheckIn = new NetworkServiceCheckIn(session, networkServiceProfile);
                connection.persist(networkServiceCheckIn);
            }

            ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(networkServiceProfile.getIdentityPublicKey(), client.getDeviceType(), ProfileTypes.NETWORK_SERVICE, RegistrationType.CHECK_IN, RegistrationResult.SUCCESS, "");
            connection.persist(profileRegistrationHistory);

            transaction.commit();

        }catch (Exception e){
            LOG.error(e);
            transaction.rollback();
            throw new CantInsertRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Check out all Network Service associate with the session and client
     *
     * @param session
     * @param client
     * @throws CantDeleteRecordDataBaseException
     */
    public void checkOut(Session session, Client client) throws CantDeleteRecordDataBaseException {

        LOG.debug("Executing checkOut("+session.getId()+")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            Map<String, Object> filters = new HashMap<>();
            filters.put("sessionId", session.getId());
            filters.put("networkService.client.id", client.getId());
            List<NetworkServiceCheckIn> list = list(filters);

            if ((list != null) && (!list.isEmpty())){
                for (NetworkServiceCheckIn networkServiceCheckIn: list) {
                    connection.remove(connection.contains(networkServiceCheckIn) ? networkServiceCheckIn : connection.merge(networkServiceCheckIn));
                    ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(networkServiceCheckIn.getNetworkService().getId(), networkServiceCheckIn.getNetworkService().getClient().getDeviceType(), ProfileTypes.NETWORK_SERVICE, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "");
                    connection.persist(profileRegistrationHistory);
                }
            }

            transaction.commit();

        }catch (Exception e){
            LOG.error(e);
            transaction.rollback();
            throw new CantDeleteRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Check out a specific Network Service associate with the session
     *
     * @param session
     * @param networkServiceProfile
     * @throws CantDeleteRecordDataBaseException
     */
    public void checkOut(Session session, NetworkServiceProfile networkServiceProfile) throws CantDeleteRecordDataBaseException {

        LOG.info("Executing checkOut(" + session.getId() + ", "+ networkServiceProfile.getClientIdentityPublicKey() +")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                Map<String, Object> filters = new HashMap<>();
                filters.put("sessionId", session.getId());
                filters.put("networkService.id",networkServiceProfile.getIdentityPublicKey());
                filters.put("networkService.client.id", networkServiceProfile.getClientIdentityPublicKey());
                List<NetworkServiceCheckIn> list = list(filters);

                if ((list != null) && (!list.isEmpty())){
                    connection.remove(connection.contains(list.get(0)) ? list.get(0) : connection.merge(list.get(0)));
                    ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(list.get(0).getNetworkService().getId(), list.get(0).getNetworkService().getClient().getDeviceType(), ProfileTypes.NETWORK_SERVICE, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "");
                    connection.persist(profileRegistrationHistory);
                }

            transaction.commit();

        }catch (Exception e){
            LOG.error(e);
            transaction.rollback();
            throw new CantDeleteRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }
}

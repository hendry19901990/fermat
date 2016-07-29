/*
 * @#ClientCheckInDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ClientSessionDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientSessionDao extends AbstractBaseDao<ClientSession>{

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ClientSessionDao.class));

    /**
     * Constructor
     */
    public ClientSessionDao(){
        super(ClientSession.class);
    }

    /**
     * Check in a client and associate with the session
     *
     * @param session
     * @param clientProfile
     */
    public void checkIn(Session session, ClientProfile clientProfile) throws CantInsertRecordDataBaseException {

        LOG.info("Executing checkIn(" + session.getId() + ", " + clientProfile.getIdentityPublicKey() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                Client client = new Client(clientProfile);
                ClientSession clientSession = new ClientSession(session, client);

                /*
                 * Verify is exist the current session for the same client
                 */
                if (exist(session.getId())){
                    connection.merge(clientSession);
                }else {
                    connection.persist(clientSession);
                }

                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(clientProfile.getIdentityPublicKey(), clientProfile.getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_IN, RegistrationResult.SUCCESS, "");
                connection.persist(profileRegistrationHistory);

            transaction.commit();

        }catch (Exception e){
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantInsertRecordDataBaseException(CantInsertRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Check out a client associate with the session, and check out all network services and
     * actors associate with this session too
     *
     * @param session
     */
    public void checkOut(Session session) throws CantDeleteRecordDataBaseException {

        LOG.debug("Executing checkOut("+session.getId()+")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            ClientSession clientSession = findById(session.getId());

            if (clientSession != null){

                transaction.begin();

                Query queryActorSessionDelete = connection.createQuery("DELETE FROM ActorSession a WHERE a.actor.client.id = :clientId AND a.sessionId = :sessionId");
                queryActorSessionDelete.setParameter("sessionId", session.getId());
                queryActorSessionDelete.setParameter("clientId", clientSession.getClient().getId());
                int deletedActors = queryActorSessionDelete.executeUpdate();

                LOG.info("deleted Actor Sessions = "+deletedActors);

                Map<String, Object> filters = new HashMap<>();
                filters.put("sessionId", session.getId());
                filters.put("networkService.client.id", clientSession.getClient().getId());

                List<NetworkServiceSession> nsList = JPADaoFactory.getNetworkServiceSessionDao().list(filters);

                LOG.info("deleted Ns Sessions  = "+(nsList != null ? nsList.size() : null));

                for (NetworkServiceSession networkServiceSession: nsList) {
                    connection.remove(connection.contains(networkServiceSession) ? networkServiceSession : connection.merge(networkServiceSession));
                }

                connection.remove(connection.contains(clientSession) ? clientSession : connection.merge(clientSession));

                LOG.info("deleted Client Sessions  1");

                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(clientSession.getClient().getId(), clientSession.getClient().getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "Delete all network service and actor session associate with this client");
                connection.persist(profileRegistrationHistory);

                transaction.commit();
                connection.flush();
            }

        }catch (Exception e){
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantDeleteRecordDataBaseException(CantDeleteRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     *  Delete all previous or old session for this client profile
     *
     * @param clientProfile
     * @throws CantDeleteRecordDataBaseException
     */
    public void deleteAll(ClientProfile clientProfile) throws CantDeleteRecordDataBaseException {

        LOG.info("Executing deleteAll(" + clientProfile.getIdentityPublicKey() +")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                /*
                 * Find previous or old session for the same client, if
                 * exist delete, but not delete the client record
                 */
                Query querySessionDelete = connection.createQuery("DELETE FROM ClientSession s WHERE s.client.id = :id");
                querySessionDelete.setParameter("id", clientProfile.getIdentityPublicKey());
                int deletedSessions = querySessionDelete.executeUpdate();

            transaction.commit();

            LOG.info("deleted oldSession ="+deletedSessions);

        }catch (Exception e){
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantDeleteRecordDataBaseException(CantDeleteRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

    /**
     * This method deletes all the client checked
     * @throws CantDeleteRecordDataBaseException
     */
    public void deleteAll() throws CantDeleteRecordDataBaseException {

        LOG.info("Executing deleteAll()");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

                /*
                 * Delete previous or old session
                 */
                Query querySessionDelete = connection.createQuery("DELETE FROM ClientSession");
                int deletedSessions = querySessionDelete.executeUpdate();

            transaction.commit();

            LOG.info("deleted oldSession ="+deletedSessions);

        }catch (Exception e){
            LOG.error(e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantDeleteRecordDataBaseException(CantDeleteRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }
    }

}

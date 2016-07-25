/*
 * @#ActorCheckInDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientCheckIn;
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
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ActorCheckInDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCheckIn</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ActorCheckInDao extends AbstractBaseDao<ActorCheckIn> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorCheckInDao.class));

    /**
     * Constructor
     */
    public ActorCheckInDao(){
        super(ActorCheckIn.class);
    }


    /**
     * Check in a actor and associate with the session
     *
     * @param session
     * @param actorCatalog
     */
    public void checkIn(Session session, ActorCatalog actorCatalog) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        LOG.debug("Executing checkIn(" + session.getId() + ", " + actorCatalog.getId() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            ActorCheckIn actorCheckIn;
            Map<String, Object> filters = new HashMap<>();
            filters.put("sessionId", session.getId());
            filters.put("actor.id", actorCatalog.getId());
            filters.put("actor.client.id", actorCatalog.getClient().getId());
            List<ActorCheckIn> list = list(filters);

            if ((list != null) && (!list.isEmpty())){
                actorCheckIn = list.get(0);
                actorCheckIn.setActor(actorCatalog);
                connection.merge(actorCheckIn);
            }else {
                actorCheckIn = new ActorCheckIn(session, actorCatalog);
                connection.persist(actorCheckIn);
            }

            ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(actorCheckIn.getId().toString(), actorCatalog.getClient().getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_IN, RegistrationResult.SUCCESS, "");
            connection.persist(profileRegistrationHistory);

            transaction.commit();

        }catch (Exception e){
            transaction.rollback();
            throw new CantInsertRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Check out all actor associate with the session
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
            filters.put("actor.client.id", client.getId());
            List<ActorCheckIn> list = list(filters);

            if ((list != null) && (!list.isEmpty())){
                for (ActorCheckIn actorCheckIn: list) {
                    connection.remove(actorCheckIn);
                    ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(actorCheckIn.getActor().getId(), actorCheckIn.getActor().getClient().getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "");
                    connection.persist(profileRegistrationHistory);
                }
            }

            transaction.commit();

        }catch (Exception e){
            transaction.rollback();
            throw new CantDeleteRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Check out a specific actor associate with the session
     *
     * @param session
     * @param actorProfile
     * @throws CantDeleteRecordDataBaseException
     */
    public void checkOut(Session session, ActorProfile actorProfile) throws CantDeleteRecordDataBaseException {

        LOG.debug("Executing checkOut(" + session.getId() + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            Map<String, Object> filters = new HashMap<>();
            filters.put("sessionId", session.getId());
            filters.put("actor.id",actorProfile.getIdentityPublicKey());
            filters.put("actor.client.id", actorProfile.getClientIdentityPublicKey());
            List<ActorCheckIn> list = list(filters);

            if ((list != null) && (!list.isEmpty())){

                connection.remove(list.get(0));
                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(list.get(0).getActor().getId(), list.get(0).getActor().getClient().getDeviceType(), ProfileTypes.CLIENT, RegistrationType.CHECK_OUT, RegistrationResult.SUCCESS, "");
                connection.persist(profileRegistrationHistory);
            }

            transaction.commit();

        }catch (Exception e){
            transaction.rollback();
            throw new CantDeleteRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }
}

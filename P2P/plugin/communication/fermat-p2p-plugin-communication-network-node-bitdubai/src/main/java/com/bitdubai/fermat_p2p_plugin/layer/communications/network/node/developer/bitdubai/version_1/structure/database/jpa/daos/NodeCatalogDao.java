/*
 * @#NodeCatalogDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.NodeCatalogDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NodeCatalogDao extends AbstractBaseDao<NodeCatalog> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NodeCatalogDao.class));

    /**
     * Constructor
     */
    public NodeCatalogDao() {
        super(NodeCatalog.class);
    }


    public final void increaseLateNotificationCounter(final String id,
                                                      final Integer quantity) throws CantUpdateRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        LOG.debug("Executing increaseLateNotificationCounter quantity (" + quantity + ")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            NodeCatalog entity = connection.find(NodeCatalog.class, id);
            entity.setLateNotificationsCounter(quantity);

            transaction.begin();
            connection.merge(entity);
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw new CantUpdateRecordDataBaseException(CantUpdateRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    public final void decreasePendingPropagationsCounter(final String id) throws CantUpdateRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        LOG.debug("Executing decreasePendingPropagationsCounter");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            NodeCatalog entity = connection.find(NodeCatalog.class, id);
            Integer previousPendingPropagationsValue = entity.getLateNotificationsCounter();
            entity.setLateNotificationsCounter(previousPendingPropagationsValue - 1);

            transaction.begin();
            connection.merge(entity);
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw new CantUpdateRecordDataBaseException(CantUpdateRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }


}

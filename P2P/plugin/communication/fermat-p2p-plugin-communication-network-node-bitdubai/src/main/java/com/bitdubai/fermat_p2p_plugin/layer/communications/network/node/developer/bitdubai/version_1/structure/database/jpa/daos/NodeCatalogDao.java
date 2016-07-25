/*
 * @#NodeCatalogDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

    public final void changeOfflineCounter(final String  id      ,
                                           final Integer quantity) throws CantUpdateRecordDataBaseException {

        LOG.debug("Executing increaseLateNotificationCounter quantity (" + quantity + ")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            NodeCatalog entity = connection.find(NodeCatalog.class, id);
            entity.setOfflineCounter(quantity);

            transaction.begin();
            connection.merge(entity);
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw new CantUpdateRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    public final void increaseTriedToPropagateTimes(final String id) throws CantUpdateRecordDataBaseException {

        LOG.debug("Executing increaseTriedToPropagateTimes id (" + id + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            NodeCatalog entity = connection.find(NodeCatalog.class, id);
            entity.setLateNotificationsCounter(entity.getTriedToPropagateTimes()+1);

            transaction.begin();
            connection.merge(entity);
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw new CantUpdateRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final long getCountOfNodesToPropagateWith(final String identityPublicKey) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getCountOfNodesToPropagateWith identityPublicKey (" + identityPublicKey + ")");

        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<NodeCatalog> entities = criteriaQuery.from(NodeCatalog.class);

            criteriaQuery.select(connection.getCriteriaBuilder().count(entities));

            Predicate filter = criteriaBuilder.notEqual(entities.get("id"), identityPublicKey);

            criteriaQuery.where(filter);

            Query query = connection.createQuery(criteriaQuery);
            return Integer.parseInt(query.getSingleResult().toString());

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final long getCountOfItemsToShare(final Long currentNodesInCatalog) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getCountOfItemsToShare currentNodesInCatalog (" + currentNodesInCatalog + ")");

        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<NodeCatalog> entities = criteriaQuery.from(NodeCatalog.class);

            criteriaQuery.select(connection.getCriteriaBuilder().count(entities));

            List<Predicate> predicates = new ArrayList<>();

            Predicate pendingPropagationsFilter = criteriaBuilder.greaterThan(entities.<Integer>get("pendingPropagations"), 0);

            predicates.add(pendingPropagationsFilter);

            if (currentNodesInCatalog != null) {
                Predicate triedToPropagateTimesFilter = criteriaBuilder.lessThan(entities.<Integer>get("triedToPropagateTimes"), 0);

                predicates.add(triedToPropagateTimesFilter);
            }

            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            Query query = connection.createQuery(criteriaQuery);
            return Integer.parseInt(query.getSingleResult().toString());

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final List<NodeCatalog> listItemsToShare(final Long currentNodesInCatalog) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getCountOfItemsToShare currentNodesInCatalog (" + currentNodesInCatalog + ")");

        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<NodeCatalog> criteriaQuery = criteriaBuilder.createQuery(NodeCatalog.class);
            Root<NodeCatalog> entities = criteriaQuery.from(NodeCatalog.class);

            criteriaQuery.select(entities);

            List<Predicate> predicates = new ArrayList<>();

            Predicate pendingPropagationsFilter = criteriaBuilder.greaterThan(entities.<Integer>get("pendingPropagations"), 0);

            predicates.add(pendingPropagationsFilter);

            if (currentNodesInCatalog != null) {
                Predicate triedToPropagateTimesFilter = criteriaBuilder.lessThan(entities.<Integer>get("triedToPropagateTimes"), 0);

                predicates.add(triedToPropagateTimesFilter);
            }

            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get("id")));

            return connection.createQuery(criteriaQuery).getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final List<NodeCatalog> listNodesToPropagateWith(final String  identityPublicKey,
                                                            final Integer max              ,
                                                            final Integer offset           ) throws CantReadRecordDataBaseException {

        LOG.debug("Executing listNodesToPropagateWith identityPublicKey (" + identityPublicKey + "), max ("+ max + "), offset ("+offset+")");

        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<NodeCatalog> criteriaQuery = criteriaBuilder.createQuery(NodeCatalog.class);
            Root<NodeCatalog> entities = criteriaQuery.from(NodeCatalog.class);

            criteriaQuery.select(entities);

            Predicate filter = criteriaBuilder.notEqual(entities.get("id"), identityPublicKey);

            criteriaQuery.where(filter);

            List<Order> orderList = new ArrayList<>();
            orderList.add(criteriaBuilder.asc(entities.get("lateNotificationsCounter")));
            orderList.add(criteriaBuilder.asc(entities.get("offlineCounter")));

            criteriaQuery.orderBy(orderList);

            return connection.createQuery(criteriaQuery).getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
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

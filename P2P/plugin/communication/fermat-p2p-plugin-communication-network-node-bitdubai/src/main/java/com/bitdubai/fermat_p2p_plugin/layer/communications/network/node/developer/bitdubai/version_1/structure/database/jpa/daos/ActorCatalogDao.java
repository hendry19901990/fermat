/*
 * @#ActorCatalogDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ResultDiscoveryTraceActor;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.geolocation.BasicGeoRectangle;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.geolocation.CoordinateCalculator;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ActorCatalogDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ActorCatalogDao extends AbstractBaseDao<ActorCatalog> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(AbstractBaseDao.class));

    /**
     * Constructor
     */
    public ActorCatalogDao() {
        super(ActorCatalog.class);
    }

    /**
     * This method returns a list of actors filtered by the discoveryQueryParameters
     * @param discoveryQueryParameters
     * @param clientIdentityPublicKey
     * @param max
     * @param offset
     * @return
     * @throws CantReadRecordDataBaseException
     */
    public List<ActorCatalog> findAll(
            final DiscoveryQueryParameters discoveryQueryParameters,
            final String clientIdentityPublicKey,
            Integer max,
            Integer offset) throws CantReadRecordDataBaseException {
        LOG.debug(new StringBuilder("Executing list(")
                .append(discoveryQueryParameters)
                .append(", ")
                .append(clientIdentityPublicKey)
                .append(", ")
                .append(max)
                .append(", ")
                .append(offset)
                .append(")")
                .toString());
        EntityManager connection = getConnection();
        connection.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);

        System.out.println("I am a clientIdentityPublicKey: " + clientIdentityPublicKey);
        try {
            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<ActorCatalog> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<ActorCatalog> entities = criteriaQuery.from(entityClass);
            criteriaQuery.select(entities);
            BasicGeoRectangle basicGeoRectangle = new BasicGeoRectangle();
            Map<String, Object> filters = buildFilterGroupFromDiscoveryQueryParameters(
                    discoveryQueryParameters);

            List<Predicate> predicates = new ArrayList<>();
            //Verify that the filters are not empty
            if (filters != null && filters.size() > 0) {

                //We are gonna calculate the geoRectangle only in case this filter exists
                if(filters.containsKey("location")){
                    //Getting GeoLocation from client
                    GeoLocation clientGeoLocation = getClientGeoLocation(
                            clientIdentityPublicKey);
                    //Calculate the BasicGeoRectangle
                    double distance;
                    try{
                        distance = (double) filters.get("distance");
                    } catch (ClassCastException cce){
                        //In this case, we assume a minimum distance as 1Km
                        distance = 1;
                    }
                    basicGeoRectangle = CoordinateCalculator
                            .calculateCoordinate(
                                    clientGeoLocation,
                                    distance);
                }

                //Walk the key map that representing the attribute names
                for (String attributeName : filters.keySet()) {

                    //Verify that the value is not empty
                    if (filters.get(attributeName) != null && filters.get(attributeName) != "") {

                        Predicate filter;

                        // If it contains the "." because it is filtered by an attribute of an attribute
                        if (attributeName.contains(".")) {

                            StringTokenizer parts = new StringTokenizer(attributeName,".");
                            Path<Object> path = null;

                            //Walk the path for all required parts
                            while (parts.hasMoreElements()) {

                                if (path == null) {
                                    path = entities.get(parts.nextToken());
                                }else {
                                    path = path.get(parts.nextToken());
                                }
                            }

                            filter = criteriaBuilder.equal(path, filters.get(attributeName));

                        }else{

                            if(attributeName.contains("location")){
                                //create criteria builder with location
                                //Lower corner queries
                                Path<Double> path = entities.get(attributeName);
                                path = path.get("latitude");
                                //lower latitude
                                filter = criteriaBuilder.greaterThan(
                                        path, basicGeoRectangle.getLowerLatitude());
                                predicates.add(filter);
                                //lower longitude
                                path = entities.get(attributeName);
                                path = path.get("longitude");
                                filter = criteriaBuilder.greaterThan(
                                        path, basicGeoRectangle.getLowerLongitude());
                                predicates.add(filter);
                                //upper latitude
                                path = entities.get(attributeName);
                                path = path.get("latitude");
                                filter = criteriaBuilder.lessThan(
                                        path, basicGeoRectangle.getUpperLatitude());
                                predicates.add(filter);
                                //upper longitude
                                path = entities.get(attributeName);
                                path = path.get("longitude");
                                filter = criteriaBuilder.lessThan(
                                        path, basicGeoRectangle.getUpperLongitude());
                                predicates.add(filter);
                                //The location filters are set, we will continue;
                                continue;

                            } else {

                                //Create the new condition for each attribute we get
                                filter = criteriaBuilder.equal(entities.get(attributeName), filters.get(attributeName));

                            }

                        }
                        //System.out.println("***************Hi, I'm a filter: "+filter);
                        predicates.add(filter);
                    }

                }



            }

            //Filter the requester actor
            Path<Object> path = entities.get("clientIdentityPublicKey");
            Predicate actorFilter = criteriaBuilder.notEqual(path, clientIdentityPublicKey);
            //System.out.println("I'm an actor filter: "+actorFilter.toString());
            predicates.add(actorFilter);
            //System.out.println("I'm predicates: "+predicates);
            // Add the conditions of the where
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            //TODO: determinate the distance of every actor and order it
            //criteriaQuery.orderBy(criteriaBuilder.asc(entities.get(attributeNameOrder)));
            //Root<ActorCatalog> root = criteriaQuery.from(entityClass);
            //criteriaQuery.select(root);
            TypedQuery<ActorCatalog> query = connection.createQuery(criteriaQuery);
            //System.out.println("---------------Criteria Q:"+criteriaQuery);
            //System.out.println("---------------Typed Query:"+query);

            if(offset != null)
                query.setFirstResult(offset);
            if(max != null)
                query.setMaxResults(max);


            return query.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(
                    e,
                    "Network Node",
                    "Cannot load records from database");
        } finally {
            connection.close();
        }

    }

    public final void decreasePendingPropagationsCounter(final String id) throws CantUpdateRecordDataBaseException {

        LOG.debug("Executing decreasePendingPropagationsCounter id ("+id+")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            Query query = connection.createQuery("UPDATE ActorCatalog a SET a.pendingPropagations = a.pendingPropagations-1 WHERE a.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();

            transaction.commit();
            connection.flush();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantUpdateRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final Long getCountOfItemsToShare(final Long currentNodesInCatalog) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getCountOfItemsToShare currentNodesInCatalog (" + currentNodesInCatalog + ")");

        EntityManager connection = getConnection();

        try {

            String sqlQuery ="SELECT COUNT(a.id) " +
                    "FROM ActorCatalog a " +
                    "WHERE a.pendingPropagations > 0 AND a.triedToPropagateTimes > :triedToPropagateTimes";

            TypedQuery<Long> q = connection.createQuery(sqlQuery, Long.class);

            q.setParameter("triedToPropagateTimes", currentNodesInCatalog);

            return q.getSingleResult();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final List<ActorPropagationInformation> listItemsToShare(final Long currentNodesInCatalog) throws CantReadRecordDataBaseException {

        LOG.debug("Executing ActorCatalogDao.listItemsToShare currentNodesInCatalog (" + currentNodesInCatalog + ")");

        EntityManager connection = getConnection();
        connection.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);

        try {

            String sqlQuery ="SELECT NEW ActorPropagationInformation(a.id, a.version, a.lastUpdateType) " +
                    "FROM ActorCatalog a " +
                    "WHERE a.triedToPropagateTimes < :currentNodesInCatalog AND a.pendingPropagations > 0";

            TypedQuery<ActorPropagationInformation> q = connection.createQuery(
                    sqlQuery, ActorPropagationInformation.class);

            q.setParameter("currentNodesInCatalog", currentNodesInCatalog);

            return q.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final ActorPropagationInformation getActorPropagationInformation(final String publicKey) throws CantReadRecordDataBaseException {

        LOG.debug("Executing ActorCatalogDao.getActorPropagationInformation publicKey (" + publicKey + ")");

        EntityManager connection = getConnection();
        connection.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.BYPASS);

        try {

            String sqlQuery ="SELECT NEW ActorPropagationInformation(a.id, a.version, a.lastUpdateType) " +
                    "FROM ActorCatalog a " +
                    "WHERE a.id = :publicKey";

            TypedQuery<ActorPropagationInformation> q = connection.createQuery(
                    sqlQuery, ActorPropagationInformation.class);

            q.setParameter("publicKey", publicKey);

            return q.getSingleResult();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    public final void increaseTriedToPropagateTimes(final String id) throws CantUpdateRecordDataBaseException {

        LOG.debug("Executing increaseTriedToPropagateTimes id (" + id + ")");

        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();

            Query query = connection.createQuery("UPDATE ActorCatalog a SET a.triedToPropagateTimes = a.triedToPropagateTimes+1 WHERE a.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();

            transaction.commit();
            connection.flush();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new CantUpdateRecordDataBaseException(e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    /**
     * Construct database filter from discovery query parameters.
     *
     * @param params parameters to filter the actors catalog table.
     *
     * @return a list with the database table filters.
     */
    private Map<String, Object> buildFilterGroupFromDiscoveryQueryParameters(final DiscoveryQueryParameters params){

        final Map<String, Object> filters = new HashMap<>();

        if (params.getIdentityPublicKey() != null)
            filters.put("id", params.getIdentityPublicKey());

        if (params.getName() != null)
            filters.put("name", params.getName());

        if (params.getAlias() != null)
            filters.put("alias",  params.getAlias());

        if (params.getActorType() != null){
            System.out.println("---------------------------------Actor type:"+params.getActorType());
            filters.put("actorType", params.getActorType());
        }

        if (params.getExtraData() != null)
            filters.put("extraData", params.getExtraData());

        if (params.getLastConnectionTime() != null)
            filters.put("lastConnection", params.getLastConnectionTime().toString());

        if (params.getLastConnectionTime() != null)
            filters.put("location", params.getLocation());

        if (params.getLastConnectionTime() != null)
            filters.put("distance", params.getDistance());

        return filters;
    }

    /**
     * Get the client geolocation
     *
     * @param clientPublicKey
     * @return GeoLocation
     * @throws CantReadRecordDataBaseException
     */
    private GeoLocation getClientGeoLocation(String clientPublicKey)
            throws CantReadRecordDataBaseException {
        return  JPADaoFactory.getGeoLocationDao().findById(clientPublicKey);
    }

    /**
     * Persist the entity into the data base
     * @param entity
     * @throws CantReadRecordDataBaseException
     */
    public void persist(ActorCatalog entity) throws CantInsertRecordDataBaseException {

        LOG.debug("Executing persist("+entity+")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            if(entity.getClientIdentityPublicKey()==null){
                entity.setClientIdentityPublicKey(UUID.randomUUID().toString());
            }
            transaction.begin();
            connection.persist(entity);
            connection.flush();
            transaction.commit();

        }catch (Exception e){
            LOG.error(e);
            throw new CantInsertRecordDataBaseException(CantInsertRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Get the home node for a actor
     *
     * @param actorID
     * @return NodeCatalog
     * @throws CantReadRecordDataBaseException
     */
    public NodeCatalog getHomeNode(String actorID) throws CantReadRecordDataBaseException {

        LOG.debug("Executing getHomeNode(" + actorID + ")");
        EntityManager connection = getConnection();

        try {

            TypedQuery<NodeCatalog> query = connection.createQuery("SELECT a.homeNode FROM ActorCatalog a WHERE id = :id", NodeCatalog.class);
            query.setParameter("id", actorID);
            query.setMaxResults(1);

            List<NodeCatalog> nodeCatalogsList = query.getResultList();
            return (nodeCatalogsList != null && !nodeCatalogsList.isEmpty() ? nodeCatalogsList.get(0) : null);

        } catch (Exception e) {
            LOG.error(e);
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    public ResultDiscoveryTraceActor getActorHomeNodeData(String publicKey) throws CantReadRecordDataBaseException {

        ActorProfile actorProfile = new ActorProfile();
        actorProfile.setIdentityPublicKey(publicKey);

        NodeProfile nodeProfile = new NodeProfile();

        NodeCatalog nodeCatalog = getHomeNode(publicKey);

        if (nodeCatalog != null) {
            nodeProfile.setDefaultPort(nodeCatalog.getDefaultPort());
            nodeProfile.setIp(nodeCatalog.getIp());

            return new ResultDiscoveryTraceActor(nodeProfile, actorProfile);
        } else {
            return null;
        }

    }

}

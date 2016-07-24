/*
 * @#AbstractBaseDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.AbstractBaseEntity;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.AbstractBaseDao</code> is
 * the base for all Data Access Object
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class AbstractBaseDao<E extends AbstractBaseEntity> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(AbstractBaseDao.class));

    /**
     * Represent the entityClass
     */
    private Class<E> entityClass;

    /**
     * Constructor
     * @param entityClass
     */
    public AbstractBaseDao(Class<E> entityClass){
        this.entityClass = entityClass;
    }

    /**
     * Get a new connection
     * @return EntityManager
     */
    public EntityManager getConnection() {
        return DatabaseManager.getConnection();
    }

    /**
     * Find a entity by his id
     * @param id
     * @return Entity
     */
    public E findById(Object id) throws CantReadRecordDataBaseException {

        LOG.debug("Executing findById(" + id + ")");
        EntityManager connection = getConnection();
        E entity = null;

        try {

            entity = connection.find(entityClass, id);

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

        return entity;

    }

    /**
     * Save the entity into the data base
     * @param entity
     * @throws CantReadRecordDataBaseException
     */
    public void save(E entity) throws CantReadRecordDataBaseException{

        LOG.debug("Executing save("+entity+")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();
            connection.persist(entity);
            connection.flush();
            transaction.commit();

        }catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        }finally {
            connection.close();
        }

    }

    /**
     * Update a entity values in the database
     * @param entity
     * @throws CantUpdateRecordDataBaseException
     */
    public void update(E entity) throws CantUpdateRecordDataBaseException {

        LOG.debug("Executing update(" + entityClass + ")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();
            connection.merge(entity);
            transaction.commit();

        } catch (Exception e){
            transaction.rollback();
            throw new CantUpdateRecordDataBaseException(CantUpdateRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    /**
     * Delete a entity from data base
     * @param entity
     * @throws CantDeleteRecordDataBaseException
     */
    public void delete(E entity) throws CantDeleteRecordDataBaseException{

        LOG.debug("Executing update(" + entityClass + ")");
        EntityManager connection = getConnection();
        EntityTransaction transaction = connection.getTransaction();

        try {

            transaction.begin();
            connection.remove(connection.merge(entity));
            transaction.commit();

        } catch (Exception e){
            transaction.rollback();
            throw new CantDeleteRecordDataBaseException(CantDeleteRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    /**
     * List all entities
     * @return List
     */
    public List<E> list() throws CantReadRecordDataBaseException {

        LOG.debug("Executing list()");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder builder = connection.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(entityClass);
            Root<E> entities = query.from(entityClass);
            query.select(entities);
           // query.orderBy(builder.asc(entities.get("id")));

            return connection.createQuery(query).getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    /**
     * List all entities order by the attribute
     * @param attributeOrder
     * @return List
     */
    public List<E> listOrderBy(String attributeOrder) throws CantReadRecordDataBaseException {

        LOG.debug("Executing listOrderBy()");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder builder = connection.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(entityClass);
            Root<E> entities = query.from(entityClass);
            query.select(entities);
            query.orderBy(builder.asc(entities.get(attributeOrder)));
            return connection.createQuery(query).getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    /**
     * List all entities paginating
     * @param offset
     * @param max
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(Integer offset, Integer max) throws CantReadRecordDataBaseException {

        LOG.debug("Executing list("+offset+", "+ max +")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entities = criteriaQuery.from(entityClass);
            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get("id")));
            TypedQuery<E> query = connection.createQuery(criteriaQuery);
            query.setFirstResult(offset);
            query.setMaxResults(max);

            return query.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }


    /**
     * List all entities that match with the parameters
     * @param attributeName
     * @param attributeValue
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(String attributeName, Object attributeValue) throws CantReadRecordDataBaseException {

        LOG.debug("Executing list("+attributeName+", "+ attributeValue +")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder builder = connection.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(entityClass);
            Root<E> entities = query.from(entityClass);
            query.select(entities);

            Predicate attribute = null;

            if ((attributeName != null) && (!(attributeName.isEmpty()))) {
                attribute = builder.equal(entities.get(attributeName), attributeValue);
            }

            query.where(attribute);
            query.orderBy(builder.asc(entities.get("id")));

            return connection.createQuery(query).getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    /**
     * List all entities that match with the parameters and
     * order by the attribute name
     * @param attributeName
     * @param attributeValue
     * @param attributeNameOrder
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(String attributeName, Object attributeValue, String attributeNameOrder) throws CantReadRecordDataBaseException {

        LOG.debug("Executing list(" + attributeName + ", " + attributeValue + ", "+ attributeNameOrder +")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder builder = connection.getCriteriaBuilder();
            CriteriaQuery<E> query = builder.createQuery(entityClass);
            Root<E> entities = query.from(entityClass);
            query.select(entities);

            Predicate attribute = null;

            if ((attributeName != null) && (!(attributeName.isEmpty()))) {
                attribute = builder.equal(entities.get(attributeName), attributeValue);
            }

            query.where(attribute);
            query.orderBy(builder.asc(entities.get(attributeNameOrder)));

            return connection.createQuery(query).getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }


    /**
     * List all entities that match with the filters and
     * paginating
     * @param offset
     * @param max
     * @param filters
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(Integer offset, Integer max, Map<String, Object> filters) throws CantReadRecordDataBaseException{
        
        LOG.debug("Executing list(" + offset + ", " + max + ", "+ filters +")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entities = criteriaQuery.from(entityClass);

            //Verify that the filters are not empty
            if (filters != null && filters.size() > 0) {

                List<Predicate> predicates = new ArrayList<>();

                //Walk the key map that representing the attribute names
                for (String attributeName : filters.keySet()) {

                    //Verify that the value is not empty
                    if (filters.get(attributeName) != null && filters.get(attributeName) != "") {

                        Predicate filter = null;

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
                            //Create the new condition for each attribute we get
                            filter = criteriaBuilder.equal(entities.get(attributeName), filters.get(attributeName));
                        }

                        predicates.add(filter);
                    }

                }

                // Add the conditions of the where
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            }

            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get("id")));
            TypedQuery<E> query = connection.createQuery(criteriaQuery);
            query.setFirstResult(offset);
            query.setMaxResults(max);

            return query.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }

    /**
     * List all entities that match with the filters
     * @param filters
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(Map<String, Object> filters) throws CantReadRecordDataBaseException{

        LOG.debug("Executing list(" + filters + ")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entities = criteriaQuery.from(entityClass);

            //Verify that the filters are not empty
            if (filters != null && filters.size() > 0) {

                List<Predicate> predicates = new ArrayList<>();

                //Walk the key map that representing the attribute names
                for (String attributeName : filters.keySet()) {

                    //Verify that the value is not empty
                    if (filters.get(attributeName) != null && filters.get(attributeName) != "") {

                        Predicate filter = null;

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
                            //Create the new condition for each attribute we get
                            filter = criteriaBuilder.equal(entities.get(attributeName), filters.get(attributeName));
                        }

                        predicates.add(filter);
                    }

                }

                // Add the conditions of the where
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            }

            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get("id")));
            TypedQuery<E> query = connection.createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }


    /**
     * List all entities that match with the filters and order by the
     * attribute name.
     * @param filters
     * @param attributeNameOrder
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(Map<String, Object> filters, String attributeNameOrder) throws CantReadRecordDataBaseException{

        LOG.debug("Executing list(" + filters + ", "+attributeNameOrder+")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entities = criteriaQuery.from(entityClass);

            //Verify that the filters are not empty
            if (filters != null && filters.size() > 0) {

                List<Predicate> predicates = new ArrayList<>();

                //Walk the key map that representing the attribute names
                for (String attributeName : filters.keySet()) {

                    //Verify that the value is not empty
                    if (filters.get(attributeName) != null && filters.get(attributeName) != "") {

                        Predicate filter = null;

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
                            //Create the new condition for each attribute we get
                            filter = criteriaBuilder.equal(entities.get(attributeName), filters.get(attributeName));
                        }

                        predicates.add(filter);
                    }

                }

                // Add the conditions of the where
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            }

            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get(attributeNameOrder)));
            TypedQuery<E> query = connection.createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }
    }


    /**
     * List all entities that match with the filters, order by attribute name
     * and apply pagination
     *
     * @param offset
     * @param max
     * @param filters
     * @param attributeNameOrder
     * @return List
     * @throws CantReadRecordDataBaseException
     */
    public List<E> list(Integer offset, Integer max, Map<String, Object> filters, String attributeNameOrder) throws CantReadRecordDataBaseException{

        LOG.debug("Executing list(" + offset + ", " + max + ", "+ filters +", "+ attributeNameOrder +")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entities = criteriaQuery.from(entityClass);

            //Verify that the filters are not empty
            if (filters != null && filters.size() > 0) {

                List<Predicate> predicates = new ArrayList<>();

                //Walk the key map that representing the attribute names
                for (String attributeName : filters.keySet()) {

                    //Verify that the value is not empty
                    if (filters.get(attributeName) != null && filters.get(attributeName) != "") {

                        Predicate filter = null;

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
                            //Create the new condition for each attribute we get
                            filter = criteriaBuilder.equal(entities.get(attributeName), filters.get(attributeName));
                        }

                        predicates.add(filter);
                    }

                }

                // Add the conditions of the where
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            }

            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get(attributeNameOrder)));
            TypedQuery<E> query = connection.createQuery(criteriaQuery);
            query.setFirstResult(offset);
            query.setMaxResults(max);

            return query.getResultList();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }
    }
    
    /**
     * Count all entities
     * @return int
     */
    public int count() throws CantReadRecordDataBaseException {

        LOG.debug("Executing count()");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<E> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(connection.getCriteriaBuilder().count(root));
            Query query = connection.createQuery(criteriaQuery);
            return Integer.parseInt(query.getSingleResult().toString());

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }
    }

    /**
     * Count all that match whit the filters
     *
     * @param filters
     * @return int
     * @throws CantReadRecordDataBaseException
     */
    public int count(Map<String, Object> filters) throws CantReadRecordDataBaseException{

        LOG.debug("Executing list(" + filters + ")");
        EntityManager connection = getConnection();

        try {

            CriteriaBuilder criteriaBuilder = connection.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<E> entities = criteriaQuery.from(entityClass);

            //Verify that the filters are not empty
            if (filters != null && filters.size() > 0) {

                List<Predicate> predicates = new ArrayList<>();

                //Walk the key map that representing the attribute names
                for (String attributeName : filters.keySet()) {

                    //Verify that the value is not empty
                    if (filters.get(attributeName) != null && filters.get(attributeName) != "") {

                        Predicate filter = null;

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
                            //Create the new condition for each attribute we get
                            filter = criteriaBuilder.equal(entities.get(attributeName), filters.get(attributeName));
                        }

                        predicates.add(filter);
                    }

                }

                // Add the conditions of the where
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

            }

            criteriaQuery.orderBy(criteriaBuilder.asc(entities.get("id")));
            TypedQuery<E> query = connection.createQuery(criteriaQuery);
            return query.getResultList().size();

        } catch (Exception e){
            throw new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, e, "Network Node", "");
        } finally {
            connection.close();
        }

    }
}

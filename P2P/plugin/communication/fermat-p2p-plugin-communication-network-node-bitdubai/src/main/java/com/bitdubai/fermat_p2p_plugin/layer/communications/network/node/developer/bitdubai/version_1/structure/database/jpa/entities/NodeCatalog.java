/*
 * @#NodeCatalog.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog</code>
 * represent the data of the node into the catalog
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@Access(AccessType.PROPERTY)
public class NodeCatalog extends NodeProfile implements AbstractBaseEntity<String>{

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the lastConnectionTimestamp
     */
    private Timestamp lastConnectionTimestamp;

    /**
     * Represent the lateNotificationsCounter
     */
    private Integer lateNotificationsCounter;

    /**
     * Represent the offlineCounter
     */
    private Integer offlineCounter;

    /**
     * Represent the registeredTimestamp
     */
    private Timestamp registeredTimestamp;

    /**
     * Represent the signature
     */
    private String signature;

    /**
     * Represent the version
     */
    private Integer version;

    /**
     * Constructor
     */
    public NodeCatalog() {
        super();
        this.lastConnectionTimestamp = new Timestamp(System.currentTimeMillis());
        this.lateNotificationsCounter = 0;
        this.offlineCounter = 0;
        this.registeredTimestamp = new Timestamp(System.currentTimeMillis());
        this.signature = "";
        this.version = 0;
    }

    /**
     * Constructor with parameters
     * @param nodeProfile
     */
    public NodeCatalog(NodeProfile nodeProfile) {
        super();
        this.setId(nodeProfile.getIdentityPublicKey());
        this.setName(nodeProfile.getName());
        this.setLocation(nodeProfile.getLocation());
        this.setDefaultPort(nodeProfile.getDefaultPort());
        this.setStatus(nodeProfile.getStatus());
        this.lastConnectionTimestamp = new Timestamp(System.currentTimeMillis());
        this.lateNotificationsCounter = 0;
        this.offlineCounter = 0;
        this.registeredTimestamp = new Timestamp(System.currentTimeMillis());
        this.signature = "";
        this.version = 0;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@getId()
     */
    @Id
    @Override
    public String getId() {
        return super.getIdentityPublicKey();
    }

    /**
     * Set the id
     * @param id
     */
    public void setId(String id){
        this.setIdentityPublicKey(id);
    }

    /**
     * Get the LastConnectionTimestamp
     * @return Timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getLastConnectionTimestamp() {
        return lastConnectionTimestamp;
    }

    /**
     * Set the LastConnectionTimestamp
     * @param lastConnectionTimestamp
     */
    public void setLastConnectionTimestamp(Timestamp lastConnectionTimestamp) {
        this.lastConnectionTimestamp = lastConnectionTimestamp;
    }

    /**
     * Get the LateNotificationsCounter
     * @return Timestamp
     */
    public Integer getLateNotificationsCounter() {
        return lateNotificationsCounter;
    }

    /**
     * Set the LateNotificationsCounter
     * @param lateNotificationsCounter
     */
    public void setLateNotificationsCounter(Integer lateNotificationsCounter) {
        this.lateNotificationsCounter = lateNotificationsCounter;
    }

    /**
     * Get the OfflineCounter
     * @return Integer
     */
    public Integer getOfflineCounter() {
        return offlineCounter;
    }

    /**
     * Set the OfflineCounter
     * @param offlineCounter
     */
    public void setOfflineCounter(Integer offlineCounter) {
        this.offlineCounter = offlineCounter;
    }

    /**
     * Get the RegisteredTimestamp
     * @return Timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    /**
     * Set the RegisteredTimestamp
     * @param registeredTimestamp
     */
    public void setRegisteredTimestamp(Timestamp registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }

    /**
     * Get the Version
     * @return Integer
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Set the Version
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the Signature
     * @return signature
     */
    @NotNull
    public String getSignature() {
        return signature;
    }

    /**
     * Set the Signature
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
}

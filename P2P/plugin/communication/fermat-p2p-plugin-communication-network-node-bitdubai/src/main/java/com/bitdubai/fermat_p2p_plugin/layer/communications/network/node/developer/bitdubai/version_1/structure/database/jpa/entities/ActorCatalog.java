/*
 * @#ActorCatalog.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInActor;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog</code>
 * represent the data of the actor into the catalog
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@Access(AccessType.PROPERTY)
public class ActorCatalog extends ActorProfile implements AbstractBaseEntity<String>{

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the hostedTimestamp
     */
    private Timestamp hostedTimestamp;

    /**
     * Represent the lastUpdateTime
     */
    private Timestamp lastUpdateTime;

    /**
     * Represent the lastConnection
     */
    private Timestamp lastConnection;

    /**
     * Represent the thumbnail
     */
    private byte[] thumbnail;

    /**
     * Represent the homeNode
     */
    private NodeCatalog homeNode;

    /**
     * Represent the session
     */
    private CheckedInActor session;

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
     * Get the hostedTimestamp
     * @return hostedTimestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getHostedTimestamp() {
        return hostedTimestamp;
    }

    /**
     * Set the HostedTimestamp
     * @param hostedTimestamp
     */
    public void setHostedTimestamp(Timestamp hostedTimestamp) {
        this.hostedTimestamp = hostedTimestamp;
    }

    /**
     * Get the LastUpdateTime
     * @return lastUpdateTime
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Set the LastUpdateTime
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Get the LastConnection
     * @return lastConnection
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getLastConnection() {
        return lastConnection;
    }

    /**
     * Set the LastConnection
     * @param lastConnection
     */
    public void setLastConnection(Timestamp lastConnection) {
        this.lastConnection = lastConnection;
    }

    /**
     * Get the Photo
     * @return photo
     */
    @Override
    @Lob
    @Basic(fetch= FetchType.LAZY)
    public byte[] getPhoto() {
        return super.getPhoto();
    }

    /**
     * Get the Thumbnail
     * @return thumbnail
     */
    @Lob
    @Basic(fetch= FetchType.EAGER)
    public byte[] getThumbnail() {
        return thumbnail;
    }

    /**
     * Set the Thumbnail
     * @param thumbnail
     */
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Get the HomeNode
     * @return homeNode
     */
    @NotNull
    @ManyToOne
    @MapKey(name="id")
    public NodeCatalog getHomeNode() {
        return homeNode;
    }

    /**
     * Set the HomeNode
     * @param homeNode
     */
    public void setHomeNode(NodeCatalog homeNode) {
        this.homeNode = homeNode;
    }

    /**
     * Get the Session
     * @return session
     */
    @OneToOne
    public CheckedInActor getSession() {
        return session;
    }

    /**
     * Set the Session
     * @param session
     */
    public void setSession(CheckedInActor session) {
        this.session = session;
    }

}

/*
 * @#ActorCheckIn.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCheckIn</code>
 * represent the session of the a actor into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
public class ActorCheckIn implements AbstractBaseEntity<String>{

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id
     */
    @Id
    private String id;

    /**
     * Represent the actor
     */
    @NotNull
    @OneToOne(mappedBy="session")
    private ActorCatalog actor;

    /**
     * Represent the timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    /**
     * Constructor
     */
    public ActorCheckIn() {
        super();
        this.id = "";
        this.actor = null;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor with parameters
     *
     * @param session
     * @param actor
     */
    public ActorCheckIn(Session session, ActorCatalog actor) {
        this.id = session.getId();
        this.actor = actor;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the actor
     * @return ActorCatalog
     */
    public ActorCatalog getActor() {
        return actor;
    }

    /**
     * Set the actor
     * @param actor
     */
    public void setActor(ActorCatalog actor) {
        this.actor = actor;
    }

    /**
     * Get the timestamp
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorCheckIn)) return false;

        ActorCheckIn that = (ActorCheckIn) o;

        return getId().equals(that.getId());

    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@toString()
     */
    @Override
    public String toString() {
        return "ActorCheckIn{" +
                "id='" + id + '\'' +
                ", actor=" + actor +
                ", timestamp=" + timestamp +
                '}';
    }
}

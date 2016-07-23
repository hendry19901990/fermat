/*
 * @#NetworkServiceCheckIn.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceCheckIn</code> is
 * represent the session of a network service into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
public class NetworkServiceCheckIn implements AbstractBaseEntity<String>{

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
     * Represent the networkServiceProfile
     */
    @NotNull
    @OneToOne
    private NetworkServiceProfile networkServiceProfile;

    /**
     * Represent the timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    /**
     * Constructor
     */
    public NetworkServiceCheckIn() {
        super();
        this.id = "";
        this.networkServiceProfile = null;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor with parameters
     *
     * @param session
     * @param networkServiceProfile
     */
    public NetworkServiceCheckIn(Session session, NetworkServiceProfile networkServiceProfile) {
        this.id = session.getId();
        this.networkServiceProfile = networkServiceProfile;
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
     * Get the networkServiceProfile
     * @return NetworkServiceProfile
     */
    public NetworkServiceProfile getNetworkServiceProfile() {
        return networkServiceProfile;
    }

    /**
     * Set the networkServiceProfile
     * @param networkService
     */
    public void setNetworkServiceProfile(NetworkServiceProfile networkService) {
        this.networkServiceProfile = networkService;
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
        if (!(o instanceof NetworkServiceCheckIn)) return false;

        NetworkServiceCheckIn that = (NetworkServiceCheckIn) o;

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
        return "NetworkServiceCheckIn{" +
                "id='" + id + '\'' +
                ", networkServiceProfile=" + networkServiceProfile +
                ", timestamp=" + timestamp +
                '}';
    }
}

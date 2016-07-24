/*
 * @#ClientCheckIn.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientCheckIn</code> is
 * represent the session of a clientProfile into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
public class NetworkService extends AbstractBaseEntity<String>{

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the Identity public key
     */
    @Id
    private String id;

    /**
     * Represent the location
     */
    @OneToOne(cascade = {CascadeType.ALL}, targetEntity = GeoLocation.class)
    private GeoLocation location;

    /**
     * Represent the status of the profile
     */
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    /**
     * Represent the networkServiceType
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private NetworkServiceType networkServiceType;

    /**
     * Represent the client
     */
    @NotNull
    @ManyToOne(targetEntity = Client.class)
    private Client client;

    /**
     * Constructor with parameter
     * @param nsIdentityPublicKey
     */
    public NetworkService(String nsIdentityPublicKey) {
        this.id = nsIdentityPublicKey;
        this.location = null;
        this.status = null;
        this.networkServiceType = null;
        this.client = null;
    }

    /**
     * Constructor with parameter
     * @param networkServiceProfile
     */
    public NetworkService(NetworkServiceProfile networkServiceProfile) {
        this.id = networkServiceProfile.getIdentityPublicKey();

        if (networkServiceProfile.getLocation() != null){
            this.location = new GeoLocation(networkServiceProfile.getLocation().getLatitude(), networkServiceProfile.getLocation().getLongitude());
        }else {
            this.location = null;
        }

        this.status = networkServiceProfile.getStatus();
        this.networkServiceType = networkServiceProfile.getNetworkServiceType();
        this.client = new Client(networkServiceProfile.getClientIdentityPublicKey());
    }

    /**
     * Constructor with parameters
     * @param id
     * @param location
     * @param status
     * @param networkServiceType
     * @param clientIdentityPublicKey
     */
    public NetworkService(String id, GeoLocation location, ProfileStatus status, NetworkServiceType networkServiceType, String clientIdentityPublicKey) {
        this.location = location;
        this.status = status;
        this.networkServiceType = networkServiceType;
        this.client = new Client(clientIdentityPublicKey);
    }

    /**
     * Get the value of id
     *
     * @return id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the value of location
     *
     * @return location
     */
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * Set the value of location
     *
     * @param location
     */
    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    /**
     * Get the value of status
     *
     * @return status
     */
    public ProfileStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status
     */
    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    /**
     * Get the value of networkServiceType
     *
     * @return networkServiceType
     */
    public NetworkServiceType getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * Set the value of networkServiceType
     *
     * @param networkServiceType
     */
    public void setNetworkServiceType(NetworkServiceType networkServiceType) {
        this.networkServiceType = networkServiceType;
    }

    /**
     * Get the value of client
     *
     * @return client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Set the value of client
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkService)) return false;

        NetworkService that = (NetworkService) o;

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
        return "NetworkService{" +
                "id='" + id + '\'' +
                ", location=" + location +
                ", status=" + status +
                ", networkServiceType=" + networkServiceType +
                ", client='" + client.getId() + '\'' +
                "} ";
    }

    /**
     * Get the NetworkServiceProfile representation
     * @return NetworkServiceProfile
     */
    public NetworkServiceProfile getNetworkServiceProfile(){

        NetworkServiceProfile networkServiceProfile = new NetworkServiceProfile();
        networkServiceProfile.setIdentityPublicKey(this.getId());
        networkServiceProfile.setNetworkServiceType(this.getNetworkServiceType());
        networkServiceProfile.setStatus(this.getStatus());
        networkServiceProfile.setLocation(this.getLocation());
        networkServiceProfile.setClientIdentityPublicKey(this.getClient().getId());

        return networkServiceProfile;
    }
}

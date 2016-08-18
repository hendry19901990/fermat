/*
* @#IsActorOnlineMsgRespond.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;

import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.IsActorOnlineMsgRespond</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 18/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class IsActorOnlineMsgRespond extends MsgRespond {

    /**
     * Represents the query Id
     */
    private UUID queryId;

    /**
     * Represents the actor profile
     */
    private ActorProfile requestedProfile;

    /**
     * Represents the profile status from the requested profile
     */
    private ProfileStatus profileStatus;

    /**
     * Represent the networkServicePublicKey
     */
    private String networkServiceType;

    /**
     * Constructor with parameters
     *
     * @param packageId
     * @param status
     * @param details
     */
    public IsActorOnlineMsgRespond(
            UUID packageId,
            STATUS status,
            String details,
            ActorProfile requestedProfile,
            ProfileStatus profileStatus,
            UUID queryId,
            String networkServiceType
    ) {
        super(
                packageId,
                status,
                details);
        this.requestedProfile = requestedProfile;
        this.profileStatus = profileStatus;
        this.queryId = queryId;
        this.networkServiceType = networkServiceType;
    }

    /**
     * This method returns the query Id
     * @return
     */
    public UUID getQueryId() {
        return queryId;
    }

    /**
     * This method returns the requested profile
     * @return
     */
    public ActorProfile getRequestedProfile() {
        return requestedProfile;
    }

    /**
     * This method returns the profile status
     * @return
     */
    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    /**
     * Generate the json representation
     * @return String
     */
    @Override
    public String toJson() {
        return GsonProvider.getGson().toJson(this, getClass());
    }

    public String getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * Get the object
     *
     * @param content
     * @return PackageContent
     */
    public static IsActorOnlineMsgRespond parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, IsActorOnlineMsgRespond.class);
    }

    @Override
    public String toString() {
        return "IsActorOnlineMsgRespond{" +
                "queryId=" + queryId +
                ", requestedProfile=" + requestedProfile +
                ", profileStatus=" + profileStatus +
                ", networkServiceType='" + networkServiceType + '\'' +
                '}';
    }
}
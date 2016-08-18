/*
* @#ACKRespond.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.google.gson.annotations.Expose;

import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ACKRespond</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 18/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ACKRespond extends MsgRespond {


    /**
     * Constructor with parameters
     *
     * @param packageId
     * @param status
     * @param details
     */
    public ACKRespond(UUID packageId, STATUS status, String details) {
        super(packageId, status, details);
    }

    /**
     * Generate the json representation
     *
     * @return String
     */
    @Override
    public String toJson() {
        return GsonProvider.getGson().toJson(this, getClass());
    }

    /**
     * Get the object
     *
     * @param content
     * @return PackageContent
     */
    public static ACKRespond parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, ACKRespond.class);
    }

    @Override
    public String toString() {
        return "ACKRespond{" +
                "packageId=" + getPackageId() +
                '}';
    }


}
/*
* @#ClientCheckInRespond.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.base.STATUS;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.google.gson.annotations.Expose;

import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ClientCheckInRespond</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 18/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientCheckInRespond extends MsgRespond {

    @Expose(serialize = false, deserialize = false)
    private String nodeUri;
    @Expose(serialize = false, deserialize = false)
    private String homeNodePk;

    /**
     * Constructor with parameters
     *
     * @param packageId
     * @param status
     * @param details
     */
    public ClientCheckInRespond(UUID packageId, STATUS status, String details) {
        super(packageId, status, details);
    }


    @Override
    public String toJson() {
        return GsonProvider.getGson().toJson(this);
    }

    public static ClientCheckInRespond parseContent(String content){
        return GsonProvider.getGson().fromJson(content,ClientCheckInRespond.class);
    }


    public void setNodeUri(String nodeUri) {
        this.nodeUri = nodeUri;
    }

    public void setHomeNodePk(String homeNodePk) {
        this.homeNodePk = homeNodePk;
    }

    public String getNodeUri() {
        return nodeUri;
    }

    public String getHomeNodePk() {
        return homeNodePk;
    }
}

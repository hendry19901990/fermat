/*
* @#PackageDecoder.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.nio.ByteBuffer;
import java.util.UUID;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.PackageDecoder</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 20/05/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class PackageDecoder implements Decoder.Binary<Package>{

    /**
     * (non-javadoc)
     * @see Text#init(EndpointConfig)
     */
    @Override
    public void init(EndpointConfig config) {

    }

    /**
     * (non-javadoc)
     * @see Text#destroy()
     */
    @Override
    public void destroy() {

    }

    @Override
    public Package decode(ByteBuffer bytes) throws DecodeException {
        com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.getRootAsPackage(bytes);
        try {
            return Package.rebuildInstance(
                    UUID.fromString(pack.id()),
                    pack.content(),
                    NetworkServiceType.getByCode(pack.networkServiceType()),
                    PackageType.buildWithInt(pack.packageType()),
                    pack.destinationPk()
            );
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public boolean willDecode(ByteBuffer bytes) {
        return true;
    }



//    /**
//     * (non-javadoc)
//     * @see Text#decode(String)
//     */
//    @Override
//    public Package decode(String s) throws DecodeException {
//        return GsonProvider.getGson().fromJson(s, Package.class);
//    }
//
//    /**
//     * (non-javadoc)
//     * @see Text#willDecode(String)
//     */
//    @Override
//    public boolean willDecode(String s) {
//        try{
//            //todo sacar esto...
//            GsonProvider.getJsonParser().parse(s);
//            return true;
//
//        }catch (Exception ex){
//            return false;
//        }
//    }

}

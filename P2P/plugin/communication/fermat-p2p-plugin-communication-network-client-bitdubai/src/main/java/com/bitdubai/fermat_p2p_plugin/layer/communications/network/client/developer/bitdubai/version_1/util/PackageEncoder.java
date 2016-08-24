/*
* @#PackageEncoder.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.com.google.flatbuffers.FlatBufferBuilder;
import com.google.gson.Gson;

import org.apache.commons.lang.ClassUtils;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.PackageEncoder</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 20/05/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class PackageEncoder implements Encoder.Binary<Package>{

    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(PackageEncoder.class.getName()));
    /**
     * (non-javadoc)
     * @see Text#encode(Object)
     */
    @Override
    public ByteBuffer encode(Package packageToSend) throws EncodeException {
        try {
            FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder();
            int packageId = flatBufferBuilder.createString(packageToSend.getPackageId().toString());
            int content = flatBufferBuilder.createString(packageToSend.getContent());
            int networkServiceType = 0;
            if (packageToSend.getNetworkServiceTypeSource()!=null) {
                networkServiceType = flatBufferBuilder.createString(packageToSend.getNetworkServiceTypeSource().getCode());
            }
            int destinationPublicKey = 0;
            if (packageToSend.getDestinationPublicKey()!=null) {
                destinationPublicKey = flatBufferBuilder.createString(packageToSend.getDestinationPublicKey());
            }
            int pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.createPackage(
                    flatBufferBuilder,
                    packageId,
                    content,
                    packageToSend.getPackageType().getPackageTypeAsShort(),
                    networkServiceType,
                    destinationPublicKey);

            flatBufferBuilder.finish(pack);
            return flatBufferBuilder.dataBuffer();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
        LOG.info("PackageEnconder destroy method");
    }

}

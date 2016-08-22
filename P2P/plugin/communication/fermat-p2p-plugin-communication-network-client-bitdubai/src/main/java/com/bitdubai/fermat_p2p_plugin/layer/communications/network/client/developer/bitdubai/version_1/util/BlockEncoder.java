/*
* @#BlockEncoder.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.BlockPackages;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Block;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.BlockEncoder</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 22/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BlockEncoder implements Encoder.Binary<BlockPackages>{

    /**
     * (non-javadoc)
     * @see Text#encode(Object)
     */
    @Override
    public ByteBuffer encode(BlockPackages blockToSend) throws EncodeException {
        try {

            FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder();
            int[] packagesData = new int[blockToSend.size()];
            for (int i=0;i<blockToSend.size();i++){
                Package packageToSend = blockToSend.getPackages().get(i);
                //todo: falta hacer un chequeo y validación más exaustiva de los datos
                int packageId = flatBufferBuilder.createString(packageToSend.getPackageId().toString());
                int content = flatBufferBuilder.createString(packageToSend.getContent());
                int networkServiceType = flatBufferBuilder.createString(packageToSend.getNetworkServiceTypeSource().getCode());
                int pack = 0;
                if (packageToSend.getDestinationPublicKey()!=null) {
                    int destinationPublicKey = flatBufferBuilder.createString(packageToSend.getDestinationPublicKey());
                    pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.createPackage(
                            flatBufferBuilder,
                            packageId,
                            content,
                            packageToSend.getPackageType().getPackageTypeAsShort(),
                            networkServiceType,
                            destinationPublicKey);
                }else {

                    pack = com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package.createPackage(
                            flatBufferBuilder,
                            packageId,
                            content,
                            packageToSend.getPackageType().getPackageTypeAsShort(),
                            networkServiceType,
                            0);
                }
                packagesData[i] = pack;
            }

            int packagesOffSet = Block.createPackagesVector(flatBufferBuilder,packagesData);
            int packageVector = Block.createBlock(flatBufferBuilder, packagesOffSet);
            flatBufferBuilder.finish(packageVector);

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

    }

}

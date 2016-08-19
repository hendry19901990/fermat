/*
* @#Package.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.com.google.flatbuffers.*;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Package</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 19/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class Package extends Table {
    public static Package getRootAsPackage(ByteBuffer _bb) {
        return getRootAsPackage(_bb, new Package());
    }

    public static Package getRootAsPackage(ByteBuffer _bb, Package obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb));
    }

    public Package __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public String id() {
        int o = __offset(4);
        return o != 0 ? __string(o + bb_pos) : null;
    }

    public ByteBuffer idAsByteBuffer() {
        return __vector_as_bytebuffer(4, 1);
    }

    public String content() {
        int o = __offset(6);
        return o != 0 ? __string(o + bb_pos) : null;
    }

    public ByteBuffer contentAsByteBuffer() {
        return __vector_as_bytebuffer(6, 1);
    }

    public short packageType() {
        int o = __offset(8);
        return o != 0 ? bb.getShort(o + bb_pos) : 0;
    }

    public String networkServiceType() {
        int o = __offset(10);
        return o != 0 ? __string(o + bb_pos) : null;
    }

    public ByteBuffer networkServiceTypeAsByteBuffer() {
        return __vector_as_bytebuffer(10, 1);
    }

    public String destinationPk() {
        int o = __offset(12);
        return o != 0 ? __string(o + bb_pos) : null;
    }

    public ByteBuffer destinationPkAsByteBuffer() {
        return __vector_as_bytebuffer(12, 1);
    }

    public static int createPackage(FlatBufferBuilder builder,
                                    int idOffset,
                                    int contentOffset,
                                    short package_type,
                                    int network_service_typeOffset,
                                    int destination_pkOffset) {
        builder.startObject(5);
        Package.addDestinationPk(builder, destination_pkOffset);
        Package.addNetworkServiceType(builder, network_service_typeOffset);
        Package.addContent(builder, contentOffset);
        Package.addId(builder, idOffset);
        Package.addPackageType(builder, package_type);
        return Package.endPackage(builder);
    }

    public static void startPackage(FlatBufferBuilder builder) {
        builder.startObject(5);
    }

    public static void addId(FlatBufferBuilder builder, int idOffset) {
        builder.addOffset(0, idOffset, 0);
    }

    public static void addContent(FlatBufferBuilder builder, int contentOffset) {
        builder.addOffset(1, contentOffset, 0);
    }

    public static void addPackageType(FlatBufferBuilder builder, short packageType) {
        builder.addShort(2, packageType, 0);
    }

    public static void addNetworkServiceType(FlatBufferBuilder builder, int networkServiceTypeOffset) {
        builder.addOffset(3, networkServiceTypeOffset, 0);
    }

    public static void addDestinationPk(FlatBufferBuilder builder, int destinationPkOffset) {
        builder.addOffset(4, destinationPkOffset, 0);
    }

    public static int endPackage(FlatBufferBuilder builder) {
        int o = builder.endObject();
        return o;
    }

    public static void finishPackageBuffer(FlatBufferBuilder builder, int offset) {
        builder.finish(offset);
    }
}



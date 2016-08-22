/*
* @#Block.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common;

import java.nio.*;
import java.lang.*;
import java.util.*;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.com.google.flatbuffers.*;


/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.common.Block</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 22/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class Block extends Table {
    public static Block getRootAsBlock(ByteBuffer _bb) {
        return getRootAsBlock(_bb, new Block());
    }

    public static Block getRootAsBlock(ByteBuffer _bb, Block obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb));
    }

    public Block __init(int _i, ByteBuffer _bb) {
        bb_pos = _i;
        bb = _bb;
        return this;
    }

    public Package packages(int j) {
        return packages(new Package(), j);
    }

    public Package packages(Package obj, int j) {
        int o = __offset(4);
        return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null;
    }

    public int packagesLength() {
        int o = __offset(4);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static int createBlock(FlatBufferBuilder builder,
                                  int packagesOffset) {
        builder.startObject(1);
        Block.addPackages(builder, packagesOffset);
        return Block.endBlock(builder);
    }

    public static void startBlock(FlatBufferBuilder builder) {
        builder.startObject(1);
    }

    public static void addPackages(FlatBufferBuilder builder, int packagesOffset) {
        builder.addOffset(0, packagesOffset, 0);
    }

    public static int createPackagesVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]);
        return builder.endVector();
    }

    public static void startPackagesVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static int endBlock(FlatBufferBuilder builder) {
        int o = builder.endObject();
        return o;
    }

    public static void finishBlockBuffer(FlatBufferBuilder builder, int offset) {
        builder.finish(offset);
    }
}

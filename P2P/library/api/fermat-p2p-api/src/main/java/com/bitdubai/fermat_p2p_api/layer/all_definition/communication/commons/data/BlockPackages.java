/*
* @#BlockPackages.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.BlockPackages</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 19/08/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BlockPackages  implements Serializable {

    private List<Package> packages;

    public BlockPackages() {
        packages = new ArrayList<>();
    }

    public void add(Package p) {
        packages.add(p);
    }

    public int size() {
        return packages.size();
    }

    public List<Package> getPackages() {
        return packages;
    }
}


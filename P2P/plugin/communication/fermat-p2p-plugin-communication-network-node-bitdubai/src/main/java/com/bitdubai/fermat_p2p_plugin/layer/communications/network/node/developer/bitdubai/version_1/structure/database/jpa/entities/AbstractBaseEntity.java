/*
 * @#AbstractBaseEntity.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;

import java.io.Serializable;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.AbstractBaseEntity</code> is
 * the base for all entities
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public abstract interface AbstractBaseEntity<TYPE> extends Serializable{

    /**
     * Return the id of the entity
     * @return id
     */
    TYPE getId();

    /**
     * (non-javadoc)
     * @see Object#hashCode()
     */
    int hashCode();

    /**
     * (non-javadoc)
     * @see Object#toString()
     */
    String toString();

    /**
     * (non-javadoc)
     * @see Object#equals(Object)
     */
    boolean equals(Object o);
}

/*
 * @#AbstractBaseEntity.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;

import java.io.Serializable;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.AbstractBaseEntity</code> is
 * the base for all entities
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
abstract public class AbstractBaseEntity<TYPE> implements Serializable{

    /**
     * Represent the value of serialVersionUID
     */
    private static final long serialVersionUID = 1;

    /**
     * Return the id of the entity
     * @return id
     */
    abstract public TYPE getId();

    /**
     * (non-javadoc)
     * @see Object#hashCode()
     */
    abstract public int hashCode();

    /**
     * (non-javadoc)
     * @see Object#toString()
     */
    abstract public String toString();

    /**
     * (non-javadoc)
     * @see Object#equals(Object)
     */
    abstract public boolean equals(Object o);
}

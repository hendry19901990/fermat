/*
 * @#ProfileRegistrationHistoryDao.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.ProfileRegistrationHistoryDao</code>
 * is the responsible for manage the <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory</code> entity
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ProfileRegistrationHistoryDao extends AbstractBaseDao<ProfileRegistrationHistory> {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ProfileRegistrationHistoryDao.class));

    /**
     * Constructor
     */
    public ProfileRegistrationHistoryDao(){
        super(ProfileRegistrationHistory.class);
    }
}

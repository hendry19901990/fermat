package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorPropagationInformation extends AbstractBaseEntity {

    private final           String                  identityPublicKey;

    private final           Integer                 version;

    private final           ActorCatalogUpdateTypes lastUpdateType;

    private       transient Integer                 pendingPropagations;

    private       transient Integer                 triedToPropagateTimes;

    public ActorPropagationInformation(final String                   identityPublicKey,
                                       final Integer                  version          ,
                                       final ActorCatalogUpdateTypes  lastUpdateType   ) {

        this.identityPublicKey     = identityPublicKey    ;
        this.version               = version              ;
        this.lastUpdateType        = lastUpdateType       ;
    }

    public ActorPropagationInformation(final String                   identityPublicKey    ,
                                       final Integer                  version              ,
                                       final ActorCatalogUpdateTypes  lastUpdateType       ,
                                       final Integer                  pendingPropagations  ,
                                       final Integer                  triedToPropagateTimes) {

        this.identityPublicKey     = identityPublicKey    ;
        this.version               = version              ;
        this.lastUpdateType        = lastUpdateType       ;
        this.pendingPropagations   = pendingPropagations  ;
        this.triedToPropagateTimes = triedToPropagateTimes;
    }

    @Override
    public String getId() {
        return identityPublicKey;
    }

    public Integer getVersion() {
        return version;
    }

    public ActorCatalogUpdateTypes getLastUpdateType() {
        return lastUpdateType;
    }

    public Integer getPendingPropagations() {
        return pendingPropagations;
    }

    public Integer getTriedToPropagateTimes() {
        return triedToPropagateTimes;
    }

    public void increaseTriedToPropagateTimes() {
        triedToPropagateTimes++;
    }
    
    @Override
    public String toString() {
        return "ActorPropagationInformation{" +
                "identityPublicKey='" + identityPublicKey + '\'' +
                ", version=" + version +
                ", lastUpdateType='" + lastUpdateType + '\'' +
                ", pendingPropagations=" + pendingPropagations +
                ", triedToPropagateTimes=" + triedToPropagateTimes +
                '}';
    }
}
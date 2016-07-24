package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class PropagationInformation extends AbstractBaseEntity {

    private final           String  identityPublicKey;

    private final           Integer version;

    private       transient Integer pendingPropagations;

    private       transient Integer triedToPropagateTimes;

    public PropagationInformation(final String  identityPublicKey    ,
                                  final Integer version              ) {

        this.identityPublicKey     = identityPublicKey    ;
        this.version               = version              ;
    }

    public PropagationInformation(final String  identityPublicKey    ,
                                  final Integer version              ,
                                  final Integer pendingPropagations  ,
                                  final Integer triedToPropagateTimes) {

        this.identityPublicKey     = identityPublicKey    ;
        this.version               = version              ;
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
        return "PropagationInformation{" +
                "identityPublicKey='" + identityPublicKey + '\'' +
                ", version=" + version +
                ", pendingPropagations=" + pendingPropagations +
                ", triedToPropagateTimes=" + triedToPropagateTimes +
                '}';
    }
}
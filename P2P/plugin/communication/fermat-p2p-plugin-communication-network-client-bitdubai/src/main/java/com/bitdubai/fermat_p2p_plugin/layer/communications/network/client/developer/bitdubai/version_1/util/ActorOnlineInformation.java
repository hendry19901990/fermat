package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.ActorOnlineInformation</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 19/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public final class ActorOnlineInformation {

    private final boolean isOnline;
    private final boolean sameNode;

    public ActorOnlineInformation(final boolean isOnline,
                                  final boolean sameNode) {

        this.isOnline = isOnline;
        this.sameNode = sameNode;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isSameNode() {
        return sameNode;
    }

    @Override
    public String toString() {
        return "ActorOnlineInformation{" +
                "isOnline=" + isOnline +
                ", sameNode=" + sameNode +
                '}';
    }
}

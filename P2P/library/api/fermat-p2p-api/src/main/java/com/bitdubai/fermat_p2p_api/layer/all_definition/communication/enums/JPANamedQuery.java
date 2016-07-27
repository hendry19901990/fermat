package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;

/**
 * Created by Gabriel Araujo on 26/07/16.
 */
public enum JPANamedQuery implements FermatEnum {

    IS_CLIENT_ONLINE ("isClientOnline"),
    IS_NETWORK_SERVICE_ONLINE("isNetworkServiceOnline"),
    IS_ACTOR_ONLINE("isActorOnline"),
    GET_NODE_PUBLICK_KEY_FROM_ACTOR("getNodePublicKeyFromActor"),
    UNKNOWN    ("UN"),
    ;

    private final String code;

    JPANamedQuery(final String code) {

        this.code     = code    ;
    }

    public static JPANamedQuery getByCode(final String code) {

        for (JPANamedQuery type : JPANamedQuery.values()) {
            if(type.getCode().equals(code))
                return type;
        }

        return UNKNOWN;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}

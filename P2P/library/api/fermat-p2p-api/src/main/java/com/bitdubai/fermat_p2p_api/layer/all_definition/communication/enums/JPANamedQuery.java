package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;

/**
 * Created by Gabriel Araujo on 26/07/16.
 */
public enum JPANamedQuery implements FermatEnum {

    IS_CLIENT_ONLINE ("ClientSession.isOnline"),


    IS_NETWORK_SERVICE_ONLINE("NetworkServiceSession.isOnline"),
    DELETE_ALL_NETWORK_SERVICE_SESSION("NetworkServiceSession.deleteAllSession"),

    IS_ACTOR_ONLINE("ActorSession.isOnline"),
    DELETE_ALL_ACTOR_SESSION("ActorSession.deleteAllSession"),


    GET_NODE_PUBLIC_KEY_FROM_ACTOR("ActorCatalog.getNodePublicKeyFromActor"),


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

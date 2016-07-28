package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;

/**
 * Class used to enum all existing named query for JPA entities.
 * Created by Gabriel Araujo on 26/07/16.
 */
public enum JPANamedQuery implements FermatEnum {

    IS_CLIENT_ONLINE ("ClientCheckIn.isClientOnline"),
    IS_NETWORK_SERVICE_ONLINE("NetworkServiceCheckIn.isNetworkServiceOnline"),
    IS_ACTOR_ONLINE("ActorCheckIn.isActorOnline"),
    GET_ALL_CHECKED_IN_ACTORS_BY_ACTORTYPE("ActorCheckIn.getAllCheckedInActorsByActorType"),
    GET_ALL_CHECKED_IN_ACTORS("ActorCheckIn.getAllCheckedInActors"),
    GET_ACTOR_CATALOG_BY_ACTOR_TYPE("ActorCatalog.getActorCatalogByActorType"),
    GET_ACTOR_CATALOG("ActorCatalog.getActorCatalog"),
    GET_ACTOR_CATALOG_BY_ID("ActorCatalog.getActorCatalogById"),
    GET_CHECK_IN_CLIENTS("ClientCheckIn.getCheckedInClient"),
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

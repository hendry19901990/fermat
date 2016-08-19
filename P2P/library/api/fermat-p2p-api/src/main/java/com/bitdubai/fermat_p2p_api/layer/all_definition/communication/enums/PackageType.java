package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums;

/**
 * The enum <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType</code> represent
 * all type can be a <code>Package</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public enum PackageType {

    // Definition types Client channel

    /*
     * Request packet types
     */

    CHECK_IN_CLIENT_REQUEST,
    CHECK_IN_NETWORK_SERVICE_REQUEST,
    CHECK_IN_ACTOR_REQUEST,

    CHECK_OUT_CLIENT_REQUEST,
    CHECK_OUT_NETWORK_SERVICE_REQUEST,
    CHECK_OUT_ACTOR_REQUEST,

    ACK,
    IS_ACTOR_ONLINE, //This represents a request to check if an actor in online in this or any node

    NETWORK_SERVICE_LIST_REQUEST,
    ACTOR_LIST_REQUEST,
    NEAR_NODE_LIST_REQUEST,

    CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST,
    ACTOR_TRACE_DISCOVERY_QUERY_REQUEST,

    MESSAGE_TRANSMIT,

    ACTOR_CALL_REQUEST,

    UPDATE_ACTOR_PROFILE_REQUEST,

    UPDATE_PROFILE_GEOLOCATION_REQUEST,

    /*
     * Respond packet types
     */
    CHECK_IN_CLIENT_RESPONSE,
    CHECK_IN_NETWORK_SERVICE_RESPONSE,
    CHECK_IN_ACTOR_RESPONSE,

    CHECK_OUT_CLIENT_RESPONSE,
    CHECK_OUT_NETWORK_SERVICE_RESPONSE,
    CHECK_OUT_ACTOR_RESPONSE,

    NETWORK_SERVICE_LIST_RESPONSE,
    ACTOR_LIST_RESPONSE,
    NEAR_NODE_LIST_RESPONSE,

    CHECK_IN_PROFILE_DISCOVERY_QUERY_RESPONSE,
    ACTOR_TRACE_DISCOVERY_QUERY_RESPONSE,

    UPDATE_ACTOR_PROFILE_RESPONSE,

    MESSAGE_TRANSMIT_RESPONSE,

    ACTOR_CALL_RESPONSE,

    // Definition types NODE channel

    /*
     * Request packet types
     */
    ADD_NODE_TO_CATALOG_REQUEST,
    GET_NODE_CATALOG_REQUEST,
    UPDATE_NODE_IN_CATALOG_REQUEST,
    NODES_CATALOG_TO_PROPAGATE_REQUEST,
    NODES_CATALOG_TO_ADD_OR_UPDATE_REQUEST,

    GET_ACTOR_CATALOG_REQUEST,
    ACTOR_CATALOG_TO_PROPAGATE_REQUEST,
    ACTOR_CATALOG_TO_ADD_OR_UPDATE_REQUEST,

    /*
     * Respond packet types
     */
    ADD_NODE_TO_CATALOG_RESPONSE,
    GET_NODE_CATALOG_RESPONSE,
    UPDATE_NODE_IN_CATALOG_RESPONSE,
    NODES_CATALOG_TO_PROPAGATE_RESPONSE,

    GET_ACTOR_CATALOG_RESPONSE,
    ACTOR_CATALOG_TO_PROPAGATE_RESPONSE,

    SERVER_HANDSHAKE_RESPONSE,
    CHECK_IN_CLIENT_RESPOND,
    CHECK_IN_NETWORK_SERVICE_RESPOND

    ;

    public short getPackageTypeAsShort(){
        short packageType = -1;
        switch (this){
            case CHECK_IN_CLIENT_REQUEST:
                packageType = 1;
                break;
            case CHECK_IN_NETWORK_SERVICE_REQUEST:
                packageType = 2;
                break;
            case CHECK_IN_ACTOR_REQUEST:
                packageType = 3;
                break;
            case ACTOR_LIST_REQUEST:
                packageType = 4;
                break;
            case MESSAGE_TRANSMIT:
                packageType = 5;
                break;
            case UPDATE_ACTOR_PROFILE_REQUEST:
                packageType = 6;
                break;
            case ACK:
                packageType = 7;
                break;
            case IS_ACTOR_ONLINE:
                packageType = 8;
                break;
            case SERVER_HANDSHAKE_RESPONSE:
                packageType = 9;
                break;
            case CHECK_IN_CLIENT_RESPOND:
                packageType = 10;
                break;
            case CHECK_IN_NETWORK_SERVICE_RESPOND:
                packageType = 11;
                break;
            case CHECK_IN_ACTOR_RESPONSE:
                packageType = 12;
                break;
        }
        return packageType;
    }

    public static PackageType buildWithInt(short type){
        PackageType packageType = null;
        switch (type){
            case 1:
                packageType = CHECK_IN_CLIENT_REQUEST;
                break;
            case 2:
                packageType = CHECK_IN_NETWORK_SERVICE_REQUEST;
                break;
            case 3:
                packageType = CHECK_IN_ACTOR_REQUEST;
                break;
            case 4:
                packageType = ACTOR_LIST_REQUEST;
                break;
            case 5:
                packageType = MESSAGE_TRANSMIT;
                break;
            case 6:
                packageType = UPDATE_ACTOR_PROFILE_REQUEST;
                break;
            case 7:
                packageType = ACK;
                break;
            case 8:
                packageType = IS_ACTOR_ONLINE;
                break;
            case 9:
                packageType = SERVER_HANDSHAKE_RESPONSE;
                break;
            case 10:
                packageType = CHECK_IN_CLIENT_RESPOND;
                break;
            case 11:
                packageType = CHECK_IN_NETWORK_SERVICE_RESPOND;
                break;
            case 12:
                packageType = CHECK_IN_ACTOR_RESPONSE;
                break;
        }
        return packageType;
    }


}

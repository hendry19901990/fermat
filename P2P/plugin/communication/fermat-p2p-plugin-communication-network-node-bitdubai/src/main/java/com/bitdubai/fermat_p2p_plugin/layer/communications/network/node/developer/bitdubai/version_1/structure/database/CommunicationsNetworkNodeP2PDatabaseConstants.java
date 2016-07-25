package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants</code>
 * keeps constants the column names of the database.<p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 28/06/2016.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class CommunicationsNetworkNodeP2PDatabaseConstants {


    public static final String DATA_BASE_NAME =  "NETWORK_NODE_DB";

    /**
     * actor catalog database table definition.
     */
    public static final String ACTOR_CATALOG_TABLE_NAME                             = "actor_catalog"             ;

    public static final String ACTOR_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME        = "identity_public_key"       ;
    public static final String ACTOR_CATALOG_NAME_COLUMN_NAME                       = "name"                      ;
    public static final String ACTOR_CATALOG_ALIAS_COLUMN_NAME                      = "alias"                     ;
    public static final String ACTOR_CATALOG_ACTOR_TYPE_COLUMN_NAME                 = "actor_type"                ;
    public static final String ACTOR_CATALOG_PHOTO_COLUMN_NAME                      = "photo"                     ;
    public static final String ACTOR_CATALOG_THUMBNAIL_COLUMN_NAME                  = "thumbnail"                 ;
    public static final String ACTOR_CATALOG_LAST_LATITUDE_COLUMN_NAME              = "last_latitude"             ;
    public static final String ACTOR_CATALOG_LAST_LONGITUDE_COLUMN_NAME             = "last_longitude"            ;
    public static final String ACTOR_CATALOG_EXTRA_DATA_COLUMN_NAME                 = "extra_data"                ;
    public static final String ACTOR_CATALOG_HOSTED_TIMESTAMP_COLUMN_NAME           = "hosted_timestamp"          ;
    public static final String ACTOR_CATALOG_LAST_UPDATE_TIME_COLUMN_NAME           = "last_update_time"          ;
    public static final String ACTOR_CATALOG_LAST_CONNECTION_COLUMN_NAME            = "last_connection"           ;
    public static final String ACTOR_CATALOG_NODE_IDENTITY_PUBLIC_KEY_COLUMN_NAME   = "node_identity_public_key"  ;
    public static final String ACTOR_CATALOG_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME = "client_identity_public_key";
    public static final String ACTOR_CATALOG_VERSION_COLUMN_NAME                    = "version"                   ;
    public static final String ACTOR_CATALOG_LAST_UPDATE_TYPE_COLUMN_NAME           = "last_update_type"          ;
    public static final String ACTOR_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME       = "pending_propagations"      ;
    public static final String ACTOR_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME   = "tried_to_propagate_times"  ;

    /**
     * checked in profiles database table definition.
     */
    public static final String CHECKED_IN_PROFILES_TABLE_NAME                       = "checked_in_profiles"  ;

    public static final String CHECKED_IN_PROFILES_IDENTITY_PUBLIC_KEY_COLUMN_NAME  = "identity_public_key"  ;
    public static final String CHECKED_IN_PROFILES_CLIENT_PUBLIC_KEY_COLUMN_NAME    = "client_public_key"    ;
    public static final String CHECKED_IN_PROFILES_INFORMATION_COLUMN_NAME          = "information"          ;
    public static final String CHECKED_IN_PROFILES_PROFILE_TYPE_COLUMN_NAME         = "profile_type"         ;
    public static final String CHECKED_IN_PROFILES_LATITUDE_COLUMN_NAME             = "latitude"             ;
    public static final String CHECKED_IN_PROFILES_LONGITUDE_COLUMN_NAME            = "longitude"            ;
    public static final String CHECKED_IN_PROFILES_CHECKED_IN_TIMESTAMP_COLUMN_NAME = "checked_in_timestamp" ;

    /**
     * Profiles Registration History database table definition.
     */
    public static final String PROFILES_REGISTRATION_HISTORY_TABLE_NAME                      = "profiles_registration_history";

    public static final String PROFILES_REGISTRATION_HISTORY_ID_COLUMN_NAME                  = "id"                           ;
    public static final String PROFILES_REGISTRATION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME = "identity_public_key"          ;
    public static final String PROFILES_REGISTRATION_HISTORY_DEVICE_TYPE_COLUMN_NAME         = "device_type"                  ;
    public static final String PROFILES_REGISTRATION_HISTORY_PROFILE_TYPE_COLUMN_NAME        = "profile_type"                 ;
    public static final String PROFILES_REGISTRATION_HISTORY_CHECKED_TIMESTAMP_COLUMN_NAME   = "checked_timestamp"            ;
    public static final String PROFILES_REGISTRATION_HISTORY_TYPE_COLUMN_NAME                = "type"                         ;
    public static final String PROFILES_REGISTRATION_HISTORY_RESULT_COLUMN_NAME              = "result"                       ;
    public static final String PROFILES_REGISTRATION_HISTORY_DETAIL_COLUMN_NAME              = "detail"                       ;

    /**
     * method calls history database table definition.
     */
    public static final String METHOD_CALLS_HISTORY_TABLE_NAME                             = "method_calls_history"      ;

    public static final String METHOD_CALLS_HISTORY_UUID_COLUMN_NAME                       = "uuid"                      ;
    public static final String METHOD_CALLS_HISTORY_METHOD_NAME_COLUMN_NAME                = "method_name"               ;
    public static final String METHOD_CALLS_HISTORY_PARAMETERS_COLUMN_NAME                 = "parameters"                ;
    public static final String METHOD_CALLS_HISTORY_CLIENT_IDENTITY_PUBLIC_KEY_COLUMN_NAME = "client_identity_public_key";
    public static final String METHOD_CALLS_HISTORY_CREATE_TIMESTAMP_COLUMN_NAME           = "create_timestamp"          ;

    /**
     * nodes catalog database table definition.
     */
    public static final String NODES_CATALOG_TABLE_NAME                            = "nodes_catalog"            ;

    public static final String NODES_CATALOG_IDENTITY_PUBLIC_KEY_COLUMN_NAME       = "identity_public_key"      ;
    public static final String NODES_CATALOG_NAME_COLUMN_NAME                      = "name"                     ;
    public static final String NODES_CATALOG_IP_COLUMN_NAME                        = "ip"                       ;
    public static final String NODES_CATALOG_DEFAULT_PORT_COLUMN_NAME              = "default_port"             ;
    public static final String NODES_CATALOG_LAST_LATITUDE_COLUMN_NAME             = "last_latitude"            ;
    public static final String NODES_CATALOG_LAST_LONGITUDE_COLUMN_NAME            = "last_longitude"           ;
    public static final String NODES_CATALOG_LATE_NOTIFICATION_COUNTER_COLUMN_NAME = "late_notification_counter";
    public static final String NODES_CATALOG_OFFLINE_COUNTER_COLUMN_NAME           = "offline_counter"          ;
    public static final String NODES_CATALOG_REGISTERED_TIMESTAMP_COLUMN_NAME      = "registered_timestamp"     ;
    public static final String NODES_CATALOG_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME = "last_connection_timestamp";
    public static final String NODES_CATALOG_VERSION_COLUMN_NAME                   = "version"                  ;
    public static final String NODES_CATALOG_PENDING_PROPAGATIONS_COLUMN_NAME      = "pending_propagations"     ;
    public static final String NODES_CATALOG_TRIED_TO_PROPAGATE_TIMES_COLUMN_NAME  = "tried_to_propagate_times" ;

}

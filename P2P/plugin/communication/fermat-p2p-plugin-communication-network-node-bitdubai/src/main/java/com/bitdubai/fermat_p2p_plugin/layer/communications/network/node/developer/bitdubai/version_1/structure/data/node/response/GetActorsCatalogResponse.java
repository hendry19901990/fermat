package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.respond.GetActorsCatalogResponse</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 05/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class GetActorsCatalogResponse extends MsgRespond {

    private List<ActorsCatalog> actorsCatalogList;

    /**
     * Represent the count
     */
    private Long count;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     */
    public GetActorsCatalogResponse(STATUS status, String details, List<ActorsCatalog> actorsCatalogList, Long count) {
        super(status, details);
        this.actorsCatalogList = actorsCatalogList;
        this.count = count;
    }

    /**
     * Get the actorsCatalogList
     * @return List<NodeProfile>
     */
    public List<ActorsCatalog> getActorsCatalogList() {
        return actorsCatalogList;
    }

    /**
     * Get the Count
     * @return Long
     */
    public Long getCount() {
        return count;
    }

    /**
     * Generate the json representation
     * @return String
     */
    @Override
    public String toJson() {
        return GsonProvider.getGson().toJson(this, getClass());
    }

    /**
     * Get the object
     *
     * @param content
     * @return PackageContent
     */
    public static GetActorsCatalogResponse parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, GetActorsCatalogResponse.class);
    }
}

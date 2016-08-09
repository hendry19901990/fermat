package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.respond.AddNodeToCatalogResponse</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 05/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class AddNodeToCatalogResponse extends MsgRespond {

    /**
     * Represent the nodeProfileAdded
     */
    private final NodeProfile nodeProfileAdded;

    /**
     * Represent the alreadyExists
     */
    private final Boolean alreadyExists;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     */
    public AddNodeToCatalogResponse(STATUS status, String details, NodeProfile nodeProfileAdded, Boolean alreadyExists) {
        super(status, details);
        this.nodeProfileAdded = nodeProfileAdded;
        this.alreadyExists = alreadyExists;
    }

    /**
     * Get the NodeProfileAdded value
     *
     * @return NodeProfileAdded
     */
    public NodeProfile getNodeProfileAdded() {
        return nodeProfileAdded;
    }

    /**
     * Get the AlreadyExists value
     *
     * @return AlreadyExists
     */
    public Boolean getAlreadyExists() {
        return alreadyExists;
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
    public static AddNodeToCatalogResponse parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, AddNodeToCatalogResponse.class);
    }
}

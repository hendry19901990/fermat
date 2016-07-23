package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.request.NodesCatalogToPropagateResponse</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogToPropagateResponse extends MsgRespond {

    /**
     * Represent the node profile
     */
    private final List<PropagationInformation> propagationInformationResponseList;

    public NodesCatalogToPropagateResponse(List<PropagationInformation> propagationInformationResponseList, STATUS status, String details) {
        super(status, details);
        this.propagationInformationResponseList = propagationInformationResponseList;
    }

    public List<PropagationInformation> getPropagationInformationResponseList() {
        return propagationInformationResponseList;
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
    public static NodesCatalogToPropagateResponse parseContent(String content) {
        return GsonProvider.getGson().fromJson(content, NodesCatalogToPropagateResponse.class);
    }

    @Override
    public String toString() {
        return "NodesCatalogToPropagateResponse{" +
                "propagationInformationResponseList=" + propagationInformationResponseList +
                '}';
    }
}

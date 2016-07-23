package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToPropagateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.NodesCatalogToPropagateResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.NodesCatalogToPropagateRequestProcessor</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogToPropagateRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NodesCatalogToPropagateRequestProcessor.class));

    /**
     * Constructor
     */
    public NodesCatalogToPropagateRequestProcessor() {
        super(PackageType.NODES_CATALOG_TO_PROPAGATE_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public synchronized void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: " + packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        NodesCatalogToPropagateRequest messageContent = NodesCatalogToPropagateRequest.parseContent(packageReceived.getContent());

        List<PropagationInformation> propagationInformationList = messageContent.getPropagationInformationList();

        try {

            LOG.info("RequestProcessor ->: propagationInformationList.size() -> "+(propagationInformationList != null ? propagationInformationList.size() : null));

            List<PropagationInformation> propagationInformationResponseList = new ArrayList<>();

            for (PropagationInformation propagationInformation : propagationInformationList) {

                try {

                    NodesCatalog nodesCatalog = getDaoFactory().getNodesCatalogDao().findById(propagationInformation.getId());

                    // if the version is minor than i have then i request for it
                    if (nodesCatalog.getVersion() < propagationInformation.getVersion())
                        propagationInformationResponseList.add(
                                new PropagationInformation(
                                        propagationInformation.getId(),
                                        nodesCatalog.getVersion()
                                )
                        );

                } catch (RecordNotFoundException recordNotFoundException) {

                    propagationInformationResponseList.add(
                            new PropagationInformation(
                                    propagationInformation.getId(),
                                    null
                            )
                    );
                }
            }

            if (!propagationInformationResponseList.isEmpty()) {

                NodesCatalogToPropagateResponse addNodeToCatalogResponse = new NodesCatalogToPropagateResponse(propagationInformationResponseList, NodesCatalogToPropagateResponse.STATUS.SUCCESS, null);
                Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.NODES_CATALOG_TO_PROPAGATE_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "There's no more information to exchange."));
            }

        } catch (Exception exception){

            try {

                LOG.info(FermatException.wrapException(exception).toString());
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Can't process NODES_CATALOG_TO_PROPAGATE_REQUEST. ||| "+ exception.getMessage()));

            } catch (Exception e) {
                LOG.info(FermatException.wrapException(e).toString());
            }

        }

    }

}

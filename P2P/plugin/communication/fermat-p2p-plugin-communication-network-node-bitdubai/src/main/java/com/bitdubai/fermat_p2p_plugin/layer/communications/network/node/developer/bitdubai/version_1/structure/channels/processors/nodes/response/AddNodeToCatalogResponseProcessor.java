package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.AddNodeToCatalogResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ConfigurationManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.HexadecimalConverter;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response.AddNodeToCatalogResponseProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class AddNodeToCatalogResponseProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(AddNodeToCatalogResponseProcessor.class));

    /**
     * Constructor
     */
    public AddNodeToCatalogResponseProcessor() {
        super(PackageType.ADD_NODE_TO_CATALOG_RESPONSE);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        try {

            AddNodeToCatalogResponse messageContent = AddNodeToCatalogResponse.parseContent(packageReceived.getContent());

            if (messageContent.getStatus() == MsgRespond.STATUS.SUCCESS){

                LOG.info("MsgRespond status "+messageContent.getStatus());
                ConfigurationManager.updateValue(ConfigurationManager.REGISTERED_IN_CATALOG, String.valueOf(Boolean.TRUE));
                ConfigurationManager.updateValue(ConfigurationManager.LAST_REGISTER_NODE_PROFILE, HexadecimalConverter.convertHexString(messageContent.getNodeProfileAdded().toJson().getBytes("UTF-8")));

            }else if (messageContent.getStatus() == MsgRespond.STATUS.FAIL) {

                LOG.info("MsgRespond status "+messageContent.getStatus());
                LOG.info("MsgRespond details "+messageContent.getDetails());

                if (messageContent.getAlreadyExists()){
                    ConfigurationManager.updateValue(ConfigurationManager.REGISTERED_IN_CATALOG, String.valueOf(Boolean.TRUE));
                    ConfigurationManager.updateValue(ConfigurationManager.LAST_REGISTER_NODE_PROFILE, HexadecimalConverter.convertHexString(messageContent.getNodeProfileAdded().toJson().getBytes("UTF-8")));
                }

            } else {

                LOG.info("MsgRespond status "+messageContent.getStatus());
                LOG.info("MsgRespond details "+messageContent.getDetails());
            }

            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Finish add node to catalog process"));

        } catch (Exception exception){

            try {

                LOG.error(exception);
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Can't process respond: "+ exception.getMessage()));

            } catch (Exception e) {
                LOG.error(e);
            }

        }
    }
}

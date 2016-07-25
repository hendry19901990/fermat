package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.NetworkNodePluginRoot;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors.ActorsCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ThumbnailUtil;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.sql.Timestamp;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.AddActorIntoCatalogProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_ACTOR_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class AddActorIntoCatalogProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(AddActorIntoCatalogProcessor.class));

    /**
     * Represent the nodeIdentity
     */
    private String nodeIdentity = ((NetworkNodePluginRoot) NodeContext.get(NodeContextItem.PLUGIN_ROOT)).getIdentity().getPublicKey();

    /**
     * Constructor
     */
    public AddActorIntoCatalogProcessor() {
        super(PackageType.CHECK_IN_ACTOR_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint fermatWebSocketChannelEndpoint) {

        LOG.info("Processing new package received "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

        ActorProfile actorProfile = (ActorProfile) messageContent.getProfileToRegister();

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            Timestamp currentMillis = new Timestamp(System.currentTimeMillis());

            ActorsCatalog actorCatalog = createActorsCatalog(actorProfile, currentMillis);

            /*
             * Validate if exist
             */
            if (getDaoFactory().getActorsCatalogDao().exists(actorProfile.getIdentityPublicKey())){

                LOG.info("Existing profile");

                boolean hasChanges = validateProfileChange(actorCatalog);

                LOG.info("hasChanges = "+hasChanges);

                if (hasChanges){

                    LOG.info("Updating profile");

                    /*
                     * Update the profile in the catalog
                     */
                    getDaoFactory().getActorsCatalogDao().update(actorCatalog, null, ActorCatalogUpdateTypes.UPDATE, ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

                } else {

                    getDaoFactory().getActorsCatalogDao().updateConnectionTime(actorCatalog, currentMillis.getTime(), ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);
                }

            } else {

                LOG.info("New Profile proceed to insert into catalog");

                /*
                 * Insert into the catalog
                 */
                getDaoFactory().getActorsCatalogDao().create(actorCatalog, 0, ActorCatalogUpdateTypes.ADD, ActorsCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);
            }

            LOG.info("Process finish");

        } catch (Exception exception){

            LOG.info(FermatException.wrapException(exception).toString());

        }
    }

    /**
     * Create a new row into the data base
     *
     * @param actorProfile
     * @throws IOException
     */
    private ActorsCatalog createActorsCatalog(ActorProfile actorProfile, Timestamp currentTimeStamp) throws IOException {

        /*
         * Create the actorsCatalog
         */
        ActorsCatalog actorsCatalog = new ActorsCatalog();
        actorsCatalog.setIdentityPublicKey(actorProfile.getIdentityPublicKey());
        actorsCatalog.setActorType(actorProfile.getActorType());
        actorsCatalog.setAlias(actorProfile.getAlias());
        actorsCatalog.setExtraData(actorProfile.getExtraData());
        actorsCatalog.setName(actorProfile.getName());
        actorsCatalog.setPhoto(actorProfile.getPhoto());

        if(actorProfile.getPhoto() != null && actorProfile.getPhoto().length > 0)
            actorsCatalog.setThumbnail(ThumbnailUtil.generateThumbnail(actorProfile.getPhoto(), "JPG"));
        else
            actorsCatalog.setThumbnail(null);

        actorsCatalog.setNodeIdentityPublicKey(nodeIdentity);
        actorsCatalog.setClientIdentityPublicKey(actorProfile.getClientIdentityPublicKey());
        actorsCatalog.setLastUpdateTime(currentTimeStamp);
        actorsCatalog.setLastConnection(currentTimeStamp);
        actorsCatalog.setHostedTimestamp(currentTimeStamp);

        //Validate if location are available
        if (actorProfile.getLocation() != null){
            actorsCatalog.setLastLocation(actorProfile.getLocation().getLatitude(), actorProfile.getLocation().getLongitude());
        }else{
            actorsCatalog.setLastLocation(0.0, 0.0);
        }

        return actorsCatalog;
    }

    /**
     * Validate if the profile register have changes
     *
     * @param actorCatalog
     * @return boolean
     * @throws CantReadRecordDataBaseException
     * @throws RecordNotFoundException
     */
    private boolean validateProfileChange(ActorsCatalog actorCatalog) throws CantReadRecordDataBaseException, RecordNotFoundException {

        ActorsCatalog actorsCatalogRegister = getDaoFactory().getActorsCatalogDao().findById(actorCatalog.getIdentityPublicKey());

        if (!actorsCatalogRegister.equals(actorCatalog)){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;

    }

}

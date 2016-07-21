package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.utils.DatabaseTransactionStatementPair;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantCreateTransactionStatementPairException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInActorRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_ACTOR_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInActorRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInActorRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInActorRequestProcessor() {
        super(PackageType.CHECK_IN_ACTOR_REQUEST);
    }


    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorProfile actorProfile = null;

        try {

            CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Obtain the profile of the actor
             */
            actorProfile = (ActorProfile) messageContent.getProfileToRegister();

            // create transaction for
            DatabaseTransaction databaseTransaction = getDaoFactory().getCheckedInProfilesDao().getNewTransaction();
            DatabaseTransactionStatementPair pair;

            /*
             * CheckedInActor into data base
             */
            pair = insertCheckedInActor(actorProfile);

            if(!getDaoFactory().getCheckedInProfilesDao().exists(actorProfile.getIdentityPublicKey())) {
                databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());
            }else {

                boolean hasChanges = validateProfileChange(actorProfile);

                if(hasChanges)
                    databaseTransaction.addRecordToUpdate(pair.getTable(), pair.getRecord());

            }

            /*
             * CheckedActorsHistory into data base
             */
            pair = insertCheckedActorsHistory(actorProfile);
            databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());

            databaseTransaction.execute();

            /*
             * If all ok, respond whit success message
             */
            CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), actorProfile.getIdentityPublicKey());

            channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_ACTOR_RESPONSE, destinationIdentityPublicKey);

            LOG.info("Registered new Actor = "+actorProfile.getName());

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), actorProfile.getIdentityPublicKey());
                channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_ACTOR_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }

    }

    /**
     * Create a new row into the data base
     *
     * @param actorProfile
     * @throws CantInsertRecordDataBaseException
     */
    private DatabaseTransactionStatementPair insertCheckedInActor(final ActorProfile actorProfile) throws CantCreateTransactionStatementPairException, CantReadRecordDataBaseException {

        CheckedInProfile checkedInProfile = new CheckedInProfile(
                actorProfile.getIdentityPublicKey(),
                actorProfile.getClientIdentityPublicKey(),
                actorProfile.getActorType(),
                ProfileTypes.ACTOR,
                actorProfile.getLocation()
        );


          if(!getDaoFactory().getCheckedInProfilesDao().exists(actorProfile.getIdentityPublicKey()))
             return getDaoFactory().getCheckedInProfilesDao().createInsertTransactionStatementPair(checkedInProfile);
          else
              return getDaoFactory().getCheckedInProfilesDao().createUpdateTransactionStatementPair(checkedInProfile);

    }

    /**
     * Create a new row into the data base
     *
     * @param actorProfile
     * @throws CantCreateTransactionStatementPairException
     */
    private DatabaseTransactionStatementPair insertCheckedActorsHistory(ActorProfile actorProfile) throws CantCreateTransactionStatementPairException {

        /*
         * Create the CheckedActorsHistory
         */
        ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(
                actorProfile.getIdentityPublicKey(),
                actorProfile.getActorType(),
                ProfileTypes.ACTOR,
                RegistrationType.CHECK_IN,
                RegistrationResult.SUCCESS,
                null
        );

        /*
         * Save into the data base
         */
        return getDaoFactory().getRegistrationHistoryDao().createInsertTransactionStatementPair(profileRegistrationHistory);

    }

    /**
     * Validate if the profile register have changes
     *
     * @param actorProfile
     * @return boolean
     * @throws CantReadRecordDataBaseException
     * @throws RecordNotFoundException
     */
    // is this necessary ?
    private boolean validateProfileChange(ActorProfile actorProfile) throws CantReadRecordDataBaseException, RecordNotFoundException {

        CheckedInProfile checkedInProfile = new CheckedInProfile(
                actorProfile.getIdentityPublicKey(),
                actorProfile.getClientIdentityPublicKey(),
                actorProfile.getActorType(),
                ProfileTypes.ACTOR,
                actorProfile.getLocation()
        );

        CheckedInProfile actorsRegistered = getDaoFactory().getCheckedInProfilesDao().findById(actorProfile.getIdentityPublicKey());

        // todo change equals
        if (!actorsRegistered.equals(checkedInProfile)){
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }

    }

}

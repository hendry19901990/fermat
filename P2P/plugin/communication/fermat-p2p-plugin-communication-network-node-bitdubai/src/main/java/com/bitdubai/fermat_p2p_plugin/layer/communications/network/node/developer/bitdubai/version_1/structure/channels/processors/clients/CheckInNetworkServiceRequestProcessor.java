package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
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

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInNetworkServiceRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_NETWORK_SERVICE_REQUEST</code><p/>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInNetworkServiceRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInNetworkServiceRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInNetworkServiceRequestProcessor() {
        super(PackageType.CHECK_IN_NETWORK_SERVICE_REQUEST);
    }

    /**
     * (non-javadoc)
     *
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        NetworkServiceProfile networkServiceProfile = null;

        try {

            CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent.getProfileToRegister()), destinationIdentityPublicKey);

            /*
             * Validate if content type is the correct
             */
            if (messageContent.getMessageContentType() == MessageContentType.JSON) {

                /*
                 * Obtain the profile of the network service
                 */
                networkServiceProfile = (NetworkServiceProfile) messageContent.getProfileToRegister();

                // create transaction for
                DatabaseTransaction databaseTransaction = getDaoFactory().getCheckedInProfilesDao().getNewTransaction();
                DatabaseTransactionStatementPair pair;

                /*
                 * CheckedInNetworkService into data base
                 */
                pair = insertCheckedInNetworkService(networkServiceProfile);

                if (!getDaoFactory().getCheckedInProfilesDao().exists(networkServiceProfile.getIdentityPublicKey())) {

                    databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());

                } else {

                    if(validateProfileChange(networkServiceProfile))
                        databaseTransaction.addRecordToUpdate(pair.getTable(), pair.getRecord());
                }

                /*
                 * CheckedInNetworkServiceHistory into data base
                 */
                pair = insertCheckedInNetworkServiceHistory(networkServiceProfile);
                databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());

                databaseTransaction.execute();

                /*
                 * If all ok, respond whit success message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), networkServiceProfile.getIdentityPublicKey());
                Package packageRespond = Package.createInstance(respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_NETWORK_SERVICE_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            }

        } catch (Exception exception) {

            try {

                LOG.error(exception.getMessage());

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), networkServiceProfile.getIdentityPublicKey());
                Package packageRespond = Package.createInstance(respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_CLIENT_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

    }

    /**
     * Create a new row into the data base
     *
     * @param networkServiceProfile
     * @throws CantCreateTransactionStatementPairException
     */
    private DatabaseTransactionStatementPair insertCheckedInNetworkService(NetworkServiceProfile networkServiceProfile) throws CantCreateTransactionStatementPairException, CantReadRecordDataBaseException {

        CheckedInProfile checkedInProfile = new CheckedInProfile(
                networkServiceProfile.getIdentityPublicKey(),
                networkServiceProfile.getClientIdentityPublicKey(),
                networkServiceProfile.getNetworkServiceType().getCode(),
                ProfileTypes.NETWORK_SERVICE,
                networkServiceProfile.getLocation()
        );

        if (!getDaoFactory().getCheckedInProfilesDao().exists(networkServiceProfile.getIdentityPublicKey()))
            return getDaoFactory().getCheckedInProfilesDao().createInsertTransactionStatementPair(checkedInProfile);
        else
            return getDaoFactory().getCheckedInProfilesDao().createUpdateTransactionStatementPair(checkedInProfile);

    }

    /**
     * Create a new row into the data base
     *
     * @param networkServiceProfile
     * @throws CantInsertRecordDataBaseException
     */
    private DatabaseTransactionStatementPair insertCheckedInNetworkServiceHistory(NetworkServiceProfile networkServiceProfile) throws CantCreateTransactionStatementPairException {

        /*
         * Create the ProfileRegistrationHistory
         */
        ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(
                networkServiceProfile.getIdentityPublicKey(),
                networkServiceProfile.getNetworkServiceType().getCode(),
                ProfileTypes.NETWORK_SERVICE,
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
     * @param networkServiceProfile
     * @return boolean
     * @throws Exception
     */
    private boolean validateProfileChange(NetworkServiceProfile networkServiceProfile) throws Exception {

        /*
         * Create the CheckedInProfile
         */
        CheckedInProfile checkedInProfile = new CheckedInProfile(
                networkServiceProfile.getIdentityPublicKey(),
                networkServiceProfile.getIdentityPublicKey(),
                networkServiceProfile.getNetworkServiceType().getCode(),
                ProfileTypes.NETWORK_SERVICE,
                networkServiceProfile.getLocation()
        );

        CheckedInProfile checkedInProfileRegistered = getDaoFactory().getCheckedInProfilesDao().findById(networkServiceProfile.getIdentityPublicKey());

        // TODO CHANGE EQUALS HERE -> ONLY VALIDATE IDENTITY PUBLIC KEY
        if (!checkedInProfileRegistered.equals(checkedInProfile)){
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }

    }

}

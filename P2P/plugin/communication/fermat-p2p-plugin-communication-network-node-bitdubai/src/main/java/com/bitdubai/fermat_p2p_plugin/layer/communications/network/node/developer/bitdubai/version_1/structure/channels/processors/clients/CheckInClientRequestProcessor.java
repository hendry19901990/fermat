package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;
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

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInClientRequestProcessor</code>
 * process all packages received the type <code>PackageType.CHECK_IN_CLIENT_REQUEST</code><p/>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInClientRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckInClientRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckInClientRequestProcessor() {
        super(PackageType.CHECK_IN_CLIENT_REQUEST);
    }

    /**
     * (non-javadoc)
     *
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);


        ClientProfile clientProfile = null;

        try {

            CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Obtain the profile of the client
             */
            clientProfile = (ClientProfile) messageContent.getProfileToRegister();

            /*
             * CheckedInProfile into data base
             */
            checkInClient(clientProfile);

            /*
             * If all ok, respond whit success message
             */
            CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), clientProfile.getIdentityPublicKey());

            channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_CLIENT_RESPONSE, destinationIdentityPublicKey);

        } catch (Exception exception) {

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(
                        CheckInProfileMsjRespond.STATUS.FAIL,
                        exception.getLocalizedMessage(),
                        null
                );

                channel.sendPackage(session, respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_CLIENT_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    /**
     * Create a new row into the data base
     *
     * @param profile of the client
     *
     * @throws CantInsertRecordDataBaseException if something goes wrong.
     */
    private void checkInClient(final ClientProfile profile) throws Exception {

        // create transaction for
        DatabaseTransaction databaseTransaction = getDaoFactory().getCheckedInProfilesDao().getNewTransaction();
        DatabaseTransactionStatementPair pair;

        /*
         * Create the CheckedInProfile
         */
        CheckedInProfile checkedInProfile = new CheckedInProfile(
                profile.getIdentityPublicKey(),
                profile.getIdentityPublicKey(),
                profile.getDeviceType(),
                ProfileTypes.CLIENT,
                profile.getLocation()
        );

        if(!getDaoFactory().getCheckedInProfilesDao().exists(checkedInProfile.getIdentityPublicKey())) {
           /*
            * Save into the data base
            */
            pair = getDaoFactory().getCheckedInProfilesDao().createInsertTransactionStatementPair(checkedInProfile);
            databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());
        } else {

            if(validateProfileChange(profile)) {

                pair = getDaoFactory().getCheckedInProfilesDao().createUpdateTransactionStatementPair(checkedInProfile);
                databaseTransaction.addRecordToUpdate(pair.getTable(), pair.getRecord());
            }

        }

        /*
         * ProfileRegistrationHistory into data base
         */
        pair = insertClientsRegistrationHistory(profile, RegistrationResult.SUCCESS, null);
        databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());

        databaseTransaction.execute();

    }

    /**
     * Create a new row into the data base
     *
     * @param profile of the client.
     * @param result  of the registration.
     * @param detail  of the registration.
     *
     * @throws CantCreateTransactionStatementPairException if something goes wrong.
     */
    private DatabaseTransactionStatementPair insertClientsRegistrationHistory(final ClientProfile      profile,
                                                  final RegistrationResult result ,
                                                  final String             detail ) throws CantCreateTransactionStatementPairException {

        /*
         * Create the ProfileRegistrationHistory
         */
        ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(
                profile.getIdentityPublicKey(),
                profile.getDeviceType(),
                ProfileTypes.CLIENT,
                RegistrationType.CHECK_IN,
                result,
                detail
        );

        /*
         * Save into the data base
         */
        return getDaoFactory().getRegistrationHistoryDao().createInsertTransactionStatementPair(profileRegistrationHistory);
    }

    /**
     * Validate if the profile register have changes
     *
     * @param profile
     * @return boolean
     * @throws Exception
     */
    private boolean validateProfileChange(ClientProfile profile) throws  Exception {

        /*
         * Create the CheckedInProfile
         */
        CheckedInProfile checkedInProfile = new CheckedInProfile(
                profile.getIdentityPublicKey(),
                profile.getIdentityPublicKey(),
                profile.getDeviceType(),
                ProfileTypes.CLIENT,
                profile.getLocation()
        );

        CheckedInProfile checkedInProfileRegistered = getDaoFactory().getCheckedInProfilesDao().findById(profile.getIdentityPublicKey());

        // TODO CHANGE EQUALS HERE -> ONLY VALIDATE IDENTITY PUBLIC KEY
        if (!checkedInProfileRegistered.equals(checkedInProfile)){
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }

    }

}

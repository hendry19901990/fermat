package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckOutProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckOutProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.utils.DatabaseTransactionStatementPair;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantCreateTransactionStatementPairException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutActorRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_OUT_ACTOR_REQUEST</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 29/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckOutActorRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(CheckOutActorRequestProcessor.class));

    /**
     * Constructor
     */
    public CheckOutActorRequestProcessor() {
        super(PackageType.CHECK_OUT_ACTOR_REQUEST);
    }


    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        CheckOutProfileMsgRequest messageContent = CheckOutProfileMsgRequest.parseContent(packageReceived.getContent());
        String profileIdentity = messageContent.getIdentityPublicKey();

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), destinationIdentityPublicKey);

            /*
             * Validate if exist
             */
            if (getDaoFactory().getCheckedInProfilesDao().exists(profileIdentity)){

                // create transaction for
                DatabaseTransaction databaseTransaction = getDaoFactory().getCheckedInProfilesDao().getNewTransaction();
                DatabaseTransactionStatementPair pair;

                /*
                 * Delete from data base
                 */
                pair = deleteCheckedInActor(profileIdentity);
                databaseTransaction.addRecordToDelete(pair.getTable(), pair.getRecord());

                /*
                 * CheckedActorsHistory into data base
                 */
                pair = insertCheckedActorsHistory(profileIdentity);
                databaseTransaction.addRecordToInsert(pair.getTable(), pair.getRecord());

                databaseTransaction.execute();

                /*
                 * If all ok, respond whit success message
                 */
                CheckOutProfileMsjRespond checkOutProfileMsjRespond = new CheckOutProfileMsjRespond(CheckOutProfileMsjRespond.STATUS.SUCCESS, CheckOutProfileMsjRespond.STATUS.SUCCESS.toString(), profileIdentity);
                channel.sendPackage(session, checkOutProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_OUT_ACTOR_RESPONSE, destinationIdentityPublicKey);

            }else{

                throw new Exception("The Profile is no actually check in");
            }

        }catch (Exception exception){

            try {

                LOG.error(exception);

                /*
                 * Respond whit fail message
                 */
                CheckOutProfileMsjRespond checkOutProfileMsjRespond = new CheckOutProfileMsjRespond(CheckOutProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), profileIdentity);
                channel.sendPackage(session, checkOutProfileMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_OUT_ACTOR_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }

        }

    }

    /**
     * Delete a row from the data base
     *
     * @param profileIdentity
     * @throws CantInsertRecordDataBaseException
     */
    private DatabaseTransactionStatementPair deleteCheckedInActor(String profileIdentity) throws CantCreateTransactionStatementPairException {

        /*
         * validate if exists
         */
        return getDaoFactory().getCheckedInProfilesDao().createDeleteTransactionStatementPair(profileIdentity);

    }

    /**
     * Create a new row into the data base
     *
     * @param profileIdentity
     * @throws CantInsertRecordDataBaseException
     */
    private DatabaseTransactionStatementPair insertCheckedActorsHistory(String profileIdentity) throws CantCreateTransactionStatementPairException {

        /*
         * Create the ProfileRegistrationHistory
         */

        ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(
                profileIdentity,
                null,
                ProfileTypes.ACTOR,
                RegistrationType.CHECK_OUT,
                null,
                null
        );
        /*
         * Save into the data base
         */
        return getDaoFactory().getRegistrationHistoryDao().createInsertTransactionStatementPair(profileRegistrationHistory);

    }

}

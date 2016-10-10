package com.bitdubai.fermat_ccp_api.layer.actor.intra_user.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_ccp_api.layer.actor.Actor;

import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantAcceptIntraWalletUserException;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCreateNewDeveloperException;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraUserException;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraUsersConnectedStateException;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantUpdateIntraWalletUserException;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.exceptions.RequestAlreadySendException;

import java.util.List;

/**
 * The interface <code>IntraWalletUserIdentityManager</code>
 * defines the methods to administrate the intra users,
 */
public interface IntraWalletUserActorManager extends FermatManager {

    /**
     * The method <code>askIntraWalletUserForAcceptance</code> registers a new intra user in the list
     * managed by this plugin with ContactState PENDING_REMOTELY_ACCEPTANCE until the other intra user
     * accepts the connection request sent also by this method.
     *
     * @param intraWalletUserIdentityToLinkPublicKey The public key of the intra user sending the connection request.
     * @param intraWalletUserToAddName               The name of the intra user to add
     * @param intraUserPhrase                        The phrase of the intra user to add
     * @param intraWalletUserToAddPublicKey          The public key of the intra user to add
     * @param profileImage                           The profile image that the intra user has
     *
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCreateIntraWalletUserException if something goes wrong.
     */
    void askIntraWalletUserForAcceptance(String intraWalletUserIdentityToLinkPublicKey,
                                         String intraWalletUserToAddName              ,
                                         String intraUserPhrase                       ,
                                         String intraWalletUserToAddPublicKey         ,
                                         byte[] profileImage  ,
                                         String city,
                                         String country) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCreateIntraWalletUserException,RequestAlreadySendException;


    /**
     * The method <code>acceptIntraWalletUser</code> takes the information of a connection request, accepts
     * the request and adds the intra user to the list managed by this plugin with ContactState CONTACT.
     *
     * @param intraUserLoggedInPublicKey The public key of the intra user sending the connection request.
     * @param intraUserToAddPublicKey    The public key of the intra user to add
     * @throws CantAcceptIntraWalletUserException
     */
    void acceptIntraWalletUser(String intraUserLoggedInPublicKey, String intraUserToAddPublicKey) throws CantAcceptIntraWalletUserException;


    /**
     * The method <code>denyConnection</code> rejects a connection request from another intra user
     *
     * @param intraUserLoggedInPublicKey The public key of the intra user identity that is the receptor of the request
     * @param intraUserToRejectPublicKey The public key of the intra user that sent the request
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantDenyConnectionException
     */
    void denyConnection(String intraUserLoggedInPublicKey, String intraUserToRejectPublicKey) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantDenyConnectionException;

    /**
     * The method <code>disconnectIntraWalletUser</code> disconnect an intra user from the connections registry
     * @param intraUserLoggedInPublicKey The public key of the intra user identity that is the receptor of the request
     * @param intraUserToDisconnectPublicKey The public key of the intra user to disconnect as connection
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantDisconnectIntraWalletUserException
     */
    void disconnectIntraWalletUser(String intraUserLoggedInPublicKey, String intraUserToDisconnectPublicKey) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantDisconnectIntraWalletUserException;


    void receivingIntraWalletUserRequestConnection(String intraUserLoggedInPublicKey, String intraUserToAddName, String intraUserPhrase,String intraUserToAddPublicKey, byte[] profileImage,String city, String country) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCreateIntraWalletUserException;

    /**
     * The method <code>updateIntraWalletUserdata</code> update all intra user data
     * @param intraUserToUpdatePublicKey The public key of the intra user to update
     * @param intraUserName the name of intra user to update
     * @param intraUserPhrase The intra user phrase to update
     * @param profileImage The profile image of intra user to update
     * @param city The city of intra user to update
     * @param country  The country of intra user to update
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCancelIntraWalletUserException
     */

    void updateIntraWalletUserdata(String intraUserToUpdatePublicKey,String intraUserName,String intraUserPhrase, byte[] profileImage,String city, String country) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantUpdateIntraWalletUserException;


    /**
     * The method <code>cancelIntraWalletUser</code> cancels an intra user from the connections registry
     * @param intraUserLoggedInPublicKey The public key of the intra user identity that is the receptor of the request
     * @param intraUserToCancelPublicKey The public key of the intra user to cancel as connection
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCancelIntraWalletUserException
     */
    void cancelIntraWalletUser(String intraUserLoggedInPublicKey, String intraUserToCancelPublicKey) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCancelIntraWalletUserException;

    /**
     * The method <code>getAllIntraWalletUsers</code> shows the list of all intra users that are connections of the logged in one.
     *
     * @param intraUserLoggedInPublicKey the public key of the intra user logged in
     * @return the list of intra users the logged in intra user has as connections.
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException
     */
    List<IntraWalletUserActor> getAllIntraWalletUsers(String intraUserLoggedInPublicKey, int max, int offset) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException;

    /**
     * The method <code>getAllIntraWalletUsersByLocation</code> shows the list of all intra users that are connections of the logged in one if de location match.
     *
     * @param intraUserLoggedInPublicKey the public key of the intra user logged in
     * @return the list of intra users the logged in intra user has as connections.
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException
     */
    List<IntraWalletUserActor> getAllIntraWalletUsersbyLocation(String intraUserLoggedInPublicKey, int max, int offset, String country, String city) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException;

    /**
     * The method <code>getAcceptedIntraWalletUsers</code> shows the list of all intra users that are accepted for the logged in one.
     *
     * @param intraUserLoggedInPublicKey
     * @return
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException
     */
    List<IntraWalletUserActor> getConnectedIntraWalletUsers(String intraUserLoggedInPublicKey) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException;

    /**
     * The method <code>getWaitingYourAcceptanceIntraWalletUsers</code> shows the list of all intra users
     * that sent a connection request and are waiting for the acceptance of the logged in one.
     *
     * @param intraUserLoggedInPublicKey the public key of the intra user logged in
     * @return the list of intra users the logged in intra user has as connections.
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException
     */
    List<IntraWalletUserActor> getWaitingYourAcceptanceIntraWalletUsers(String intraUserLoggedInPublicKey, int max, int offset) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException;

    /**
     * The method <code>getWaitingTheirAcceptanceIntraWalletUsers</code> shows the list of all intra users
     * that the logged in one has sent connections request to and have not been answered yet..
     *
     * @param intraUserLoggedInPublicKey the public key of the intra user logged in
     * @return the list of intra users the logged in intra user has as connections.
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException
     */
    List<IntraWalletUserActor> getWaitingTheirAcceptanceIntraWalletUsers(String intraUserLoggedInPublicKey, int max, int offset) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantGetIntraWalletUsersException;


    /**
     *
     * @param actorName
     * @param photo
     * @return
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCreateIntraUserException
     */

    Actor createActor(String intraUserLoggedInPublicKey, String actorName, byte[] photo) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantCreateIntraUserException;


    /**
     *
     * @param actorPublicKey
     * @return
     */
    Actor getActorByPublicKey(String intraUserLoggedInPublicKey, String actorPublicKey) throws CantGetIntraUserException, com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.IntraUserNotFoundException;


    /**
     *
     * @param actorPublicKey
     * @param photo
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantSetPhotoException
     * @throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.IntraUserNotFoundException
     */
    void setPhoto(String actorPublicKey, byte[] photo) throws com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantSetPhotoException, com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.IntraUserNotFoundException;

    boolean isActorConnected(String publicKey) throws CantCreateNewDeveloperException;

    /**
     *The method <code>CantGetIntraUsersConnectedStateException</code> intra user get connection status
     *
     * @param intraUserConnectedPublicKey
     * @return ConnectionState object
     * @throws CantGetIntraUsersConnectedStateException
     */
    ConnectionState getIntraUsersConnectionStatus(String intraUserConnectedPublicKey) throws CantGetIntraUsersConnectedStateException;

    /**
     *The method <code>getLastNotification</code> get the last notification received by actor public key
     * @param intraUserConnectedPublicKey
     * @return IntraWalletUserActor notification object
     * @throws CantGetIntraUserException
     */
    IntraWalletUserActor getLastNotification(String intraUserConnectedPublicKey) throws CantGetIntraUserException;

}

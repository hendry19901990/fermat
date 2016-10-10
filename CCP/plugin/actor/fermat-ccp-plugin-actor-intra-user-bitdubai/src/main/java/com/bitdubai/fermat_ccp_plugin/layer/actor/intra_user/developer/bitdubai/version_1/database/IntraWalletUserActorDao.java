package com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_ccp_api.layer.actor.Actor;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.exceptions.CantUpdateIntraWalletUserException;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.interfaces.IntraWalletUserActor;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.exceptions.RequestAlreadySendException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantAddPendingIntraWalletUserException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantGetIntraWalletUserActorException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantGetIntraWalletUserActorProfileImageException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantGetIntraWalletUsersListException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantInitializeIntraWalletUserActorDatabaseException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantPersistProfileImageException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.exceptions.CantUpdateConnectionException;
import com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.structure.IntraUserActorRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.database.IntraWalletUserActorDao</code>
 * has all methods related with database access.<p/>
 * <p/>
 * <p/>
 * Created by natalia on 12/08/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class IntraWalletUserActorDao {

    private static final String PROFILE_IMAGE_DIRECTORY_NAME   = DeviceDirectory.LOCAL_USERS.getName() + "/CCP/intraWalletUser";
    private static final String PROFILE_IMAGE_FILE_NAME_PREFIX = "profileImage";

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final PluginFileSystem     pluginFileSystem    ;
    private final UUID                 pluginId            ;

    public IntraWalletUserActorDao(final PluginDatabaseSystem pluginDatabaseSystem,
                                   final PluginFileSystem pluginFileSystem,
                                   final UUID pluginId) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem     = pluginFileSystem    ;
        this.pluginId             = pluginId            ;
    }

    private Database database;

    public void initializeDatabase() throws CantInitializeIntraWalletUserActorDatabaseException {

        try {

            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_DATABASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            throw new CantInitializeIntraWalletUserActorDatabaseException(cantOpenDatabaseException, "", "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (DatabaseNotFoundException e) {

            IntraWalletUserActorDatabaseFactory databaseFactory = new IntraWalletUserActorDatabaseFactory(pluginDatabaseSystem);

            try {

                database = databaseFactory.createDatabase(this.pluginId, IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_DATABASE_NAME);

            } catch (CantCreateDatabaseException cantCreateDatabaseException) {

                throw new CantInitializeIntraWalletUserActorDatabaseException(cantCreateDatabaseException, "", "There is a problem and i cannot create the database.");
            }
        } catch (Exception e) {
            throw new CantInitializeIntraWalletUserActorDatabaseException(e, "", "Exception not handled by the plugin, there is a unknown problem and i cannot open the database.");
        }
    }

    public void createNewIntraWalletUser(String intraUserLoggedInPublicKey, String intraUserToAddName, String intraUserToAddPublicKey, byte[] profileImage, ConnectionState contactState, String phrase, String city, String country) throws CantAddPendingIntraWalletUserException,RequestAlreadySendException {

        try {

            DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME                , intraUserToAddPublicKey   , DatabaseFilterType.EQUAL);

            if (table.getCount() == 0){

                long milliseconds = System.currentTimeMillis();

                DatabaseTableRecord record = table.getEmptyRecord();

                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserToAddPublicKey);
                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME, intraUserToAddName);
                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, contactState.getCode());
                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey);
                record.setLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME, milliseconds);
                record.setLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_MODIFIED_DATE_COLUMN_NAME, milliseconds);
                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME, phrase);
                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME, city);
                record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME, country);

                table.insertRecord(record);


                /**
                 * Persist profile image on a file
                 */
                if(profileImage != null && profileImage.length > 0)
                    persistNewUserProfileImage(intraUserToAddPublicKey, profileImage);

            } else {
                System.out.println("THE INTRA WALLET USER IS ALREADY CREATED IN THE DB, I have to update it");

                updateConnectionStateAndData(table, intraUserToAddPublicKey, intraUserToAddName, profileImage, contactState,phrase,city,country);
            }

        } catch (CantInsertRecordException e) {

            throw new CantAddPendingIntraWalletUserException("CAN'T INSERT INTRA USER", e, "", "Cant create new intra user, insert database problems.");


        } catch (CantPersistProfileImageException e) {
            throw new CantAddPendingIntraWalletUserException("CAN'T INSERT INTRA USER", e, "", "Cant Persist Profile Image.");

        } catch (Exception e) {
            throw new CantAddPendingIntraWalletUserException("CAN'T INSERT INTRA USER", FermatException.wrapException(e), "", "Cant get if intra user exist.");
        }



    }

    public void updateIntraWalletUserdata(final String intraUserToUpdatePublicKey,
                                          final String intraUserName,
                                          final String intraUserPhrase,
                                          final byte[] profileImage,
                                          final String city,
                                          final String country) throws CantUpdateIntraWalletUserException{
        try {

            /**Get the Table**/
            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            /**Filter by keys**/
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME,intraUserToUpdatePublicKey,DatabaseFilterType.EQUAL);

            /**Get a Record to set Data **/
            DatabaseTableRecord record = table.getEmptyRecord();

            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME,intraUserName);
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME,intraUserPhrase);
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME,city);
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME,country);

            //only connections users
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, ConnectionState.CONNECTED.getCode());

            if(table.numRecords() > 0){
                table.updateRecord(record);

                /**
                 * Persist profile image on a file
                 */
                if(profileImage != null && profileImage.length > 0)
                    persistNewUserProfileImage(intraUserToUpdatePublicKey, profileImage);
            }



        }catch (Exception e){
            throw new CantUpdateIntraWalletUserException("CAN'T INSERT INTRA USER", e, "", "Cant Update intra user, insert database problems.");

        }

    }


    public void updateConnectionState(final String          intraUserLoggedInPublicKey,
                                      final String          intraUserToAddPublicKey   ,
                                      final ConnectionState contactState              ) throws CantUpdateConnectionException {

        try {

            /**
             * 1) Get the table.
             */
            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            // 2) set filter by keys.
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserToAddPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);

            long milliseconds = System.currentTimeMillis();

            // 3) Get a record to set data
            DatabaseTableRecord record =  table.getEmptyRecord();

            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, contactState.getCode());
            record.setLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_MODIFIED_DATE_COLUMN_NAME, milliseconds);
            record.setLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME, milliseconds);

            table.updateRecord(record);

        }  catch (CantUpdateRecordException e) {

            throw new CantUpdateConnectionException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantUpdateConnectionException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get developer identity list, unknown failure.");
        }
    }


    public void updateConnectionStateAndData(final DatabaseTable table,
                                             final String          intraUserToAddPublicKey   ,
                                             String intraUserToAddName,
                                             byte[] profileImage,
                                             final ConnectionState contactState,
                                             String phrase,
                                             String city,
                                             String country) throws CantUpdateConnectionException {

        try {

            long milliseconds = System.currentTimeMillis();

            // 3) Get a record to set data
            DatabaseTableRecord record =  table.getEmptyRecord();

            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, contactState.getCode());
            record.setLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_MODIFIED_DATE_COLUMN_NAME, milliseconds);
            record.setLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME, milliseconds);
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME, intraUserToAddName);

            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME, phrase);
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME, city);
            record.setStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME, country);

            table.updateRecord(record);

            if(profileImage!=null && profileImage.length > 0) persistNewUserProfileImage(intraUserToAddPublicKey, profileImage);

        }  catch (CantUpdateRecordException e) {

            throw new CantUpdateConnectionException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantUpdateConnectionException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get developer identity list, unknown failure.");
        }
    }


    public List<IntraWalletUserActor> getAllConnectedIntraWalletUsers(String intraUserLoggedInPublicKey, int max, int offset) throws CantGetIntraWalletUsersListException {

        // Get Intra Users identities list.
        try {

            final ConnectionState connectedState = ConnectionState.CONNECTED;

            /**
             * 1) Get the table.
             */
            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            // 2) Find all Intra Users.
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, connectedState.getCode(), DatabaseFilterType.EQUAL);

            table.setFilterOffSet(String.valueOf(offset));
            table.setFilterTop(String.valueOf(max));

            table.loadToMemory();

            List<IntraWalletUserActor> list = new ArrayList<>(); // Intra User Actor list.

            // 3) Get Intra Users Recorod.
            for (DatabaseTableRecord record : table.getRecords()) {

                byte[] image;
                try {
                    image = getIntraUserProfileImagePrivateKey(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME));
                } catch(FileNotFoundException e) {
                    image = new  byte[0];
                }

                // Add records to list.
                list.add(new com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.structure.IntraWalletUserActor(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME),
                        image,
                        record.getLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME),
                        ConnectionState.getByCode(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME)),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME)));
            }

            // Return the list values.
            return list;

        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");
            //} catch (CantGetIntraWalletUserActorProfileImageException e) {

            // Failure unknown.
            //     throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Can't get profile ImageMiddleware.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get Instra User Actor list, unknown failure.");
        }


    }

    public List<IntraWalletUserActor> getAllConnectedIntraWalletUsersByLocation(String intraUserLoggedInPublicKey, int max, int offset, String country, String city) throws CantGetIntraWalletUsersListException {

        // Get Intra Users identities list.
        try {

            final ConnectionState connectedState = ConnectionState.CONNECTED;

            /**
             * 1) Get the table.
             */
            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            // 2) Find all Intra Users.

            /* this case compare city selected agains city of actors,
             */
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME, city, DatabaseFilterType.LIKE);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, connectedState.getCode(), DatabaseFilterType.EQUAL);

            table.setFilterOffSet(String.valueOf(offset));
            table.setFilterTop(String.valueOf(max));

            /* this case compare city selected agains contry of actors,
             *  because the error in which gps invert city and contry...
             */
            if(table.getCount() == 0){
                table.clearAllFilters();
                table.setFilterOffSet(String.valueOf(offset));
                table.setFilterTop(String.valueOf(max));

                table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME, city, DatabaseFilterType.LIKE);
                table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);
                table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, connectedState.getCode(), DatabaseFilterType.EQUAL);

                  /* this case compare country selected agains contry of actors,
                   */
                if(table.getCount() == 0){
                    table.clearAllFilters();
                    table.setFilterOffSet(String.valueOf(offset));
                    table.setFilterTop(String.valueOf(max));

                    table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME, country, DatabaseFilterType.LIKE);
                    table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);
                    table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, connectedState.getCode(), DatabaseFilterType.EQUAL);

                    /* this case compare contry selected agains city of actors,
                     *  because the error in which gps invert city and contry...
                     */
                    if(table.getCount() == 0){
                        table.clearAllFilters();
                        table.setFilterOffSet(String.valueOf(offset));
                        table.setFilterTop(String.valueOf(max));

                        table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME, country, DatabaseFilterType.LIKE);
                        table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraUserLoggedInPublicKey, DatabaseFilterType.EQUAL);
                        table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, connectedState.getCode(), DatabaseFilterType.EQUAL);
                    }
                }
            }

            table.loadToMemory();

            List<IntraWalletUserActor> list = new ArrayList<>(); // Intra User Actor list.

            // 3) Get Intra Users Recorod.
            for (DatabaseTableRecord record : table.getRecords()) {

                byte[] image;
                try {
                    image = getIntraUserProfileImagePrivateKey(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME));
                } catch(FileNotFoundException e) {
                    image = new  byte[0];
                }

                // Add records to list.
                list.add(new com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.structure.IntraWalletUserActor(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME),
                        image,
                        record.getLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME),
                        ConnectionState.getByCode(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME)),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME)));
            }

            // Return the list values.
            return list;

        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");
            //} catch (CantGetIntraWalletUserActorProfileImageException e) {
            // Failure unknown.
            //     throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Can't get profile ImageMiddleware.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get Instra User Actor list, unknown failure.");
        }


    }


    // return an Actor related with a wallet
    public IntraWalletUserActor getIntraUserConnectedInfo(String  intraUserConnectedPublicKey ) throws CantGetIntraWalletUsersListException {


        try {

            IntraWalletUserActor intraWalletUserActor = null;

            /**
             * 1) Get the table.
             */
            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            // 2) Find all Intra Users.
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserConnectedPublicKey, DatabaseFilterType.EQUAL);

            table.loadToMemory();

            // 3) Get Intra Users Recorod.
            for (DatabaseTableRecord record : table.getRecords()) {

                byte[] image;
                try {
                    image = getIntraUserProfileImagePrivateKey(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME));
                } catch(FileNotFoundException e) {
                    image = new  byte[0];
                }

                intraWalletUserActor =  new com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.structure.IntraWalletUserActor(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME),
                        image,
                        record.getLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME),
                        ConnectionState.getByCode(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME)),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME) ,
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME));
            }

            return intraWalletUserActor;


        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get Instra User Actor list, unknown failure.");
        }

    }


    public IntraWalletUserActor getLastNotification(String  intraUserConnectedPublicKey ) throws CantGetIntraWalletUsersListException {


        try {

            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            IntraWalletUserActor intraWalletUserActor = null;


            // 2) Find all Intra Users.
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserConnectedPublicKey, DatabaseFilterType.EQUAL);
            table.setFilterTop("1");
            table.addFilterOrder(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME, DatabaseFilterOrder.DESCENDING);

            table.loadToMemory();

            // 3) Get Intra Users Recorod.
            for (DatabaseTableRecord record : table.getRecords()) {

                byte[] image;
                try {
                    image = getIntraUserProfileImagePrivateKey(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME));
                } catch(FileNotFoundException e) {
                    image = new  byte[0];
                }

                intraWalletUserActor =  new com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.structure.IntraWalletUserActor(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME),
                        image,
                        record.getLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME),
                        ConnectionState.getByCode(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME)),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME));
            }

            return intraWalletUserActor;


        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get Instra User Actor list, unknown failure.");
        }

    }


    public Actor getIntraUserActorByPublicKey(String          walletPublicKey,
                                              String          intraUserPublicKey ) throws CantGetIntraWalletUserActorException {

        try {

            Actor intraWalletUserActor = null;

            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            // 2) Find all Intra Users.
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, walletPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserPublicKey, DatabaseFilterType.EQUAL);


            table.loadToMemory();

            // 3) Get Intra Users Record.
            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.

                byte[] image;
                try {
                    image = getIntraUserProfileImagePrivateKey(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME));
                } catch(FileNotFoundException e) {
                    image = new  byte[0];
                }

                intraWalletUserActor =   new IntraUserActorRecord(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME),"",
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME), image);
            }

            return intraWalletUserActor;

        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetIntraWalletUserActorException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUserActorException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get Instra User Actor, unknown failure.");
        }


    }


    public List<IntraWalletUserActor> getAllConnectedIntraWalletUsers(final String intraActorSelectedPublicKey,
                                                                      final ConnectionState contactState,
                                                                      final int max,
                                                                      final int offset) throws CantGetIntraWalletUsersListException {


        // Setup method.
        List<IntraWalletUserActor> list = new ArrayList<>(); // Intra User Actor list.

        // Get Intra Users identities list.
        try {

            DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);

            // 2) Find  Intra Users by state.
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, intraActorSelectedPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, contactState.getCode(), DatabaseFilterType.EQUAL);

            table.setFilterOffSet(String.valueOf(offset));
            table.setFilterTop(String.valueOf(max));

            table.loadToMemory();

            // 3) Get Intra Users Recorod.
            for (DatabaseTableRecord record : table.getRecords()) {

                byte[] image;
                try {
                    image = getIntraUserProfileImagePrivateKey(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME));
                } catch(FileNotFoundException e) {
                    image = new  byte[0];
                }


                // Add records to list.
                list.add(new com.bitdubai.fermat_ccp_plugin.layer.actor.intra_user.developer.bitdubai.version_1.structure.IntraWalletUserActor(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_NAME_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME),
                        image,
                        record.getLongValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_REGISTRATION_DATE_COLUMN_NAME),
                        ConnectionState.getByCode(record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME)),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PHRASE_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CITY_COLUMN_NAME),
                        record.getStringValue(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_COUNTRY_COLUMN_NAME)));
            }

        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");
            //} catch (CantGetIntraWalletUserActorProfileImageException e) {
            // Failure unknown.
            //   throw new CantGetIntraWalletUsersListException(e.getMessage(), e, "Intra User Actor", "Can't get profile ImageMiddleware.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUsersListException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant get Intra User Actor list, unknown failure.");
        }


        // Return the list values.
        return list;
    }

    /**
     * Private Methods
     */

    private void deleteUserProfileImage(String publicKey, byte[] profileImage) throws CantPersistProfileImageException,FileNotFoundException {

        try {

            this.pluginFileSystem.deleteBinaryFile(pluginId,
                    PROFILE_IMAGE_DIRECTORY_NAME,
                    buildProfileImageFileName(publicKey),
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

        }catch (FileNotFoundException e){
            throw new FileNotFoundException("File not found",e);
        } catch (CantCreateFileException e) {

            throw new CantPersistProfileImageException(e, "Error Delete file.", null);
        } catch (Exception e) {

            throw new CantPersistProfileImageException(FermatException.wrapException(e), "", "");
        }
    }


    private void persistNewUserProfileImage(String publicKey, byte[] profileImage) throws CantPersistProfileImageException {

        try {

            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    PROFILE_IMAGE_DIRECTORY_NAME,
                    buildProfileImageFileName(publicKey),
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.setContent(profileImage);
            file.persistToMedia();

        } catch (CantPersistFileException e) {
            throw new CantPersistProfileImageException(e, "Error persist file.", null);

        } catch (CantCreateFileException e) {

            throw new CantPersistProfileImageException(e, "Error creating file.", null);
        } catch (Exception e) {

            throw new CantPersistProfileImageException(FermatException.wrapException(e), "", "");
        }
    }


    private byte[] getIntraUserProfileImagePrivateKey(final String publicKey) throws CantGetIntraWalletUserActorProfileImageException,FileNotFoundException {

        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    PROFILE_IMAGE_DIRECTORY_NAME,
                    buildProfileImageFileName(publicKey),
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.loadFromMedia();

            return file.getContent();

        } catch (CantLoadFileException e) {
            throw new CantGetIntraWalletUserActorProfileImageException(e, "Error loaded file.", null);

        } catch (FileNotFoundException | CantCreateFileException e) {

            throw new FileNotFoundException(e, "", null);
        } catch (Exception e) {

            throw new CantGetIntraWalletUserActorProfileImageException(FermatException.wrapException(e), "", "");
        }
    }

    private String buildProfileImageFileName(final String publicKey) {
        return PROFILE_IMAGE_FILE_NAME_PREFIX + "_" + publicKey;
    }

    /**
     * <p>Method that check if intra user public key exists.
     *
     * @param intraUserToAddPublicKey
     * @return boolean exists
     * @throws CantGetIntraWalletUserActorException
     */
    public boolean intraUserRequestExists(final String intraUserToAddPublicKey, ConnectionState connectionState) throws CantGetIntraWalletUserActorException {

        if (intraUserToAddPublicKey == null) {
            throw new CantGetIntraWalletUserActorException("intraUserToAddPublicKey null", "intraUserToAddPublicKey must not be null.");
        }

        try {

            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);



            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserToAddPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_CONTACT_STATE_COLUMN_NAME, connectionState.getCode(), DatabaseFilterType.EQUAL);

            return table.getCount() > 0;

        } catch (CantLoadTableToMemoryException em) {
            throw new CantGetIntraWalletUserActorException(em.getMessage(), em, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUserActorException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant check if actor public key exists, unknown failure.");
        }
    }

    public boolean intraUserRequestExists(final String intraUserToAddPublicKey) throws CantGetIntraWalletUserActorException {

        if (intraUserToAddPublicKey == null) {
            throw new CantGetIntraWalletUserActorException("intraUserToAddPublicKey null", "intraUserToAddPublicKey must not be null.");
        }

        try {

            final DatabaseTable table = this.database.getTable(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME);


            table.addStringFilter(IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_PUBLIC_KEY_COLUMN_NAME, intraUserToAddPublicKey, DatabaseFilterType.EQUAL);

            return table.getCount() > 0;

        } catch (CantLoadTableToMemoryException em) {
            throw new CantGetIntraWalletUserActorException(em.getMessage(), em, "Intra User Actor", "Cant load " + IntraWalletUserActorDatabaseConstants.INTRA_WALLET_USER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            throw new CantGetIntraWalletUserActorException(e.getMessage(), FermatException.wrapException(e), "Intra User Actor", "Cant check if actor public key exists, unknown failure.");
        }
    }

}

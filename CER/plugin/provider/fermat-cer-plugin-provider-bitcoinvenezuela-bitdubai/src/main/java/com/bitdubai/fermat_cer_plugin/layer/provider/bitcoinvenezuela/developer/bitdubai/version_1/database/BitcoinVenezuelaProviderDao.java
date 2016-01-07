package com.bitdubai.fermat_cer_plugin.layer.provider.bitcoinvenezuela.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.CurrencyPair;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.ExchangeRate;
import com.bitdubai.fermat_cer_api.all_definition.utils.ExchangeRateImpl;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantCreateExchangeRateException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetExchangeRateException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetProviderInfoException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantInitializeProviderInfoException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantSaveExchangeRateException;
import com.bitdubai.fermat_cer_plugin.layer.provider.bitcoinvenezuela.developer.bitdubai.version_1.exceptions.CantInitializeBitcoinVenezuelaProviderDatabaseException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alejandro Bicelis on 12/7/2015.
 */
public class BitcoinVenezuelaProviderDao {


    private final ErrorManager errorManager;
    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;

    private Database database;

    public BitcoinVenezuelaProviderDao(final PluginDatabaseSystem pluginDatabaseSystem, final UUID pluginId, final ErrorManager errorManager) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
        this.errorManager = errorManager;
    }


    public void initialize() throws CantInitializeBitcoinVenezuelaProviderDatabaseException {
        try {
            database = this.pluginDatabaseSystem.openDatabase(pluginId, pluginId.toString());
        } catch (DatabaseNotFoundException e) {
            BitcoinVenezuelaProviderDatabaseFactory databaseFactory = new BitcoinVenezuelaProviderDatabaseFactory(pluginDatabaseSystem);
            try {
                database = databaseFactory.createDatabase(pluginId, pluginId.toString());
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CER_PROVIDER_BITCOINVENEZUELA, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantCreateDatabaseException);
                throw new CantInitializeBitcoinVenezuelaProviderDatabaseException("Database could not be opened", cantCreateDatabaseException, "Database Name: " + BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME, "");
            }
        }catch (CantOpenDatabaseException cantOpenDatabaseException) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CER_PROVIDER_BITCOINVENEZUELA, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);
            throw new CantInitializeBitcoinVenezuelaProviderDatabaseException("Database could not be opened", cantOpenDatabaseException, "Database Name: " + BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME, "");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CER_PROVIDER_BITCOINVENEZUELA, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
            throw new CantInitializeBitcoinVenezuelaProviderDatabaseException("Database could not be opened", FermatException.wrapException(e), "Database Name: " + BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME, "");
        }
    }

    public void initializeProvider(String providerName) throws CantInitializeProviderInfoException {
        //Try to get info, if there's no info, populate.
        try{
            this.getProviderInfo();
        }catch (CantGetProviderInfoException e){
            this.populateProviderInfo(providerName);
        }
    }


    public void saveExchangeRate(ExchangeRate exchangeRate) throws CantSaveExchangeRateException {

        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME);
        DatabaseTableRecord newRecord = table.getEmptyRecord();
        constructRecordFromExchangeRate(newRecord, exchangeRate);
        try {
            table.insertRecord(newRecord);
        }catch (CantInsertRecordException e) {
            throw new CantSaveExchangeRateException(e.getMessage(), e, "BitcoinVenezuela provider plugin", "Cant save new record in table");
        }
    }

    public ExchangeRate getExchangeRateFromDate(CurrencyPair currencyPair, long timestamp) throws CantGetExchangeRateException
    {
        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME);

        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_FROM_CURRENCY_COLUMN_NAME, currencyPair.getFrom().getCode(), DatabaseFilterType.EQUAL);
        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TO_CURRENCY_COLUMN_NAME, currencyPair.getTo().getCode(), DatabaseFilterType.EQUAL);
        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TIMESTAMP_COLUMN_NAME, String.valueOf(timestamp), DatabaseFilterType.EQUAL);

        try {
            table.loadToMemory();
            List<DatabaseTableRecord> records = table.getRecords();
            if (records.size() > 0){
                DatabaseTableRecord record = records.get(0);
                return constructExchangeRateFromRecord(record);
            }
            else {
                throw new CantGetExchangeRateException(CantGetExchangeRateException.DEFAULT_MESSAGE, null, "Failed to get exchange rate for timestamp: " + timestamp, "Exchange Rate not found in database");
            }

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetExchangeRateException(CantGetExchangeRateException.DEFAULT_MESSAGE, e, "Failed to get exchange rate for timestamp: " + timestamp, "Couldn't load table to memory");
        }catch (CantCreateExchangeRateException e) {
            throw new CantGetExchangeRateException(CantGetExchangeRateException.DEFAULT_MESSAGE, e, "Failed to get exchange rate for timestamp: " + timestamp, "Couldn't create ExchangeRate object");
        }
    }


    public List<ExchangeRate> getQueriedExchangeRateHistory(CurrencyPair currencyPair) throws CantGetExchangeRateException
    {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME);

        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_FROM_CURRENCY_COLUMN_NAME, currencyPair.getFrom().getCode(), DatabaseFilterType.EQUAL);
        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TO_CURRENCY_COLUMN_NAME, currencyPair.getTo().getCode(), DatabaseFilterType.EQUAL);

        try {
            table.loadToMemory();

            for (DatabaseTableRecord record : table.getRecords()) {
                ExchangeRate exchangeRate = constructExchangeRateFromRecord(record);
                exchangeRateList.add(exchangeRate);
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetExchangeRateException(CantGetExchangeRateException.DEFAULT_MESSAGE, e, "Failed to get History for currencyPair: " + currencyPair.toString(), "Couldn't load table to memory");
        }catch (CantCreateExchangeRateException e) {
            throw new CantGetExchangeRateException(CantGetExchangeRateException.DEFAULT_MESSAGE, e, "Failed to get History for currencyPair: " + currencyPair.toString(), "Couldn't create ExchangeRate object");
        }

        return exchangeRateList;
    }

    public void updateExchangeRatesFromDates(CurrencyPair currencyPair, List<ExchangeRate> exchangeRates) throws CantSaveExchangeRateException
    {
        List<String> exchangeRateTimestampsInDatabase = new ArrayList<>();

        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME);
        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_FROM_CURRENCY_COLUMN_NAME, currencyPair.getFrom().getCode(), DatabaseFilterType.EQUAL);
        table.addStringFilter(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TO_CURRENCY_COLUMN_NAME, currencyPair.getTo().getCode(), DatabaseFilterType.EQUAL);

        try {
            table.loadToMemory();

            for (DatabaseTableRecord record : table.getRecords()) {
                String timestamp = record.getStringValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TIMESTAMP_COLUMN_NAME);
                exchangeRateTimestampsInDatabase.add(timestamp);
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantSaveExchangeRateException(CantSaveExchangeRateException.DEFAULT_MESSAGE, e, "Failed to get ExchangeRates in database for CurrencyPair: " + currencyPair.toString(), "Couldn't load table to memory");
        }

        for(ExchangeRate e : exchangeRates)
        {
            String currentTimestamp = String.valueOf(e.getTimestamp());
            if(!exchangeRateTimestampsInDatabase.contains(currentTimestamp)) {
                this.saveExchangeRate(e);
            }
        }

    }





    /* PROVIDER INFO GETTERS */
    public String getProviderName() throws CantGetProviderInfoException {
        DatabaseTableRecord record = this.getProviderInfo();
        return record.getStringValue(BitcoinVenezuelaProviderDatabaseConstants.PROVIDER_INFO_NAME_COLUMN_NAME);
    }

    public UUID getProviderId() throws CantGetProviderInfoException {
        DatabaseTableRecord record = this.getProviderInfo();
        return record.getUUIDValue(BitcoinVenezuelaProviderDatabaseConstants.PROVIDER_INFO_ID_COLUMN_NAME);
    }

    private DatabaseTableRecord getProviderInfo() throws CantGetProviderInfoException {
        List<DatabaseTableRecord> records;
        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.PROVIDER_INFO_TABLE_NAME);

        try{
            table.loadToMemory();
            records = table.getRecords();
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetProviderInfoException(CantGetProviderInfoException.DEFAULT_MESSAGE);
        }

        if (records.size() != 1)
            throw new CantGetProviderInfoException("Inconsistent number of fetched records (" + records.size() + "), should be 1.");

        return records.get(0);
    }
    private void populateProviderInfo(String providerName) throws CantInitializeProviderInfoException {
        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.PROVIDER_INFO_TABLE_NAME);
        DatabaseTableRecord newRecord = table.getEmptyRecord();

        newRecord.setUUIDValue(BitcoinVenezuelaProviderDatabaseConstants.PROVIDER_INFO_ID_COLUMN_NAME, UUID.randomUUID());
        newRecord.setStringValue(BitcoinVenezuelaProviderDatabaseConstants.PROVIDER_INFO_NAME_COLUMN_NAME, providerName);

        try {
            table.insertRecord(newRecord);
        }catch (CantInsertRecordException e) {
            throw new CantInitializeProviderInfoException(e.getMessage());
        }
    }



    /* INTERNAL HELPER FUNCTIONS */
    private DatabaseTableFilter getEmptyTableFilter() {
        return this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME).getEmptyTableFilter();
    }



    private List<DatabaseTableRecord> getRecordsByFilter(DatabaseTableFilter filter) throws CantLoadTableToMemoryException {
        DatabaseTable table = this.database.getTable(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME);

        if (filter != null)
            table.addStringFilter(filter.getColumn(), filter.getValue(), filter.getType());

        table.loadToMemory();
        return table.getRecords();
    }


    private void constructRecordFromExchangeRate(DatabaseTableRecord newRecord, ExchangeRate exchangeRate) {

        newRecord.setUUIDValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_ID_COLUMN_NAME, UUID.randomUUID());
        newRecord.setStringValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_FROM_CURRENCY_COLUMN_NAME, exchangeRate.getFromCurrency().getCode());
        newRecord.setStringValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TO_CURRENCY_COLUMN_NAME, exchangeRate.getToCurrency().getCode());
        newRecord.setDoubleValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_SALE_PRICE_COLUMN_NAME, exchangeRate.getSalePrice());
        newRecord.setDoubleValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_PURCHASE_PRICE_COLUMN_NAME, exchangeRate.getPurchasePrice());
        newRecord.setLongValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TIMESTAMP_COLUMN_NAME, exchangeRate.getTimestamp());

    }

    private ExchangeRate constructExchangeRateFromRecord(DatabaseTableRecord record) throws CantCreateExchangeRateException {

        UUID id = record.getUUIDValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_ID_COLUMN_NAME);
        double salePrice = record.getDoubleValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_SALE_PRICE_COLUMN_NAME);
        double purchasePrice = record.getDoubleValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_PURCHASE_PRICE_COLUMN_NAME);
        long timestamp = record.getLongValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TIMESTAMP_COLUMN_NAME);

        Currency fromCurrency;
        try {
            String fromCurrencyStr = record.getStringValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_FROM_CURRENCY_COLUMN_NAME);

            if(FiatCurrency.codeExists(fromCurrencyStr))
                fromCurrency = FiatCurrency.getByCode(fromCurrencyStr);
            else if(CryptoCurrency.codeExists(fromCurrencyStr))
                fromCurrency = CryptoCurrency.getByCode(fromCurrencyStr);
            else throw new InvalidParameterException();

        } catch (InvalidParameterException e) {
            throw new CantCreateExchangeRateException(e.getMessage(), e, "BitcoinVenezuela provider plugin", "Invalid From Currency value stored in table"
                    + BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME + " for id " + id);
        }

        Currency toCurrency;
        try {
            String toCurrencyStr = record.getStringValue(BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TO_CURRENCY_COLUMN_NAME);

            if(FiatCurrency.codeExists(toCurrencyStr))
                toCurrency = FiatCurrency.getByCode(toCurrencyStr);
            else if(CryptoCurrency.codeExists(toCurrencyStr))
                toCurrency = CryptoCurrency.getByCode(toCurrencyStr);
            else throw new InvalidParameterException();

        } catch (InvalidParameterException e) {
            throw new CantCreateExchangeRateException(e.getMessage(), e, "BitcoinVenezuela provider plugin", "Invalid To Currency value stored in table"
                    + BitcoinVenezuelaProviderDatabaseConstants.QUERY_HISTORY_TABLE_NAME + " for id " + id);
        }

        return new ExchangeRateImpl(fromCurrency, toCurrency, salePrice, purchasePrice, timestamp);
    }



}

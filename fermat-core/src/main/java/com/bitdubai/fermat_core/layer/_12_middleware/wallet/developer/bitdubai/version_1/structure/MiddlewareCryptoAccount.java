package com.bitdubai.fermat_core.layer._12_middleware.wallet.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer._12_middleware.wallet.CryptoAccount;
import com.bitdubai.fermat_api.layer._1_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer._1_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer._2_os.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer._2_os.database_system.DatabaseTableRecord;

import java.util.UUID;

/**
 * Created by ciencias on 2/15/15.
 */

public class MiddlewareCryptoAccount implements CryptoAccount{

    
    /**
     * MiddlewareCryptoAccount Interface member variables.
     */
    private DatabaseTable table;
    private DatabaseTableRecord record;
    private String labelColumName;
    private String nameColumName;

    /**
     * FiatAccount Interface member variables.
     */
    private UUID id;
    private String label ="";
    private String name ="";
    private long balance = 0;
    private CryptoCurrency cryptoCurrency;
    
    /**
     * Class constructor.
     */
    MiddlewareCryptoAccount (UUID id){
        this.id = id;
    }

    /**
     * MiddlewareCryptoAccount interface implementation.
     *
     * Note that the following methods are package-private.
     * * * * 
     */
    void setBalance(long balance){
        this.balance = balance;
    }

    void setCryptoCurrency(CryptoCurrency cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    void setTable (DatabaseTable table) {
        this.table = table;
    }

    void setRecord (DatabaseTableRecord record){
        this.record = record;
    }

    void setLabelColumName (String labelColumName){
        this.labelColumName = labelColumName;
    }

    void setNameColumName (String nameColumName){
        this.nameColumName = nameColumName;
    }
    
    /**
     * FiatAccount interface implementation.
     */
    public long getBalance() {
        return balance;
    }

    public CryptoCurrency getCryptoCurrency() {
        return cryptoCurrency;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label;
        this.record.setStringValue(this.labelColumName, this.label);
        this.table.updateRecord(this.record);
    }

    public void setName(String name) {
        this.name = name;
        this.record.setStringValue(this.nameColumName, this.name);
        this.table.updateRecord(this.record);
    }
}

package com.bitdubai.fermat_wpd_api.all_definition.events;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_wpd_api.all_definition.enums.EventType;

import java.util.UUID;

/**
 * Created by ciencias on 26.01.15.
 */
public class WalletCreatedEvent extends AbstractWPDEvent {

    private UUID walletId;
    private CryptoCurrency cryptoCurrency;
    private FiatCurrency fiatCurrency;

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public UUID getWalletId() {
        return this.walletId;
    }

    public void setCryptoCurrency(CryptoCurrency cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public CryptoCurrency getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setFiatCurrency(FiatCurrency fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public FiatCurrency getFiatCurrency() {
        return fiatCurrency;
    }

    public WalletCreatedEvent(EventType eventType) {
        super(eventType);
    }
}


package com.bitdubai.fermat_wpd_api.all_definition.events;

import com.bitdubai.fermat_wpd_api.all_definition.enums.EventType;

import java.util.UUID;

/**
 * Created by ciencias on 25.01.15.
 */
public class WalletWentOnlineEvent extends AbstractWPDEvent {

    private UUID walletId;

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public UUID getWalletId() {
        return this.walletId;
    }

    public WalletWentOnlineEvent(EventType eventType) {
        super(eventType);
    }


}


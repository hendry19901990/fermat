package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.identity;

import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;

import java.io.Serializable;

/**
 * ChatPreferenceSettings
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 29/02/16.
 * @version 1.0
 */
public class ChatIdentityPreferenceSettings implements FermatSettings, Serializable {

    private boolean isHomeTutorialDialogEnabled;

    public boolean isHomeTutorialDialogEnabled() {
        return isHomeTutorialDialogEnabled;
    }

    public void setIsHomeTutorialDialogEnabled(boolean isHomeTutorialDialogEnabled) {
        this.isHomeTutorialDialogEnabled = isHomeTutorialDialogEnabled;
    }

    @Override
    public void setIsPresentationHelpEnabled(boolean b) {
        isHomeTutorialDialogEnabled=b;
    }

}

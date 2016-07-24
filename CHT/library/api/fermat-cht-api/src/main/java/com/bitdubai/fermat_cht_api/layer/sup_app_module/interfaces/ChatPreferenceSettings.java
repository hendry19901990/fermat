package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;

import java.io.Serializable;
/**
 * ChatPreferenceSettings
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 29/02/16.
 * @version 1.0
 */
public class ChatPreferenceSettings implements FermatSettings, Serializable {
    private boolean isHomeTutorialDialogEnabled;
    private String localPublicKey;
    private PlatformComponentType localPlatformComponentType;
    private ChatActorCommunitySelectableIdentity identity;

    public boolean isHomeTutorialDialogEnabled() {
        return isHomeTutorialDialogEnabled;
    }

    public String getLocalPublicKey() {
        return localPublicKey;
    }

    public void setProfileSelected(String publicKey, PlatformComponentType localPlatformComponentType) {
        this.localPlatformComponentType=localPlatformComponentType;
        this.localPublicKey=publicKey;
    }

    public void setIdentitySelected(ChatActorCommunitySelectableIdentity identity) {
        this.identity=identity;
    }
    public ChatActorCommunitySelectableIdentity getIdentitySelected() {
        return identity;
    }

    public void setIsHomeTutorialDialogEnabled(boolean isHomeTutorialDialogEnabled) {
        this.isHomeTutorialDialogEnabled = isHomeTutorialDialogEnabled;
    }

    @Override
    public void setIsPresentationHelpEnabled(boolean b) {
        isHomeTutorialDialogEnabled=b;
    }


}

package com.bitdubai.wallet_platform_core.layer._10_middleware.app_runtime.developer.bitdubai.version_1.structure;

import com.bitdubai.wallet_platform_api.layer._10_middleware.app_runtime.LanguagePackage;
import com.bitdubai.wallet_platform_api.layer._1_definition.enums.Languages;
import com.bitdubai.wallet_platform_api.layer._4_user.User;

import java.util.Map;

/**
 * Created by ciencias on 2/14/15.
 */
public class RuntimeLanguagePackage implements LanguagePackage {
    
    User author;
    String name;
    Languages language;
    Map<String,String> translation;


    public void setAuthor(User author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(Languages language) {
        this.language = language;
    }

    public void setTranslation(Map<String, String> translation) {
        this.translation = translation;
    }


    /**
     * LanguagePackage Interface implementation.
     */

    public User getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public Languages getLanguage() {
        return language;
    }

    public Map<String, String> getTranslation() {
        return translation;
    }
}

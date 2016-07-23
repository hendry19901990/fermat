package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ProfileRegistrationHistory</code>
 * is the persistent class for the "PROFILES_REGISTRATION_HISTORY" database table.
 * <p/>
 *
 * Created by Leon Acosta (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ProfileRegistrationHistory extends AbstractBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

    private final UUID               uuid             ;
    private final String             identityPublicKey;
    private final String             deviceType       ;
    private final ProfileTypes       profileType      ;
    private final Timestamp          checkedTimestamp ;
    private final RegistrationType   type             ;
    private final RegistrationResult result           ;
    private final String             detail           ;

    public ProfileRegistrationHistory(final String             identityPublicKey,
                                      final String             deviceType       ,
                                      final ProfileTypes       profileType      ,
                                      final RegistrationType   type             ,
                                      final RegistrationResult result           ,
                                      final String             detail           ) {

        this.deviceType        = deviceType       ;
        this.profileType       = profileType      ;
        this.identityPublicKey = identityPublicKey;
        this.type              = type             ;
        this.result            = result           ;
        this.detail            = detail           ;

        this.uuid              = UUID.randomUUID();
        this.checkedTimestamp  = new Timestamp(System.currentTimeMillis());
    }

    public ProfileRegistrationHistory(final UUID               uuid             ,
                                      final String             identityPublicKey,
                                      final String             deviceType       ,
                                      final ProfileTypes       profileType      ,
                                      final Timestamp          checkedTimestamp ,
                                      final RegistrationType   type             ,
                                      final RegistrationResult result           ,
                                      final String             detail           ) {

        this.uuid              = uuid             ;
        this.checkedTimestamp  = checkedTimestamp ;
        this.deviceType        = deviceType       ;
        this.profileType       = profileType      ;
        this.identityPublicKey = identityPublicKey;
        this.type              = type             ;
        this.result            = result           ;
        this.detail            = detail           ;
    }

    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public ProfileTypes getProfileType() {
        return profileType;
    }

    public Timestamp getCheckedTimestamp() {
        return checkedTimestamp;
    }

    public RegistrationType getType() {
        return type;
    }

    public RegistrationResult getResult() {
        return result;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String getId() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof ProfileRegistrationHistory)) return false;
        ProfileRegistrationHistory that = (ProfileRegistrationHistory) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ProfileRegistrationHistory{" +
                "uuid=" + uuid +
                ", identityPublicKey='" + identityPublicKey + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", profileType=" + profileType +
                ", checkedTimestamp=" + checkedTimestamp +
                ", type=" + type +
                ", result=" + result +
                ", detail='" + detail + '\'' +
                '}';
    }
}
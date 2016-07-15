package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInProfile</code>
 * is the persistent class for the "CHECKED_IN_PROFILES" database table.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class CheckedInProfile extends AbstractBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String identityPublicKey;

	private final String clientPublicKey;

	private final String information;

	private final ProfileTypes profileType;

    private final Location location;

	private final Timestamp checkedInTimestamp;

	/**
	 * Constructor with checked in timestamp by default.
	 */
	public CheckedInProfile(final String           identityPublicKey ,
							final String           clientPublicKey   ,
							final String           information       ,
							final ProfileTypes     profileType       ,
                            final Location         location          ) {

		this.identityPublicKey  = identityPublicKey ;
		this.clientPublicKey    = clientPublicKey   ;
		this.information         = information        ;
		this.profileType        = profileType       ;
        this.location           = location          ;

		this.checkedInTimestamp = new Timestamp(System.currentTimeMillis());
	}

	public CheckedInProfile(final String           identityPublicKey ,
							final String           clientPublicKey   ,
							final String           information       ,
							final ProfileTypes     profileType       ,
                            final Location         location          ,
							final Timestamp        checkedInTimestamp) {

		this.identityPublicKey  = identityPublicKey ;
		this.clientPublicKey    = clientPublicKey   ;
		this.information        = information       ;
		this.profileType        = profileType       ;
        this.location           = location          ;
		this.checkedInTimestamp = checkedInTimestamp;
	}

	public String getIdentityPublicKey() {
		return identityPublicKey;
	}

	public String getClientPublicKey() {
		return clientPublicKey;
	}

	public String getInformation() {
		return information;
	}

	public ProfileTypes getProfileType() {
		return profileType;
	}

    public Location getLocation() {
        return location;
    }

    public Timestamp getCheckedInTimestamp() {
		return checkedInTimestamp;
	}

	@Override
    public String getId() {
        return identityPublicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckedInProfile)) return false;
        CheckedInProfile that = (CheckedInProfile) o;
        return Objects.equals(getIdentityPublicKey(), that.getIdentityPublicKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentityPublicKey());
    }

    @Override
    public String toString() {
        return "CheckedInProfile{" +
                "identityPublicKey='" + identityPublicKey + '\'' +
                ", clientPublicKey='" + clientPublicKey + '\'' +
                ", information='" + information + '\'' +
                ", profileType=" + profileType +
                ", location=" + location +
                ", checkedInTimestamp=" + checkedInTimestamp +
                '}';
    }
}
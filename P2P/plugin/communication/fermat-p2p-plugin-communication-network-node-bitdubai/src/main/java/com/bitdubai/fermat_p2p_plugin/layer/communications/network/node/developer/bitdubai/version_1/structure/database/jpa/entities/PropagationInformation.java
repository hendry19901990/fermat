package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.PropagationInformation</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class PropagationInformation implements AbstractBaseEntity<Long> {

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Represent the identityPublicKey
     */
    @NotNull
    private String identityPublicKey;

    /**
     * Represent the version
     */
    @NotNull
    private Integer version;

    /**
     * Represent the pendingPropagations
     */
    @NotNull
    private transient Integer pendingPropagations;

    /**
     * Represent the triedToPropagateTimes
     */
    @NotNull
    private transient Integer triedToPropagateTimes;

    /**
     * Constructor
     */
    public PropagationInformation(){
        super();
        this.identityPublicKey = "";
        this.version = 0;
        this.pendingPropagations = 0;
        this.triedToPropagateTimes = 0;
    }

    /**
     * Constructor with parameters
     * @param identityPublicKey
     * @param version
     */
    public PropagationInformation(final String identityPublicKey,  final Integer version) {
        super();
        this.identityPublicKey = identityPublicKey;
        this.version = version;
    }

    /**
     * Constructor with parameters
     * @param identityPublicKey
     * @param version
     * @param pendingPropagations
     * @param triedToPropagateTimes
     */
    public PropagationInformation(final String identityPublicKey, final Integer version, final Integer pendingPropagations, final Integer triedToPropagateTimes) {
        super();
        this.identityPublicKey = identityPublicKey;
        this.version = version;
        this.pendingPropagations = pendingPropagations;
        this.triedToPropagateTimes = triedToPropagateTimes;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the IdentityPublicKey
     * @return identityPublicKey
     */
    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    /**
     * Set the IdentityPublicKey
     * @param identityPublicKey
     */
    public void setIdentityPublicKey(String identityPublicKey) {
        this.identityPublicKey = identityPublicKey;
    }

    /**
     * Get the Version
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Set the Version
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the PendingPropagations
     * @return pendingPropagations
     */
    public Integer getPendingPropagations() {
        return pendingPropagations;
    }

    /**
     * Set the PendingPropagations
     * @param pendingPropagations
     */
    public void setPendingPropagations(Integer pendingPropagations) {
        this.pendingPropagations = pendingPropagations;
    }

    /**
     * Get the TriedToPropagateTimes
     * @return triedToPropagateTimes
     */
    public Integer getTriedToPropagateTimes() {
        return triedToPropagateTimes;
    }

    /**
     * Set the TriedToPropagateTimes
     * @param triedToPropagateTimes
     */
    public void setTriedToPropagateTimes(Integer triedToPropagateTimes) {
        this.triedToPropagateTimes = triedToPropagateTimes;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropagationInformation)) return false;

        PropagationInformation that = (PropagationInformation) o;

        return getId().equals(that.getId());

    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@toString()
     */
    @Override
    public String toString() {
        return "PropagationInformation{" +
                "id=" + id +
                ", identityPublicKey='" + identityPublicKey + '\'' +
                ", version=" + version +
                ", pendingPropagations=" + pendingPropagations +
                ", triedToPropagateTimes=" + triedToPropagateTimes +
                '}';
    }
}
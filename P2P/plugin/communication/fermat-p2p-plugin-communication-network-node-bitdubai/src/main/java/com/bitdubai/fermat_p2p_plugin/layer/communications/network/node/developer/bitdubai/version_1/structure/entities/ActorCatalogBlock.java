package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities;

import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.BlockStatus;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.BlockTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.BlockStatus</code>
 * is the persistent class for the "ACTOR_CATALOG_BLOCK" database table.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogBlock extends AbstractBaseEntity implements Serializable {

	 private static final long serialVersionUID = 1L;

	 private final String hashId;

	 private final String nodePublicKey;

	 private final String signature;

	 private final Timestamp generationTime;

	 private       BlockStatus status;

	 private final BlockTypes type;

	 private       Integer pendingPropagations;

	 public ActorCatalogBlock(final String      hashId             ,
							  final String      nodePublicKey      ,
							  final String      signature          ,
							  final Timestamp   generationTime     ,
							  final BlockStatus status             ,
							  final BlockTypes  type               ,
							  final Integer     pendingPropagations) {

		 this.hashId              = hashId             ;
		 this.nodePublicKey       = nodePublicKey      ;
		 this.signature           = signature          ;
		 this.generationTime      = generationTime     ;
		 this.status              = status             ;
		 this.type                = type               ;
		 this.pendingPropagations = pendingPropagations;
	 }

	 public String getNodePublicKey() {
		 return nodePublicKey;
	 }

	 public String getSignature() {
		 return signature;
	 }

	 public Timestamp getGenerationTime() {
		 return generationTime;
	 }

	 public BlockStatus getStatus() {
		 return status;
	 }

	 public BlockTypes getType() {
		 return type;
	 }

	 public Integer getPendingPropagations() {
		 return pendingPropagations;
	 }

	 @Override
	 public String getId() {

		 return hashId;
	 }

     public void setStatus(BlockStatus status) {
         this.status = status;
     }

     public void setPendingPropagations(Integer pendingPropagations) {
         this.pendingPropagations = pendingPropagations;
     }

     @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
		 if (!(o instanceof ActorCatalogBlock)) return false;
		 ActorCatalogBlock that = (ActorCatalogBlock) o;
		 return Objects.equals(getId(), that.getId());
	 }

	 @Override
	 public int hashCode() {
		 return Objects.hash(getId());
	 }

	 @Override
	 public String toString() {
		 return "ActorCatalogBlock{" +
				 "hashId='" + hashId + '\'' +
				 ", nodePublicKey='" + nodePublicKey + '\'' +
				 ", signature='" + signature + '\'' +
				 ", generationTime=" + generationTime +
				 ", status=" + status +
				 ", type=" + type +
				 ", pendingPropagations=" + pendingPropagations +
				 '}';
	 }
 }
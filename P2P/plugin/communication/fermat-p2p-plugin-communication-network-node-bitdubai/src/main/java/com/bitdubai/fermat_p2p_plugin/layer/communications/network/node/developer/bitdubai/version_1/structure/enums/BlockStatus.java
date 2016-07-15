package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * The enum <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.BlockStatus</code>
 * contains all the possible results of a registration in the network node.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public enum BlockStatus implements FermatEnum {

    /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */

    CREATED    ("CR"),
    PROPAGATED ("PR"),

    ;

    private final String code;

    BlockStatus(final String code) {

        this.code = code;
    }

    public static BlockStatus getByCode(final String code) throws InvalidParameterException {

        for (BlockStatus status : BlockStatus.values()) {
            if(status.getCode().equals(code))
                return status;
        }

        throw new InvalidParameterException(
                "Code Received: " + code,
                "The received code is not valid for the BlockStatus enum."
        );
    }

    @Override
    public String getCode() {

        return this.code;
    }
}
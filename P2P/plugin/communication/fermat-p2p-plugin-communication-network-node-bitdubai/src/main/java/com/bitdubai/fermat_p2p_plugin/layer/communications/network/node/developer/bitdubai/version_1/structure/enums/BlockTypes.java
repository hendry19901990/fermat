package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * The enum <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.BlockTypes</code>
 * contains all the possible results of a registration in the network node.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 15/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public enum BlockTypes implements FermatEnum {

    /**
     * To make the code more readable, please keep the elements in the Enum sorted alphabetically.
     */

    INFORMATION ("IN"),
    RELEVANT    ("RE"),

    ;

    private final String code;

    BlockTypes(final String code) {

        this.code = code;
    }

    public static BlockTypes getByCode(final String code) throws InvalidParameterException {

        for (BlockTypes type : BlockTypes.values()) {
            if(type.getCode().equals(code))
                return type;
        }

        throw new InvalidParameterException(
                "Code Received: " + code,
                "The received code is not valid for the BlockTypes enum."
        );
    }

    @Override
    public String getCode() {

        return this.code;
    }
}
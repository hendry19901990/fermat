package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util;

import com.bitdubai.fermat_api.layer.all_definition.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.BlockUtils</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 19/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class BlockUtils {

    public static String getHash(final String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(s.getBytes(Charset.forName("UTF-8")));
        byte[] digest = md.digest();
        byte[] encoded = Base64.encode(digest, 1);

        String encryptedString = new String(encoded, "UTF-8");
        encryptedString = encryptedString.replace("/","");
        return encryptedString.replace("\n","");
    }
}
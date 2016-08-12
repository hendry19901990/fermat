package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Class <code>HardcodeConstants</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 07/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class HardcodeConstants {

    /**
     * Represent the SERVER IP DEFAULT
     */
    public static final String SERVER_IP_DEFAULT = "193.234.224.198";

    /**
     * Represent the WS_PROTOCOL
     */
    public static final String WS_PROTOCOL = "ws://";

    /**
     * Represent the DEFAULT_PORT
     */
    public static final int DEFAULT_PORT = 8080;

    /**
     * Represents the value of DISABLE_CLIENT
     */
    public static final Boolean DISABLE_CLIENT = Boolean.TRUE;

    /**
     * Represents the value of ENABLE_CLIENT
     */
    public static final Boolean ENABLE_CLIENT = Boolean.FALSE;

    /*
     * Represent the photo to set the actors
     */
    public static byte [] photoActor(){

        File file = new File("avatar.png");

        System.out.println("file exists= " + file.exists());

        if(file.exists()){

            byte[] imageInByte = new byte[0];

            try {

                BufferedImage originalImage = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(originalImage, "png", baos);
                baos.flush();
                imageInByte = baos.toByteArray();
                baos.close();

            } catch (IOException e) {

            }

            return imageInByte;

        }else{

            return new byte[]{};
        }

    }

}

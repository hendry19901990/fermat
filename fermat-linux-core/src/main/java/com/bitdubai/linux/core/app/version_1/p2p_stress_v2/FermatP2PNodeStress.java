/*
* @#FermatP2PNodeStress.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.linux.core.app.version_1.p2p_stress_v2;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatPluginsEnum;
import com.bitdubai.fermat_core.FermatSystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPlatform;
import com.bitdubai.fermat_osa_linux_core.OSAPlatform;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientManager;
import com.bitdubai.linux.core.app.version_1.structure.context.FermatLinuxContext;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>com.bitdubai.linux.core.app.version_1.p2p_stress_v2.FermatP2PNodeStress</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 30/07/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class FermatP2PNodeStress extends AbstractJavaSamplerClient implements Serializable {

    private static final long serialVersionUID = 1;
    private static final String SERVER_IP_DEFAULT = "193.234.224.198";
    private static final String WS_PROTOCOL = "ws://";
    private static final int DEFAULT_PORT = 8080;

    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

        try {

            if(!isNodeRunning())
                throw new Exception("The Node is down right Now!");

            FermatLinuxContext fermatLinuxContext = FermatLinuxContext.getInstance();
            FermatSystem fermatSystem = FermatSystem.getInstance();
            fermatSystem.start(fermatLinuxContext, new OSAPlatform());
            fermatSystem.startAndGetPluginVersion(new PluginVersionReference(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.NETWORK_CLIENT, Developers.BITDUBAI, new Version()));
            NetworkClientManager clientManager = (NetworkClientManager)fermatSystem.startAndGetPluginVersion(new PluginVersionReference(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.NETWORK_CLIENT, Developers.BITDUBAI, new Version()));

            TimeUnit.MINUTES.sleep(2);
            clientManager.stop();

            sampleResult.setSuccessful(true);
            sampleResult.setResponseCode("200");

            StringBuffer stringBufferResult = new StringBuffer();
            stringBufferResult.append("totalOfProfileSendToCheckin: ").append(clientManager.getConnection().getTotalOfProfileSendToCheckin());
            stringBufferResult.append("\ntotalOfProfileSuccessChecked: ").append(clientManager.getConnection().getTotalOfProfileSuccessChecked());
            stringBufferResult.append(" totalOfProfileFailureToCheckin: ").append(clientManager.getConnection().getTotalOfProfileFailureToCheckin());
            stringBufferResult.append("\n\ntotalOfMessagesSents: ").append(clientManager.getConnection().getTotalOfMessagesSents());
            stringBufferResult.append("\ntotalOfMessagesSentsSuccessfully: ").append(clientManager.getConnection().getTotalOfMessagesSentsSuccessfully());
            stringBufferResult.append(" totalOfMessagesSentsFails: ").append(clientManager.getConnection().getTotalOfMessagesSentsFails());
            sampleResult.setSamplerData(stringBufferResult.toString());

            fermatLinuxContext = null;
            fermatSystem = null;
            clientManager = null;
        }
        catch (Exception e) {
            sampleResult.setSuccessful(false);
            sampleResult.setResponseCode("500");
            sampleResult.setResponseMessage("Exception: " + e);
        }
        return sampleResult;
    }

    public void setupTest(JavaSamplerContext context) {
    }

    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("URI", WS_PROTOCOL + SERVER_IP_DEFAULT + ":" + DEFAULT_PORT + "/fermat/ws/client-channel");
        return params;
    }

    public static void main(String[] args) {

        try {
            FermatP2PNodeStress fer = new FermatP2PNodeStress();
            SampleResult result = fer.runTest(null);
            System.out.println(result.getSamplerData());
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean isNodeRunning() {

        try {

            String URLName = "http://" + SERVER_IP_DEFAULT + ":8080/fermat/rest/api/v1/network/actors";

            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setConnectTimeout(500);
            con.setRequestMethod("HEAD");

            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

}

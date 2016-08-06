/*
* @#FermatP2PNodeStress.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.linux.core.app.version_1.p2p_stress_v2;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.NetworkClientCommunicationPluginRoot;

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
    private static final String SERVER_IP_DEFAULT = NetworkClientCommunicationPluginRoot.NODE_SERVER_IP_DEFAULT;

    private NetworkClientCommunicationPluginRoot clientManager;

    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

        try {

            System.out.println("IPNODO " + javaSamplerContext.getParameter("ipnodo"));

            String ipNodoToConnecting = (javaSamplerContext.containsParameter("ipnodo")) ? javaSamplerContext.getParameter("ipnodo") : SERVER_IP_DEFAULT;

            if(ipNodoToConnecting.equalsIgnoreCase("XXX"))
                ipNodoToConnecting = SERVER_IP_DEFAULT;

//            if(!isNodeRunning())
//                throw new Exception("The Node is down right Now!");


            clientManager = new NetworkClientCommunicationPluginRoot(ipNodoToConnecting);
            clientManager.start();

            TimeUnit.MINUTES.sleep(15);
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
        params.addArgument("NODODEFAULT", SERVER_IP_DEFAULT);
        params.addArgument("ipnodo", "XXX");
        return params;
    }

    public void teardownTest(JavaSamplerContext context){

        try {

            if (clientManager != null && clientManager.isStarted())
                clientManager.stop();

        }catch (Exception e){

        }

    }

    public static void main(String[] args) {

        try {

            FermatP2PNodeStress fer = new FermatP2PNodeStress();

            Arguments params = new Arguments();
            params.addArgument("NODODEFAULT", SERVER_IP_DEFAULT);
            params.addArgument("ipnodo", "XXX");

            JavaSamplerContext context = new JavaSamplerContext(params);

            SampleResult result = fer.runTest(context);
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

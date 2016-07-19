package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.util.ActorOnlineHelper</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 19/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorOnlineHelper {

    public static ActorOnlineInformation isActorOnlineInTheSameNode(final ActorProfile actorProfile, final String nodeUrl) {

        try {
            URL url = new URL("http://" + nodeUrl + "/fermat/rest/api/v1/online/component/actor/" + actorProfile.getIdentityPublicKey());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String respond = reader.readLine();

            if (conn.getResponseCode() == 200 && respond != null && respond.contains("success")) {

                JsonObject respondJsonObject = (JsonObject) GsonProvider.getJsonParser().parse(respond.trim());

                return new ActorOnlineInformation(respondJsonObject.get("isOnline").getAsBoolean(), respondJsonObject.get("sameNode").getAsBoolean());


            } else {
                return new ActorOnlineInformation(false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ActorOnlineInformation(false, false);
        }
    }
}

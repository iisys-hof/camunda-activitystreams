package de.hofuniversity.iisys.camunda.activitystreams.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.camunda.bpm.engine.impl.util.json.JSONObject;

/**
 * Utility extracting additional information from an Apache Shindig instance.
 */
public class ShindigUtil
{
    private static final String PERSON_FRAG = "social/rest/people/";
    
    private final String fShindigUrl;
    
    public ShindigUtil(String shindigUrl)
    {
        fShindigUrl = shindigUrl;
    }
    
    public String getDisplayName(String userId) throws Exception
    {
        String displayName = null;
        
        String url = fShindigUrl + PERSON_FRAG + userId;
        JSONObject response = new JSONObject(sendRequest(url));
        
        displayName = response.optString("displayName");
        
        return displayName;
    }
    
    private String sendRequest(String url) throws Exception
    {
        //set parameters for sending
        URL shindigUrl = new URL(url);
        final HttpURLConnection connection =
            (HttpURLConnection) shindigUrl.openConnection();
        
        connection.setRequestMethod("GET");
        
        //read reply
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
        
        String line = reader.readLine();
        String response = "";
        while(line != null)
        {
            response += line;
            line = reader.readLine();
        }
        
        reader.close();
        
        return response;
    }
}

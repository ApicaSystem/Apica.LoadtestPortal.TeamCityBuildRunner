/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author andras.nemes
 */
public class CommonLtpWebApiService
{

    public WebRequestOutcome makeWebRequest(URI uri, String username, String password)
    {
        WebRequestOutcome outcome = new WebRequestOutcome();
        outcome.setExceptionMessage("");
        try
        {
            String auth = getBase64Credentials(username, password);
            URL presetUrl = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) presetUrl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic ".concat(auth));
            con.setConnectTimeout(60000);
            int responseCode = con.getResponseCode();
            outcome.setHttpResponseCode(responseCode);
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                outcome.setRawResponseContent(response.toString());
                outcome.setWebRequestSuccessful(true);
                try
                {
                    in.close();
                } catch (Exception ex)
                {
                }
            } else
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }

                outcome.setExceptionMessage(response.toString());
                try
                {
                    in.close();
                } catch (Exception ex)
                {
                }
            }
        } catch (Exception ex)
        {
            outcome.setExceptionMessage(ex.getMessage());
            outcome.setRawResponseContent("");
        }
        return outcome;
    }
    
    private String getBase64Credentials(String username, String password)
    {
        String auth = username.concat(":").concat(password);        
        return new String(Base64.encodeBase64(auth.getBytes()));
    }
}

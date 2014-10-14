package com.apicasystem.ltpselfservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class CommonLtpWebApiService
{

    public WebRequestOutcome makeWebRequest(URI uri)
    {
        WebRequestOutcome outcome = new WebRequestOutcome();
        outcome.setExceptionMessage("");
        try
        {
            URL presetUrl = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) presetUrl.openConnection();
            con.setRequestMethod("GET");
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
                } catch (IOException ex)
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
                } catch (IOException ex)
                {
                }
            }
        } catch (IOException ex)
        {
            outcome.setExceptionMessage(ex.getMessage());
            outcome.setRawResponseContent("");
        }
        return outcome;
    }
}

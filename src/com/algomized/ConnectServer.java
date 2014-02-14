package com.algomized;

import android.app.Activity;
import android.os.Bundle;
import java.io.BufferedReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStreamReader;
import android.util.Log;

public class ConnectServer
{
    String LOG_TAG = "ConnectServer";
    String username = "";
    int age = 0;
	  HttpClient client;
    String result = "";
     
    public ConnectServer(String session_id)
    {
      //retrieve details of user using the provided session_id
      //details include user name, age etc and put into a user object
      
    BufferedReader in = null;
		// TODO Auto-generated method stub
		try
		{

			client = new DefaultHttpClient();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("session_id", session_id));

			HttpPost request = new HttpPost(Constants.CONNECTION_STRING + "authenticateUser.java");
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()), 8);
			result = in.readLine();
			if (!result.equals("null"))
			{
				  ObjectMapper mapper = new ObjectMapper();
          User user = mapper.readValue(result,User.class);
			}
			else
			{
				result = "0";
			}

			in.close();
			client.getConnectionManager().shutdown();
		}

		catch (Exception exc)
		{
			Log.e(LOG_TAG, "Error converting result " + exc.toString());
		}
    }

}
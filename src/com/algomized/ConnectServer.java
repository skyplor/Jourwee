package com.algomized;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import java.io.BufferedReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;

import android.util.Log;

public class ConnectServer
{
	String LOG_TAG = "ConnectServer";
	String username = "";
	int age = 0;
	HttpClient client;
	String result = "";
	User user;
	private CookieStore mCookie = null;
	private Object mLock = new Object();

	public ConnectServer(String session_id, Context context)
	{
		// retrieve details of user using the provided session_id
		// details include user name, age etc and put into a user object

		// client = this.getHttpClient();
		final AlgoCookieStore cs = new AlgoCookieStore(context);
		final CookieManager cm = new CookieManager(cs, CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cm);
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				BufferedReader in = null;
				try
				{
					client = new DefaultHttpClient();

					User user_login = new User();
					user_login.setUsername("skypay");
					user_login.setPassword("abcdef");
					ObjectMapper mapper_login = new ObjectMapper();
					mapper_login.setSerializationInclusion(Include.NON_NULL);
					String jsonString = mapper_login.writeValueAsString(user_login);
					Log.d(LOG_TAG, jsonString);

					HttpPost request = new HttpPost(Constants.CONNECTION_STRING + "login");
					StringEntity sEntity = new StringEntity(jsonString, "UTF-8");
					request.setEntity(sEntity);
					request.setHeader("Content-type", "application/json");
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					Header[] mCookies = response.getHeaders("Set-Cookie");
					Log.d(LOG_TAG, "Cookie: " + mCookies[0].getName() + " Value: " + mCookies[0].getValue());
					HttpCookie cookie = new HttpCookie(jsonString, jsonString);
//					cookie.setValue(mCookies[0].getValue());
//					cs.add("spring.strengthandwill.cloudbees.net", mCookies[0].)

					in = new BufferedReader(new InputStreamReader(entity.getContent()), 8);
					result = in.readLine();
					Log.d(LOG_TAG, "Cookie Store: "+cm.getCookieStore().toString());
					// result = "{\"name\":\"Rekha_S\"}";
					if (!result.equals("null"))
					{
						Log.d(LOG_TAG, result);
						ObjectMapper mapper = new ObjectMapper();
						user = mapper.readValue(result, User.class);
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
					Log.e(LOG_TAG, "Caught Exception: Error converting result " + exc.toString());
				}
			}
		});

		thread.start();
		Log.d(LOG_TAG, printUserDetails());
		Log.d(LOG_TAG, "Cookies: " + cs.getCookies().size());

	}

	public String printUserDetails()
	{
		if (user == null)
			return "null";
		else
			return user.toString();
	}

	/**
	 * Builds a new HttpClient with the same CookieStore than the previous one. This allows to follow the http session, without keeping in memory the full DefaultHttpClient.
	 */
	private HttpClient getHttpClient()
	{
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		synchronized (mLock)
		{
			if (mCookie == null)
			{
				mCookie = httpClient.getCookieStore();
			}
			else
			{
				httpClient.setCookieStore(mCookie);
			}
		}

		return httpClient;
	}
}
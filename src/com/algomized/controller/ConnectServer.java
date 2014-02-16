package com.algomized.controller;

import android.app.ProgressDialog;
import android.content.Context;
import java.io.UnsupportedEncodingException;

import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;

import com.algomized.Constants;
import com.algomized.model.User;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.*;

import android.util.Log;
import android.widget.Toast;

public class ConnectServer
{
	String LOG_TAG = "ConnectServer";
	String username = "";
	String password = "";
	int age = 0;
	String result = "";
	User user;
	private static AsyncHttpClient client;
	Context context;
	PersistentCookieStore myCookieStore;

	public ConnectServer(Context context)
	{
		this.context = context;
		myCookieStore = new PersistentCookieStore(context);
	}

	public ConnectServer(String username, String password, Context context)
	{
		this.context = context;
		this.username = username;
		this.password = password;
		myCookieStore = new PersistentCookieStore(context);
	}

	public Boolean login() throws JsonProcessingException, UnsupportedEncodingException
	{
		client = new AsyncHttpClient();

		client.setCookieStore(myCookieStore);

		User user_login = new User();
		// user_login.setUsername("skypay");
		// user_login.setPassword("abcdef");
		user_login.setUsername(username);
		user_login.setPassword(password);

		ObjectMapper mapper_login = new ObjectMapper();
		mapper_login.setSerializationInclusion(Include.NON_NULL);
		String jsonString = mapper_login.writeValueAsString(user_login);
		Log.d(LOG_TAG, jsonString);

		StringEntity sEntity = new StringEntity(jsonString, "UTF-8");
		client.post(context, Constants.BASE_URL + "login", sEntity, "application/json", new AsyncHttpResponseHandler()
		{
			ProgressDialog dialog;

			@Override
			public void onStart()
			{
				Constants.CONNECTING_FLAG = false;
				dialog = new ProgressDialog(context);
				dialog.setMessage("Logging in");
				dialog.show();
			}

			@Override
			public void onSuccess(String response)
			{
				dialog.dismiss();

				try
				{
					ObjectMapper mapper = new ObjectMapper();
					user = mapper.readValue(response, User.class);
					Log.d(LOG_TAG, "Cookies number: " + myCookieStore.getCookies().size());
					Cookie cookie = myCookieStore.getCookies().get(0);
					Log.d(LOG_TAG, "Cookie: " + cookie.getName() + " Value: " + cookie.getValue());
					Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
					Constants.CONNECTING_FLAG = true;
					Constants.LOGIN_STATUS = true;
				}

				catch (Exception exc)
				{
					Log.e(LOG_TAG, "Caught Exception: Error converting result " + exc.toString());
				}
			}

			@Override
			public void onFailure(Throwable e, String response)
			{
				dialog.dismiss();
				Toast.makeText(context, "Error Occured ! Please try again.", Toast.LENGTH_SHORT).show();
				Log.d(LOG_TAG, response);
				Constants.CONNECTING_FLAG = true;
				Constants.LOGIN_STATUS = false;
				// cd.goHome(SearchActivity.this);
			}
		});

		while (!Constants.CONNECTING_FLAG)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return Constants.LOGIN_STATUS;
	}

	public Boolean checkLoginStatus()
	{
		if (myCookieStore.getCookies().size() != 0)
		{
			Cookie cookie = myCookieStore.getCookies().get(0);
			Log.d(LOG_TAG, "Cookie: " + cookie.getName() + " Value: " + cookie.getValue());
			if (myCookieStore.getCookies().size() != 0)
			{
				Constants.CONNECTING_FLAG = true;
				Constants.LOGIN_STATUS = true;
			}
			return true;
		}
		else
			return false;
	}

	public Boolean logout()
	{
		myCookieStore.clear();
		return true;
	}

	public String printUserDetails()
	{
		if (user == null)
			return "null";
		else
			return user.toString();
	}

}
package com.algomized.android.jourwee.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.SingleAsyncHttpClient;
import com.algomized.android.jourwee.model.User;
import com.algomized.android.jourwee.view.LocationActivity;
import com.algomized.android.jourwee.view.LoginActivity;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectServer
{
	String LOG_TAG = "ConnectServer";
	String username = "";
	String password = "";
	int age = 0;
	String result = "";
	User user;
	private static SingleAsyncHttpClient client;
	Context context;
	PersistentCookieStore myCookieStore;
	SharedPreferences sharedpref;

	// Constructor for checking if user is already logged in
	public ConnectServer(Context context)
	{
		this.context = context;
		sharedpref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		myCookieStore = new PersistentCookieStore(context);
	}

	// When user logs in for the first time or when user was logged out
	public ConnectServer(String username, String password, Context context)
	{
		this.context = context;
		this.username = username;
		this.password = password;
		myCookieStore = new PersistentCookieStore(context);
		sharedpref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
	}

	public void login() throws JsonProcessingException, UnsupportedEncodingException
	{
		client = SingleAsyncHttpClient.getInstance();

		client.setCookieStore(myCookieStore);

		User user_login = new User();
		user_login.setUsername(username);
		user_login.setPassword(password);

		ObjectMapper mapper_login = new ObjectMapper();
		mapper_login.setSerializationInclusion(Include.NON_NULL);
		String jsonString = mapper_login.writeValueAsString(user_login);
		Log.d(LOG_TAG, jsonString);

		StringEntity sEntity = new StringEntity(jsonString, "UTF-8");
		client.post(context, Constants.BASE_URL + Constants.LOGIN, sEntity, "application/json", new AsyncHttpResponseHandler()
		{
			ProgressDialog dialog;

			@Override
			public void onStart()
			{
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
					if (user.getId() <= 0)
					{
						// login is unsuccessful
						Toast.makeText(context, "Username and password do not match. Please try again.", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Log.d(LOG_TAG, "Cookies number: " + myCookieStore.getCookies().size());
						Cookie cookie = myCookieStore.getCookies().get(0);
						Log.d(LOG_TAG, "Cookie: " + cookie.getName() + " Value: " + cookie.getValue());
						Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
						Constants.LOGIN_STATUS = true;
						Log.d(LOG_TAG, "Response: " + response);
						Intent locationIntent = new Intent(context, LocationActivity.class);
						if (user.getId() == null || user.getUsername() == null)
						{
							Log.d(LOG_TAG, "User ID is null");
						}
						else
						{
							locationIntent.putExtra("userId", "" + user.getId());
							locationIntent.putExtra("username", user.getUsername());
							((Activity) context).startActivity(locationIntent);
							((Activity) context).finish();
						}
					}
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
				Constants.LOGIN_STATUS = false;

				// cd.goHome(SearchActivity.this);
			}
		});

		// while (!Constants.CONNECTING_FLAG)
		// {
		// try
		// {
		// Thread.sleep(100);
		// }
		// catch (InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		// }
	}

	public Boolean checkLoginStatus()
	{
		if (myCookieStore.getCookies().size() != 0)
		{
			Cookie cookie = myCookieStore.getCookies().get(0);
			Log.d(LOG_TAG, "Cookie: " + cookie.getName() + " Value: " + cookie.getValue());
			if (myCookieStore.getCookies().size() != 0)
			{
				Constants.LOGIN_STATUS = true;
			}
			return true;
		}
		else
			return false;
	}

	public void logout()
	{
		client = SingleAsyncHttpClient.getInstance();
		client.get(context, Constants.BASE_URL + Constants.LOGOUT, new AsyncHttpResponseHandler()
		{
			ProgressDialog dialog;

			@Override
			public void onStart()
			{
				dialog = new ProgressDialog(context);
				dialog.setMessage("Logging out...");
				dialog.show();
			}

			@Override
			public void onSuccess(String response)
			{
				dialog.dismiss();

				try
				{

					if (response.equals("completed"))
					{
						// logout is successful
						Toast.makeText(context, "Logout successfully!", Toast.LENGTH_SHORT).show();
						// Go back to login page.
						Intent loginIntent = new Intent(context, LoginActivity.class);
						loginIntent.putExtra("logout", true);
						((Activity) context).startActivity(loginIntent);
						((Activity) context).finish();

						myCookieStore.clear();
					}
					else
					{
						Toast.makeText(context, "Something is wrong. Please try again.", Toast.LENGTH_SHORT).show();
						Log.d(LOG_TAG, "Response: " + response);
					}

				}

				catch (Exception exc)
				{
					Log.e(LOG_TAG, "Caught Exception in logout(): Error converting result " + exc.toString());
				}
			}

			@Override
			public void onFailure(Throwable e, String response)
			{
				dialog.dismiss();
				Toast.makeText(context, "Error Occured ! Please try again.", Toast.LENGTH_SHORT).show();
				Log.d(LOG_TAG, response);
			}
		});
	}

	public String printUserDetails()
	{
		if (user == null)
			return "null";
		else
			return user.toString();
	}

	public void testRequest(final Boolean fromLogin)
	{
		client = SingleAsyncHttpClient.getInstance();

		client.setCookieStore(myCookieStore);
		user = new User();

		// Header[] headers = {
		// new BasicHeader("Content-type", "application/x-www-form-urlencoded")
		// ,new BasicHeader("Content-type", "application/x-www-form-urlencoded")
		// ,new BasicHeader("Accep", "text/html,text/xml,application/xml")
		// ,new BasicHeader("Connection", "keep-alive")
		// ,new BasicHeader("keep-alive", "115")
		// ,new BasicHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
		// };
		client.get(context, Constants.BASE_URL + Constants.TEST_API, getHeaders(fromLogin), null, new AsyncHttpResponseHandler()
		{
//			ProgressDialog dialog;

			@Override
			public void onStart()
			{
//				dialog = new ProgressDialog(context);
//				dialog.setMessage("Retrieving user data...");
//				dialog.show();
			}

			@Override
			public void onSuccess(String response)
			{
//				dialog.dismiss();

				try
				{

					ObjectMapper mapper = new ObjectMapper();
					user = mapper.readValue(response, User.class);

					if (user.getId() <= 0)
					{
						// login is unsuccessful
						// Toast.makeText(context, "Cookie has expired. Please login again.", Toast.LENGTH_SHORT).show();
						// Go back to login page.
					}
					else
					{
						Toast.makeText(context, "Retrieval successful!", Toast.LENGTH_SHORT).show();
						Log.d(LOG_TAG, "Response: " + response);
						if (!fromLogin)
						{
							// Change textviews text.
							TextView user_idText = (TextView) ((Activity) context).findViewById(R.id.userIDTxt);
							user_idText.setText("ID: " + user.getId());
							TextView usernameText = (TextView) ((Activity) context).findViewById(R.id.usernameTxt);
							usernameText.setText("Username: " + user.getUsername());
							// TextView nameText = (TextView) ((Activity)context).findViewById(R.id.nameTxt);
							// nameText.setText(user_request.getName());
							// if (activity != null) {
							// TextView text = activity.findViewById(R.id.text);
						}
						else
						{
							if (user.getId() == null || user.getUsername() == null)
							{
								Log.d(LOG_TAG, "User ID is null");
							}
							else
							{
								Intent locationIntent = new Intent(context, LocationActivity.class);
								locationIntent.putExtra("userId", "" + user.getId());
								locationIntent.putExtra("username", user.getUsername());
								((Activity) context).startActivity(locationIntent);
								((Activity) context).finish();
							}
						}
					}

				}

				catch (Exception exc)
				{
					Log.e(LOG_TAG, "Caught Exception in TestRequest: Error converting result " + exc.toString());
				}
			}

			@Override
			public void onFailure(Throwable e, String response)
			{
//				dialog.dismiss();
				Toast.makeText(context, "Error Occured ! Please try again.", Toast.LENGTH_SHORT).show();
				Log.d(LOG_TAG, response);
			}
		});
	}

	private Header[] getHeaders(Boolean fromLogin)
	{
		if (!fromLogin)
		{
			Cookie cookie = myCookieStore.getCookies().get(0);
			Header[] headers = { new BasicHeader("Cookie", cookie.toString()) };
			return headers;
		}
		return null;
	}
}
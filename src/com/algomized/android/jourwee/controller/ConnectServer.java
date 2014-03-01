package com.algomized.android.jourwee.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicHeader;
import com.algomized.android.jourwee.JourweeApplication;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.SingleAsyncHttpClient;
import com.algomized.android.jourwee.model.User;
import com.algomized.android.jourwee.view.LocationActivity;
import com.algomized.android.jourwee.view.LoginActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.android.volley.toolbox.JacksonJsonRequest;
import com.android.volley.toolbox.StringRequest;
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
	static final int GET = 0, POST = 1, PUT = 2, DELETE = 3, HEAD = 4, OPTIONS = 5, TRACE = 6, PATCH = 7;
	String result = "";
	User user;
	private static SingleAsyncHttpClient client;
	Context context;
	PersistentCookieStore myCookieStore;
	SharedPreferences sharedpref;
	String oauth = "";

	@Inject
	AndroidAuthenticator authenticator;

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

	public String login() throws JsonProcessingException, UnsupportedEncodingException
	{
		
		RequestQueue mRequestQueue = JourweeApplication.getInstance().getRequestQueue();

		StringRequest req = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.LOGIN, new Response.Listener<String>()
		{
			@Override
			public void onResponse(String response)
			{
				try
				{
					VolleyLog.d("Response: %s", response);
					Toast.makeText(context, "Response: " + response, Toast.LENGTH_LONG).show();
					ObjectMapper mapper = new ObjectMapper();
					user = mapper.readValue(response, User.class);
					oauth = user.getMessage();
					// oauth = response.getMessage();
					VolleyLog.d("Oauth %s", oauth);

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				VolleyLog.e("Error: ", error.getMessage());
			}
		})
		{
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<String, String>();
				VolleyLog.d("username: %s", username);
				params.put("j_username", username);
				params.put("j_password", password);

				return params;
			}

		};

		// add the request object to the queue to be executed
		mRequestQueue.add(req);

		return oauth;
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
			// ProgressDialog dialog;

			@Override
			public void onStart()
			{
				// dialog = new ProgressDialog(context);
				// dialog.setMessage("Retrieving user data...");
				// dialog.show();
			}

			@Override
			public void onSuccess(String response)
			{
				// dialog.dismiss();

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
				// dialog.dismiss();
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

	public String register() throws JsonProcessingException, UnsupportedEncodingException
	{
		Log.d(LOG_TAG, "Started authenticating");

		User user_reg = new User();
		user_reg.setUsername(username);
		user_reg.setPassword(password);
		user_reg.setEnabled(true);
		Map<String, String> user_role_map = new HashMap<String, String>();
		user_role_map.put("authority", "ROLE_USER");
		List<Map<String, String>> user_role = new ArrayList<Map<String, String>>();
		user_role.add(user_role_map);
		user_reg.setUserRoles(user_role);
		ObjectMapper mapper_reg = new ObjectMapper();
		mapper_reg.setSerializationInclusion(Include.NON_NULL);
		final String jsonString = mapper_reg.writeValueAsString(user_reg);
		Log.d(LOG_TAG, "JSON STRING: " + jsonString);

		RequestQueue mRequestQueue = JourweeApplication.getInstance().getRequestQueue();

		JacksonJsonRequest<User> req = new JacksonJsonRequest<User>(Request.Method.POST, Constants.BASE_URL + Constants.REGISTER, jsonString, User.class, new Response.Listener<User>()
		{

			@Override
			public void onResponse(User response)
			{

				try
				{
					VolleyLog.v("Response: %s", response.toString());
					Toast.makeText(context, "Response: " + response.toString(), Toast.LENGTH_LONG).show();
					oauth = response.getMessage();
					VolleyLog.d("Oauth %s", oauth);

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				VolleyLog.e("Error: ", error.getMessage());
			}
		});

		// add the request object to the queue to be executed
		mRequestQueue.add(req);
		
		return oauth;
	}
}
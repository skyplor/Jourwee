package com.algomized.android.jourwee.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.model.User;
import com.algomized.android.jourwee.unused.SingleAsyncHttpClient;
import com.algomized.android.jourwee.view.LocationActivity_;
import com.algomized.android.jourwee.view.LoginActivity;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkUtil
{
	String LOG_TAG = "NetworkUtil";
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
	Intent res;
	ProgressDialog dialog;
	ArrayList<NameValuePair> nameValuePairs;

	@Inject
	AndroidAuthenticator authenticator;

	// Constructor for checking if user is already logged in
	public NetworkUtil(Context context)
	{
		this.context = context;
		sharedpref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		myCookieStore = new PersistentCookieStore(context);
	}

	// When user logs in for the first time or when user was logged out
	public NetworkUtil(String username, String password, Context context)
	{
		this.context = context;
		this.username = username;
		this.password = password;
		myCookieStore = new PersistentCookieStore(context);
		sharedpref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		res = new Intent();
	}

	public User login() throws ClientProtocolException, IOException
	{
		user = new User();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("j_username", username));
		nameValuePairs.add(new BasicNameValuePair("j_password", password));

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.LOGIN);

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = httpclient.execute(httppost);

		ObjectMapper mapper = new ObjectMapper();
		String result = EntityUtils.toString(response.getEntity());

		Log.d(LOG_TAG, "Result: " + result);

		User user = mapper.readValue(result, User.class);
		Log.d(LOG_TAG, "User message: " + user.getMessage());

		return user;
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

	public boolean logout(String username, String oauthtoken) throws ClientProtocolException, IOException
	{
		
		user = new User();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("j_username", username));
		nameValuePairs.add(new BasicNameValuePair("j_oauth", oauthtoken));

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.LOGOUT);

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = httpclient.execute(httppost);

		ObjectMapper mapper = new ObjectMapper();
		String result = EntityUtils.toString(response.getEntity());

		Log.d(LOG_TAG, "Result: " + result);

		User user = mapper.readValue(result, User.class);
		Log.d(LOG_TAG, "User message: " + user.getMessage());
		return true;
//		client = SingleAsyncHttpClient.getInstance();
//		client.get(context, Constants.BASE_URL + Constants.LOGOUT, new AsyncHttpResponseHandler()
//		{
//			ProgressDialog dialog;
//
//			@Override
//			public void onStart()
//			{
//				dialog = new ProgressDialog(context);
//				dialog.setMessage("Logging out...");
//				dialog.show();
//			}
//
//			@Override
//			public void onSuccess(String response)
//			{
//				dialog.dismiss();
//
//				try
//				{
//
//					if (response.equals("completed"))
//					{
//						// logout is successful
//						Toast.makeText(context, "Logout successfully!", Toast.LENGTH_SHORT).show();
//						// Go back to login page.
//						Intent loginIntent = new Intent(context, LoginActivity.class);
//						loginIntent.putExtra("logout", true);
//						((Activity) context).startActivity(loginIntent);
//						((Activity) context).finish();
//
//						myCookieStore.clear();
//					}
//					else
//					{
//						Toast.makeText(context, "Something is wrong. Please try again.", Toast.LENGTH_SHORT).show();
//						Log.d(LOG_TAG, "Response: " + response);
//					}
//
//				}
//
//				catch (Exception exc)
//				{
//					Log.e(LOG_TAG, "Caught Exception in logout(): Error converting result " + exc.toString());
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable e, String response)
//			{
//				dialog.dismiss();
//				Toast.makeText(context, "Error Occured ! Please try again.", Toast.LENGTH_SHORT).show();
//				Log.d(LOG_TAG, response);
//			}
//		});
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
								Intent locationIntent = new Intent(context, LocationActivity_.class);
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

	public User register() throws ClientProtocolException, IOException
	{
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
		String jsonString;

		jsonString = mapper_reg.writeValueAsString(user_reg);

		Log.d(LOG_TAG, "JSON STRING: " + jsonString);
		StringEntity sEntity;

		sEntity = new StringEntity(jsonString, "UTF-8");

		// intent = cs.login();
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.REGISTER);

		httppost.setHeader("Content-Type", "application/json");

		httppost.setEntity(sEntity);

		HttpResponse response = httpclient.execute(httppost);

		ObjectMapper mapper = new ObjectMapper();
		String result = EntityUtils.toString(response.getEntity());

		Log.d(LOG_TAG, "Result: " + result);

		User user = mapper.readValue(result, User.class);
		Log.d(LOG_TAG, "User message: " + user.getMessage());

		return user_reg;
	}

	public void removeAccounts()
	{
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccounts();
		if (accounts.length > 0)
		{
			for (Account accountToRemove : accounts)
				// Account accountToRemove = accounts[0];
				am.removeAccount(accountToRemove, null, null);
		}

	}
}
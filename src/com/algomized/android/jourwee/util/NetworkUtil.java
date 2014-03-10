package com.algomized.android.jourwee.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.model.User;
import com.algomized.android.jourwee.unused.SingleAsyncHttpClient;
import com.algomized.android.jourwee.view.LocationActivity_;
import com.algomized.android.jourwee.view.StartActivity_;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.Base64;

import android.os.Bundle;
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
	OutputStream out = null;
	InputStream in = null;
	// private static SingleAsyncHttpClient client;
	Context context;
	// PersistentCookieStore myCookieStore;
	// SharedPreferences sharedpref;
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
		// sharedpref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		// myCookieStore = new PersistentCookieStore(context);
	}

	// When user logs in for the first time or when user was logged out
	public NetworkUtil(String username, String password, Context context)
	{
		this.context = context;
		this.username = username;
		this.password = password;
		// myCookieStore = new PersistentCookieStore(context);
		// sharedpref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		res = new Intent();
	}

	public User login() throws ClientProtocolException, IOException
	{
		user = new User();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(Constants.KEY_GRANT_TYPE, Constants.GRANT_PASSWORD));
		nameValuePairs.add(new BasicNameValuePair(Constants.KEY_USERNAME, username));
		nameValuePairs.add(new BasicNameValuePair(Constants.KEY_PASSWORD, password));

		String encodedHeader = encodeHeader(Constants.CLIENT_ID, Constants.CLIENT_SECRET);

		OkHttpClient httpclient = new OkHttpClient();
		HttpURLConnection connection = httpclient.open(new URL(Constants.BASE_URL + Constants.URL_TOKEN));

		try
		{
			// Write the request.
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String headerValue = Constants.HEADER_BASIC + " " + encodedHeader;

			Log.d(LOG_TAG, "Header Value: " + headerValue);

			connection.addRequestProperty(Constants.KEY_HEADER_AUTH, headerValue);

			out = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(getQuery(nameValuePairs));
			writer.flush();
			writer.close();
			out.close();

			// Read the response.
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				throw new IOException("Unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
			in = connection.getInputStream();

			ObjectMapper mapper = new ObjectMapper();

			String result = readFirstLine(in);

			Log.d(LOG_TAG, "Result: " + result);

			user = mapper.readValue(result, User.class);
			Log.d(LOG_TAG, "User Response: " + user.toString());

			return user;
		}
		finally
		{
			// Clean up.
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		}
	}

	String readFirstLine(InputStream in) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		Log.d(LOG_TAG, "After bufferedreader");
		String result = reader.readLine();
		Log.d(LOG_TAG, "result in readfirstline: " + result);
		return result;
	}

	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private String encodeHeader(String clientId, String clientSecret)
	{
		byte[] clientBytes = null;
		try
		{
			clientBytes = (clientId + ":" + clientSecret).getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return "null";
		}
		String encodedHeader = Base64.encode(clientBytes);
		Log.d(LOG_TAG, "EncodedHeader: " + encodedHeader);

		return encodedHeader;
	}

	public Bundle checkLoginStatus(Activity activity)
	{
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccountsByType(Constants.AM_ACCOUNT_TYPE);
		Bundle resultBundle = null;
		if (accounts.length > 0)
		{
			Log.d(LOG_TAG, "Accounts is not empty");
			// Get current auth token stored. Refresh token if available.
			AccountManagerFuture<Bundle> future = am.getAuthToken(accounts[0], Constants.AM_AUTH_TYPE, null, activity, null, null);
			try
			{
				Bundle bundle = future.getResult();
				String old_authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

				// invalidate the token since it may have expired.
				am.invalidateAuthToken(Constants.AM_ACCOUNT_TYPE, old_authToken);

				// Get token again
				future = am.getAuthToken(accounts[0], Constants.AM_AUTH_TYPE, null, activity, null, null);
				String oauth = "", refresh_token = "", expires_in = "";

				resultBundle = future.getResult();
				oauth = resultBundle.getString(AccountManager.KEY_AUTHTOKEN);
				refresh_token = resultBundle.getString(Constants.AM_KEY_REFRESH_TOKEN);
				expires_in = resultBundle.getString(Constants.AM_KEY_EXPIRES_IN);
				if (oauth != "")
				{
					Log.d(LOG_TAG, "oauth: " + oauth + "\n refresh_token: " + refresh_token + "\n expires_in: " + expires_in);
					am.setAuthToken(accounts[0], Constants.AM_AUTH_TYPE, oauth);
					am.setUserData(accounts[0], Constants.AM_KEY_REFRESH_TOKEN, refresh_token);
					am.setUserData(accounts[0], Constants.AM_KEY_EXPIRES_IN, expires_in);
				}
			}
			catch (OperationCanceledException e)
			{
				e.printStackTrace();
			}
			catch (AuthenticatorException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Log.d(LOG_TAG, "AccountManager's account is empty!");
		}
		return resultBundle;
	}

	public Bundle refreshToken(Account account, String refreshToken) throws IOException
	{
		Bundle bundleResult = new Bundle();

		user = new User();
		try
		{
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(Constants.KEY_GRANT_TYPE, Constants.GRANT_REFRESH));
			nameValuePairs.add(new BasicNameValuePair(Constants.KEY_REFRESH_TOKEN, refreshToken));

			String encodedHeader = encodeHeader(Constants.CLIENT_ID, Constants.CLIENT_SECRET);

			OkHttpClient httpclient = new OkHttpClient();
			HttpURLConnection connection = httpclient.open(new URL(Constants.BASE_URL + Constants.URL_TOKEN));

			// Write the request.
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String headerValue = Constants.HEADER_BASIC + " " + encodedHeader;

			Log.d(LOG_TAG, "Header Value: " + headerValue);

			connection.addRequestProperty(Constants.KEY_HEADER_AUTH, headerValue);

			out = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(getQuery(nameValuePairs));
			writer.flush();
			writer.close();
			out.close();

			// Read the response.
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				throw new IOException("Unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
			in = connection.getInputStream();

			ObjectMapper mapper = new ObjectMapper();

			String result = readFirstLine(in);

			Log.d(LOG_TAG, "Result: " + result);

			user = mapper.readValue(result, User.class);
			Log.d(LOG_TAG, "User Response: " + user.toString());
		}
		catch (ClientProtocolException cpe)
		{
			Log.d(LOG_TAG, "Encountered ClientProtocolException: " + cpe);
		}
		finally
		{
			// Clean up.
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		}

		bundleResult.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
		bundleResult.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
		Log.d(LOG_TAG, "In Refresh Token, accessToken: " + user.getAccess_token());
		bundleResult.putString(AccountManager.KEY_AUTHTOKEN, user.getAccess_token());
		Log.d(LOG_TAG, "In Refresh Token, refreshToken: " + user.getRefresh_token());
		bundleResult.putString(Constants.AM_KEY_REFRESH_TOKEN, user.getRefresh_token());
		Log.d(LOG_TAG, "In Refresh Token, expiresIn: " + user.getExpires_in());
		bundleResult.putString(Constants.AM_KEY_EXPIRES_IN, user.getExpires_in());

		return bundleResult;
	}

	public boolean logout(String username, String oauthtoken) throws ClientProtocolException, IOException
	{
		user = new User();
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("j_username", username));
		nameValuePairs.add(new BasicNameValuePair("j_oauth", oauthtoken));

		try
		{
			OkHttpClient httpclient = new OkHttpClient();
			HttpURLConnection connection = httpclient.open(new URL(Constants.BASE_URL + Constants.URL_LOGOUT));

			// Write the request.
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			out = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(getQuery(nameValuePairs));
			writer.flush();
			writer.close();
			out.close();

			// Read the response.
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				throw new IOException("Unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
			in = connection.getInputStream();

			ObjectMapper mapper = new ObjectMapper();

			String result = readFirstLine(in);

			Log.d(LOG_TAG, "Result: " + result);

			user = mapper.readValue(result, User.class);
			Log.d(LOG_TAG, "User message: " + user.getMessage());
			return user.isStatus();
		}
		finally
		{
			// Clean up.
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		}
		// client = SingleAsyncHttpClient.getInstance();
		// client.get(context, Constants.BASE_URL + Constants.LOGOUT, new AsyncHttpResponseHandler()
		// {
		// ProgressDialog dialog;
		//
		// @Override
		// public void onStart()
		// {
		// dialog = new ProgressDialog(context);
		// dialog.setMessage("Logging out...");
		// dialog.show();
		// }
		//
		// @Override
		// public void onSuccess(String response)
		// {
		// dialog.dismiss();
		//
		// try
		// {
		//
		// if (response.equals("completed"))
		// {
		// // logout is successful
		// Toast.makeText(context, "Logout successfully!", Toast.LENGTH_SHORT).show();
		// // Go back to login page.
		// Intent loginIntent = new Intent(context, LoginActivity.class);
		// loginIntent.putExtra("logout", true);
		// ((Activity) context).startActivity(loginIntent);
		// ((Activity) context).finish();
		//
		// myCookieStore.clear();
		// }
		// else
		// {
		// Toast.makeText(context, "Something is wrong. Please try again.", Toast.LENGTH_SHORT).show();
		// Log.d(LOG_TAG, "Response: " + response);
		// }
		//
		// }
		//
		// catch (Exception exc)
		// {
		// Log.e(LOG_TAG, "Caught Exception in logout(): Error converting result " + exc.toString());
		// }
		// }
		//
		// @Override
		// public void onFailure(Throwable e, String response)
		// {
		// dialog.dismiss();
		// Toast.makeText(context, "Error Occured ! Please try again.", Toast.LENGTH_SHORT).show();
		// Log.d(LOG_TAG, response);
		// }
		// });
	}

	public String printUserDetails()
	{
		if (user == null)
			return "null";
		else
			return user.toString();
	}

	public Boolean testRequest(boolean fromLogin) throws IOException
	{

		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccountsByType(Constants.AM_ACCOUNT_TYPE);
		if (accounts.length > 0)
		{
			Account account = accounts[0];
			String accessToken = am.peekAuthToken(account, Constants.AM_AUTH_TYPE);
			try
			{
				user = new User();

				OkHttpClient httpclient = new OkHttpClient();
				HttpURLConnection connection = httpclient.open(new URL(Constants.BASE_URL + Constants.URL_GET_USER));

				// Write the request.
				connection.setRequestMethod("GET");

				String headerValue = Constants.HEADER_BEARER + " " + accessToken;

				Log.d(LOG_TAG, "Header Value: " + headerValue);

				connection.addRequestProperty(Constants.KEY_HEADER_AUTH, headerValue);

				// Read the response.
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
				{
					Log.d(LOG_TAG, "We got either http ok or unauthorized: " + connection.getResponseCode());

					ObjectMapper mapper = new ObjectMapper();

					if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
					{
						// If get error response, we have to get error stream instead of input stream
						in = connection.getErrorStream();
						Log.d(LOG_TAG, "After getting error stream");
						String result = readFirstLine(in);
						user = mapper.readValue(result, User.class);
						Log.d(LOG_TAG, "User Response Error: " + user.getError());
						return false;
					}
					else
					{

						in = connection.getInputStream();
						Log.d(LOG_TAG, "After getting input stream");
						String result = readFirstLine(in);
						Log.d(LOG_TAG, "Result: " + result);
						Log.d(LOG_TAG, "No error in retrieving user. Result: " + result);
						return true;
					}

				}
				else
				{
					Log.d(LOG_TAG, "We got unexpected HTTP response");
					throw new IOException("Unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
				}

			}
			catch (ClientProtocolException cpe)
			{
				Log.d(LOG_TAG, "Encountered ClientProtocolException: " + cpe);
			}
			finally
			{
				// Clean up.
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			}

		}

		return false;

		// client = SingleAsyncHttpClient.getInstance();
		//
		// client.setCookieStore(myCookieStore);
		// user = new User();
		//
		// // Header[] headers = {
		// // new BasicHeader("Content-type", "application/x-www-form-urlencoded")
		// // ,new BasicHeader("Content-type", "application/x-www-form-urlencoded")
		// // ,new BasicHeader("Accep", "text/html,text/xml,application/xml")
		// // ,new BasicHeader("Connection", "keep-alive")
		// // ,new BasicHeader("keep-alive", "115")
		// // ,new BasicHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
		// // };
		// client.get(context, Constants.BASE_URL + Constants.URL_TEST_API, getHeaders(fromLogin), null, new AsyncHttpResponseHandler()
		// {
		// // ProgressDialog dialog;
		//
		// @Override
		// public void onStart()
		// {
		// // dialog = new ProgressDialog(context);
		// // dialog.setMessage("Retrieving user data...");
		// // dialog.show();
		// }
		//
		// @Override
		// public void onSuccess(String response)
		// {
		// // dialog.dismiss();
		//
		// try
		// {
		//
		// ObjectMapper mapper = new ObjectMapper();
		// user = mapper.readValue(response, User.class);
		//
		// if (user.getId() <= 0)
		// {
		// // login is unsuccessful
		// // Toast.makeText(context, "Cookie has expired. Please login again.", Toast.LENGTH_SHORT).show();
		// // Go back to login page.
		// }
		// else
		// {
		// Toast.makeText(context, "Retrieval successful!", Toast.LENGTH_SHORT).show();
		// Log.d(LOG_TAG, "Response: " + response);
		// if (!fromLogin)
		// {
		// // Change textviews text.
		// TextView user_idText = (TextView) ((Activity) context).findViewById(R.id.userIDTxt);
		// user_idText.setText("ID: " + user.getId());
		// TextView usernameText = (TextView) ((Activity) context).findViewById(R.id.usernameTxt);
		// usernameText.setText("Username: " + user.getUsername());
		// // TextView nameText = (TextView) ((Activity)context).findViewById(R.id.nameTxt);
		// // nameText.setText(user_request.getName());
		// // if (activity != null) {
		// // TextView text = activity.findViewById(R.id.text);
		// }
		// else
		// {
		// if (user.getId() == null || user.getUsername() == null)
		// {
		// Log.d(LOG_TAG, "User ID is null");
		// }
		// else
		// {
		// Intent locationIntent = new Intent(context, LocationActivity_.class);
		// locationIntent.putExtra("userId", "" + user.getId());
		// locationIntent.putExtra("username", user.getUsername());
		// ((Activity) context).startActivity(locationIntent);
		// ((Activity) context).finish();
		// }
		// }
		// }
		//
		// }
		//
		// catch (Exception exc)
		// {
		// Log.e(LOG_TAG, "Caught Exception in TestRequest: Error converting result " + exc.toString());
		// }
		// }
		//
		// @Override
		// public void onFailure(Throwable e, String response)
		// {
		// // dialog.dismiss();
		// Toast.makeText(context, "Error Occured ! Please try again.", Toast.LENGTH_SHORT).show();
		// Log.d(LOG_TAG, response);
		// }
		// });
	}

	// private Header[] getHeaders(Boolean fromLogin)
	// {
	// if (!fromLogin)
	// {
	// Cookie cookie = myCookieStore.getCookies().get(0);
	// Header[] headers = { new BasicHeader("Cookie", cookie.toString()) };
	// return headers;
	// }
	// return null;
	// }

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

		HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.URL_REGISTER);

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
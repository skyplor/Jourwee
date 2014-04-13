package com.algomized.android.jourwee.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.TextUtils;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.R.id;
import com.algomized.android.jourwee.R.layout;
import com.algomized.android.jourwee.R.menu;
import com.algomized.android.jourwee.model.JourUser;
import com.algomized.android.jourwee.util.LogUtil;
import com.android.volley.VolleyLog;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.Base64;

import android.R.color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;

@EActivity(R.layout.activity_web_view)
public class WebViewActivity extends AccountAuthenticatorActivity
{

	// @Extra("url")
	String url;

	// @Extra("params")
	String params;

	@ViewById
	WebView social_web;

	private final static String POSTSIGNIN_ENDPOINT = "postsignin";
	private ArrayList<NameValuePair> nameValuePairs;

	private static final String LOG_TAG = WebViewActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			url = extras.getString("url");
			Log.d(LOG_TAG, "OnCreate URL: " + url);
			params = extras.getString("params");

		}
		else
		{
			finish();
		}

	}

	@AfterViews
	void init()
	{
		social_web.getSettings().setJavaScriptEnabled(true);
		social_web.setBackgroundColor(color.black);

		/* Register a new JavaScript interface called HTMLOUT */
		social_web.addJavascriptInterface(new MyJavaScriptInterface(), "htmlOut");
		social_web.getSettings().setBuiltInZoomControls(true);
		social_web.getSettings().setDisplayZoomControls(true);
		// social_web.getSettings().setDefaultZoom(ZoomDensity.FAR);
//		social_web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		/* WebViewClient must be set BEFORE calling loadUrl! */
		social_web.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				/* This call inject JavaScript into the page which just finished loading. */
				Log.d(LogUtil.getCurrentClassName(), LogUtil.getCurrentMethodName() + ", URL: " + url);
				Uri uri = Uri.parse(url);
				if (uri.getLastPathSegment() != null && uri.getLastPathSegment().equals(POSTSIGNIN_ENDPOINT))
				{
					social_web.loadUrl("javascript:window.htmlOut.showHTML(document.getElementsByTagName('pre')[0].innerHTML);");
				}
			}

			// @Override
			// public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
			// {
			// handler.proceed(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
			// }
		});
		// String grant = Constants.KEY_GRANT_TYPE + "=" + Constants.GRANT_PASSWORD;
		// params = (params == null) ? grant : grant + "&" + params;

		loadsociallogin(social_web);

		// TODO Webview post header and params
		// if (params != null)
		// {
		// params = "user_type=rider";
		// social_web.postUrl(url, EncodingUtils.getBytes(params, "BASE64"));
		// }
	}

	@Background
	public void loadsociallogin(WebView webview)
	{
		String result = "";
		OutputStream out = null;
		InputStream in = null;
		try
		{
			nameValuePairs = new ArrayList<NameValuePair>();
			// if (!params.isEmpty())
			// {
			// nameValuePairs.add(new BasicNameValuePair("scope", params));
			nameValuePairs.add(new BasicNameValuePair("user_type", "rider"));
			// }

			String encodedHeader = encodeHeader(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
			HttpClient httpclient = new DefaultHttpClient();
	         HttpPost httppost = new HttpPost(url);  
	         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	         httppost.setHeader(Constants.KEY_HEADER_AUTH, Constants.HEADER_BASIC+" "+encodedHeader);
	         
	         HttpResponse response = httpclient.execute(httppost);  
	         if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK || response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
	         {
	        	 result = new BasicResponseHandler().handleResponse(response);

			/*OkHttpClient httpclient = new OkHttpClient();
			HttpURLConnection connection = httpclient.open(new URL(url));

			// Write the request.
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String headerValue = Constants.HEADER_BASIC + " " + encodedHeader;

			Log.d(LOG_TAG, "Header Value: " + headerValue);

			connection.addRequestProperty(Constants.KEY_HEADER_AUTH, headerValue);

			// if (!params.isEmpty())
			// {
			out = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(getQuery(nameValuePairs));
			Log.d(LOG_TAG, "NameValuePairs: " + getQuery(nameValuePairs));
			// writer.write(nameValuePairs.toString());
			writer.flush();
			writer.close();
			out.close();
			// }

			// Read the response.
			ObjectMapper mapper = new ObjectMapper();
			

			// Read the response.
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
			{
				Log.d(LOG_TAG, "We got either http ok or unauthorized: " + connection.getResponseCode());

				if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
				{
					// If get error response, we have to get error stream instead of input stream
					in = connection.getErrorStream();
					Log.d(LOG_TAG, "After getting error stream");

					result = readResult(in);
					// user = mapper.readValue(result, JourUser.class);

				}
				else
				{

					in = connection.getInputStream();
					Log.d(LOG_TAG, "After getting input stream");

					result = readResult(in);
					// user = mapper.readValue(result, JourUser.class);
					// if (TextUtils.isEmpty(user.getError()))
					// {
					// user = login();
					// }
				}
				*/
	        	 writeToFile(result);
				showWebView(1, result);

			}
			else
			{
//				Log.d(LOG_TAG, "We got unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
				Log.d(LOG_TAG, "We got unexpected HTTP response: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				// throw new IOException("Unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
//				showWebView(-1, "We got unexpected HTTP response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
				showWebView(-1, "We got unexpected HTTP response: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
			}
		}
		catch (ClientProtocolException cpe)
		{
			Log.d(LOG_TAG, "Encountered ClientProtocolException: " + cpe);
			showWebView(-1, cpe.toString());
		}
		catch (ProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(LOG_TAG, LogUtil.getCurrentMethodName() + ", Kena ProtocolException: " + e.toString());
			showWebView(-1, e.toString());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(LOG_TAG, LogUtil.getCurrentMethodName() + ", Kena IOException: " + e.toString());
			showWebView(-1, e.toString());
		}
		finally
		{
			// Clean up.
			if (out != null)
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d(LOG_TAG, LogUtil.getCurrentMethodName() + ", Kena IOException: " + e.toString());
				}
			if (in != null)
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d(LOG_TAG, LogUtil.getCurrentMethodName() + ", Kena IOException: " + e.toString());
				}
		}

	}

	@UiThread
	public void showWebView(int code, String html)
	{
		// TODO Auto-generated method stub

		switch (code)
		{
			case 1:
//				social_web.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
				social_web.loadDataWithBaseURL("www.facebook.com", html, "text/html", "UTF-8", null);
				// social_web.loadDataWithBaseURL(Constants.BASE_URL, html, "text/html", "UTF-8", null);
//				 social_web.loadData(html, "text/html", "UTF-8");
				break;
			case -1:
//				social_web.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
				social_web.loadDataWithBaseURL("www.facebook.com", html, "text/html", "UTF-8", null);
				// social_web.loadDataWithBaseURL(Constants.BASE_URL, html, "text/html", "UTF-8", null);
//				 social_web.loadData(html, "text/html", "UTF-8");
				break;
			default:
				break;
		}
	}

	private void writeToFile(String data)
	{
		String path = getFilesDir().getAbsolutePath();
		VolleyLog.d("Response stored in: " + path);
		try
		{
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		}
		catch (IOException e)
		{
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	String readResult(InputStream in) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		Log.d(LOG_TAG, "After bufferedreader");
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;)
		{
			builder.append(line).append("\n");
		}
		String result = builder.toString();
		Log.d(LOG_TAG, "result in readResult: " + result);

		writeToFile(result);

		return result;
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

			if (pair.getName().equals("scope"))
			{
				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				result.append(pair.getValue());
			}
			else
			{
				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			}
		}
		Log.d(LOG_TAG, "Get Query: " + result.toString());

		return result.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	// public static class PlaceholderFragment extends Fragment
	// {
	//
	// public PlaceholderFragment()
	// {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	// {
	// View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
	// return rootView;
	// }
	// }

	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
		@JavascriptInterface
		// extract access token from WebView and return to calling activity
		public void showHTML(String json)
		{
			Log.d(LOG_TAG, "JSON: " + json);
			JourUser user = new JourUser();
			if (StringUtils.isNotBlank(json))
			{
				try
				{
					ObjectMapper mapper = new ObjectMapper();
					// OAuth2AccessToken token = mapper.readValue(json, OAuth2AccessToken.class);
					// Intent intent = getIntent();
					// intent.putExtra("access_token", token.getAccess_token());
					// intent.putExtra("token_type", token.getToken_type());
					// intent.putExtra("refresh_token", token.getRefresh_token());
					// intent.putExtra("expires_in", token.getExpires_in());
					user = mapper.readValue(json, JourUser.class);
					Intent intent = getIntent();
					intent.putExtra(AccountManager.KEY_AUTHTOKEN, user.getAccess_token());
					intent.putExtra(Constants.KEY_TOKEN_TYPE, user.getToken_type());
					intent.putExtra(Constants.KEY_REFRESH_TOKEN, user.getRefresh_token());
					intent.putExtra(Constants.KEY_EXPIRES_IN, user.getExpires_in());

					String oauth = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
					String refresh_token = intent.getStringExtra(Constants.KEY_REFRESH_TOKEN);
					String expires_in = intent.getStringExtra(Constants.KEY_EXPIRES_IN);
					AccountManager mAccountManager = AccountManager.get(getBaseContext());
					String username = intent.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
					final Account account = new Account(username, Constants.AM_ACCOUNT_TYPE);
					if (mAccountManager.addAccountExplicitly(account, "", null))
					{
						VolleyLog.d("Successfully added account: %s", username);
						mAccountManager.setAuthToken(account, Constants.AM_AUTH_TYPE, oauth);
						mAccountManager.setUserData(account, Constants.AM_KEY_REFRESH_TOKEN, refresh_token);
						mAccountManager.setUserData(account, Constants.AM_KEY_EXPIRES_IN, expires_in);
					}
					else
					{
						VolleyLog.d("Unable to add account");
					}
					WebViewActivity.this.setAccountAuthenticatorResult(intent.getExtras());
					setResult(RESULT_OK, intent);
					finish();
				}
				catch (JsonParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (JsonMappingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

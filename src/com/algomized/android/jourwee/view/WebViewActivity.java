package com.algomized.android.jourwee.view;

import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.util.EncodingUtils;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.R.id;
import com.algomized.android.jourwee.R.layout;
import com.algomized.android.jourwee.R.menu;
import com.algomized.android.jourwee.model.OAuth2AccessToken;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;

@EActivity(R.layout.activity_web_view)
public class WebViewActivity extends Activity
{

	// @Extra("url")
	String url;

	// @Extra("params")
	String params;

	@ViewById
	WebView social_web;

	private final static String POSTSIGNIN_ENDPOINT = "postsignin";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			url = extras.getString("url");
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

		/* WebViewClient must be set BEFORE calling loadUrl! */
		social_web.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				/* This call inject JavaScript into the page which just finished loading. */
				Uri uri = Uri.parse(url);
				if (uri.getLastPathSegment().equals(POSTSIGNIN_ENDPOINT))
				{
					social_web.loadUrl("javascript:window.htmlOut.showHTML(document.getElementsByTagName('pre')[0].innerHTML);");
				}
			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
			{
				handler.proceed(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
			}
		});
		String grant = Constants.KEY_GRANT_TYPE + "=" + Constants.GRANT_PASSWORD;
		params = (params == null) ? grant : grant + "&" + params;
		social_web.postUrl(url, EncodingUtils.getBytes(params, "BASE64"));
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
	public static class PlaceholderFragment extends Fragment
	{

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
			return rootView;
		}
	}

	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
		@JavascriptInterface
		// extract access token from WebView and return to calling activity
		public void showHTML(String json)
		{
			try
			{
				ObjectMapper mapper = new ObjectMapper();
				OAuth2AccessToken token = mapper.readValue(json, OAuth2AccessToken.class);
				Intent intent = getIntent();
				intent.putExtra("access_token", token.getAccess_token());
				intent.putExtra("token_type", token.getToken_type());
				intent.putExtra("refresh_token", token.getRefresh_token());
				intent.putExtra("expires_in", token.getExpires_in());
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

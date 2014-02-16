package com.algomized;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AlgoCookieStore implements CookieStore
{
	private static final String LOG_TAG = "AlgoCookieStore";
	/*
	 * The memory storage of the cookies
	 */
	private Map<String, Map<String, String>> mapCookies = new HashMap<String, Map<String, String>>();
	/*
	 * The instance of the shared preferences
	 */
	private final SharedPreferences sharedPrefs;

	public AlgoCookieStore(Context context)
	{
		// TODO Auto-generated constructor stub
		sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
	}

	public void add(URI uri, HttpCookie cookie)
	{
		String domain = cookie.getDomain();

		// Log.i(LOGTAG, "adding ( " + domain +", " + cookie.toString() );

		Map<String, String> cookies = mapCookies.get(domain);
		if (cookies == null)
		{
			cookies = new HashMap<String, String>();
			mapCookies.put(domain, cookies);
		}
		cookies.put(cookie.getName(), cookie.getValue());

		if (cookie.getName().startsWith("SPRING_SECURITY") && !cookie.getValue().equals(""))
		{
			// Log.i(LOGTAG, "Saving rememberMeCookie = " + cookie.getValue() );
			// Update in Shared Preferences
			Editor e = sharedPrefs.edit();
			e.putString(Constants.PREF_SPRING_SECURITY_COOKIE, cookie.toString());
			e.commit(); // save changes
		}

	}

	public List<HttpCookie> get(URI uri)
	{

		List<HttpCookie> cookieList = new ArrayList<HttpCookie>();

		String domain = uri.getHost();

		// Log.i(LOGTAG, "getting ( " + domain +" )" );

		Map<String, String> cookies = mapCookies.get(domain);
		if (cookies == null)
		{
			cookies = new HashMap<String, String>();
			mapCookies.put(domain, cookies);
		}

		for (Map.Entry<String, String> entry : cookies.entrySet())
		{
			cookieList.add(new HttpCookie(entry.getKey(), entry.getValue()));
			// Log.i(LOGTAG, "returning cookie: " + entry.getKey() + "="+ entry.getValue());
		}
		return cookieList;

	}
	
	public boolean removeAll() {

        // Log.i(LOGTAG, "removeAll()" );

        mapCookies.clear();
        return true;

    } 

	public boolean remove(URI uri, HttpCookie cookie)
	{

		String domain = cookie.getDomain();

		Log.i(LOG_TAG, "remove( " + domain + ", " + cookie.toString());

		Map<String, String> lstCookies = mapCookies.get(domain);

		if (lstCookies == null)
			return false;

		return lstCookies.remove(cookie.getName()) != null;

	}

	public List<HttpCookie> getCookies()
	{
		Log.i(LOG_TAG, "getCookies()");
		Set<String> mapKeys = mapCookies.keySet();

		List<HttpCookie> result = new ArrayList<HttpCookie>();
		for (String key : mapKeys)
		{
			Map<String, String> cookies = mapCookies.get(key);
			for (Map.Entry<String, String> entry : cookies.entrySet())
			{
				result.add(new HttpCookie(entry.getKey(), entry.getValue()));
				Log.i(LOG_TAG, "returning cookie: " + entry.getKey() + "=" + entry.getValue());
			}
		}

		return result;
	}

	public List<URI> getURIs()
	{

		Log.i(LOG_TAG, "getURIs()");

		Set<String> keys = mapCookies.keySet();
		List<URI> uris = new ArrayList<URI>(keys.size());
		for (String key : keys)
		{
			URI uri = null;
			try
			{
				uri = new URI(key);
			}
			catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
			uris.add(uri);
		}
		return uris;

	}

}

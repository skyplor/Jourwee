package com.algomized;

import org.apache.http.impl.client.DefaultHttpClient;
import com.loopj.android.http.AsyncHttpClient;

public class SingleAsyncHttpClient extends AsyncHttpClient
{
	private static SingleAsyncHttpClient instance = null;
	private DefaultHttpClient httpClient = null;

	/**
	 * Ensures the class cannot be instantiated
	 */
	private SingleAsyncHttpClient()
	{
		httpClient = new DefaultHttpClient();
	}

	/**
	 * @return The Singleton instance of our {@link SingleAsyncHttpClient}
	 */
	public static SingleAsyncHttpClient getInstance()
	{
		if (instance == null)
		{
			instance = new SingleAsyncHttpClient();
		}

		return instance;
	}
}

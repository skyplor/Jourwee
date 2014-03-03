package com.algomized.android.jourwee.unused;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.unused.util.InlineRequest;
import com.algomized.android.jourwee.unused.util.TokenStore;
import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonParseException;

public class JourweeAuth
{
	private boolean mHasRestored;
	protected long mExpires;
	protected String mAccessToken;
	protected String mRefreshToken;
	protected String curUser;
	private String mHost = Constants.BASE_URL;
	private final TokenStore mStore;

	public JourweeAuth(Context paramContext, TokenStore paramTokenStore, Network paramNetwork)
	{
		this.mStore = paramTokenStore;
	}

	public JourweeAuth(Context paramContext, TokenStore paramTokenStore)
	{
		this.mStore = paramTokenStore;
	}

	public void onRestoreCreds()
	{
		if (!this.mHasRestored)
		{
			this.mHasRestored = true;
			TokenStore localTokenStore = this.mStore;
			this.mExpires = localTokenStore.getLong("jourwee_access_token_expires", 0L);
			this.mAccessToken = localTokenStore.getString("jourwee_access_token", null);
			this.mRefreshToken = localTokenStore.getString("jourwee_refresh_token", null);
			this.curUser = localTokenStore.getString("jourwee_current_user", null);
		}
	}

	public boolean isValid()
	{
		return (this.curUser != null) && (this.mExpires - 3600000L > System.currentTimeMillis());
	}

	public InlineRequest onBuildAuthRequest(Object obj, AuthListener authlistener)
	{
		return onBuildAuthRequest((UserPassCreds) obj, authlistener);
	}

	public InlineRequest onBuildAuthRequest(final UserPassCreds paramUserPassCreds, final AuthListener paramAuthListener)
	{
		return new InlineRequest(1, getOauthUrl(), new Response.ErrorListener()
		{
			public void onErrorResponse(VolleyError paramAnonymousVolleyError)
			{
				Log.e("jourwee:auth", "onErrorResponse", paramAnonymousVolleyError);
				paramAuthListener.onAuthResult(Result.error(paramAnonymousVolleyError));
			}
		})
		{
			protected void deliverResponse(JourweeAuthResponse paramAnonymousJourweeAuthResponse)
			{
				paramAuthListener.onAuthResult(Result.SUCCESS);
			}

			protected Map<String, String> getParams() throws AuthFailureError
			{
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("grant_type", "password");
				localHashMap.put("scope", "read_public read_all modify_all modify_user upload_new");
				addClientCredentials(localHashMap);
				localHashMap.put("username", paramUserPassCreds.username);
				localHashMap.put("password", paramUserPassCreds.password);
				return localHashMap;
			}

			protected Response<JourweeAuthResponse> parseNetworkResponse(NetworkResponse paramAnonymousNetworkResponse)
			{
				InputStreamReader localInputStreamReader = new InputStreamReader(new ByteArrayInputStream(paramAnonymousNetworkResponse.data));
				try
				{
					JourweeAuth.JourweeAuthResponse localJourweeAuthResponse = null;//(JourweeAuthResponse) new Gson().fromJson(localInputStreamReader, JourweeAuth.JourweeAuthResponse.class);
					if (localJourweeAuthResponse != null)
					{
						mAccessToken = localJourweeAuthResponse.access_token;
						mRefreshToken = localJourweeAuthResponse.refresh_token;
						mExpires = localJourweeAuthResponse.getExpiresTimestamp();
						curUser = localJourweeAuthResponse.currentUser;
						onPersistCreds();
					}
					return Response.success(localJourweeAuthResponse, getCacheEntry());
				}
//				catch (JsonParseException localJsonParseException)
//				{
//					Log.w("jourwee:auth", "Error parsing auth response", localJsonParseException);
//				}
				catch(Exception e)
				{
					
				}
				return Response.error(new VolleyError("Unexpected server response"));
			}

			@Override
			protected void deliverResponse(Object response, boolean intermediate)
			{
				// TODO Auto-generated method stub

			}
		};
	}

	String getOauthUrl()
	{
		return this.mHost + "oauth/token";
	}

	private void onPersistCreds()
	{
		this.mStore.edit().putString("jourweeApi_current_user", curUser).putString("jourweeApi_refresh_token", this.mRefreshToken).putString("jourweeApi_access_token", this.mAccessToken).putLong("jourweeApi_access_token_expires", this.mExpires).commit();
	}

	public void addClientCredentials(Map<String, String> paramMap)
	{
		paramMap.put("client_id", getClientKey());
		paramMap.put("client_secret", getClientSecret());
	}

	public static String getClientKey()
	{
		return "e2d331932d981eb2d27544cb28bd87";
	}

	public static String getClientSecret()
	{
		return "f795d18a4cdc97d267c4c1dfb84e24";
	}

	public static class JourweeAuthResponse
	{
		String access_token;
		long expires_in;
		String refresh_token;
		String currentUser;

		public long getExpiresTimestamp()
		{
			return System.currentTimeMillis() + 1000L * expires_in;
		}
	}
}

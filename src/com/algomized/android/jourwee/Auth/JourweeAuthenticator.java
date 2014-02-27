package com.algomized.android.jourwee.auth;

import com.algomized.android.jourwee.view.LoginActivity_;
import com.algomized.android.jourwee.view.RegisterActivity_;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

public class JourweeAuthenticator extends AbstractAccountAuthenticator
{

	private String TAG = "JourweeAuthenticator";
	private final Context mContext;

	public JourweeAuthenticator(Context context)
	{
		super(context);

		// I hate you! Google - set mContext as protected!
		this.mContext = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException
	{
		Log.d(TAG, "> addAccount");

		final Intent intent = new Intent(mContext, LoginActivity_.class);
		intent.putExtra("ACCOUNT_TYPE", accountType);
		intent.putExtra("AUTH_TYPE", authTokenType);
		intent.putExtra("IS_ADDING_NEW_ACCOUNT", true);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException
	{

		Log.d(TAG,"> getAuthToken");

		// If the caller requested an authToken type we don't support, then
		// return an error
		if (!authTokenType.equals("Read only") && !authTokenType.equals("Full access"))
		{
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
			return result;
		}

		// Extract the username and password from the Account Manager, and ask
		// the server for an appropriate AuthToken.
		final AccountManager am = AccountManager.get(mContext);

		String authToken = am.peekAuthToken(account, authTokenType);

		Log.d(TAG, "> peekAuthToken returned - " + authToken);

		// Lets give another try to authenticate the user
		if (TextUtils.isEmpty(authToken))
		{
			final String password = am.getPassword(account);
			if (password != null)
			{
				try
				{
					Log.d(TAG, "> re-authenticating with the existing password");
					authToken = "$2a$12$H8P4No9L.SiGZmeca94Lte2XGvKJFIU3m9WofWW4O7s1T.YAvgvVm";
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		// If we get an authToken - we return it
		if (!TextUtils.isEmpty(authToken))
		{
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			return result;
		}

		// If we get here, then we couldn't access the user's password - so we
		// need to re-prompt them for their credentials. We do that by creating
		// an intent to display our AuthenticatorActivity.
		final Intent intent = new Intent(mContext, LoginActivity_.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		intent.putExtra("ACCOUNT_TYPE", account.type);
		intent.putExtra("AUTH_TYPE", authTokenType);
		intent.putExtra("ACCOUNT_NAME", account.name);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType)
	{
		if ("Full Access".equals(authTokenType))
			return "Full access to an Jourwee account";
		else if ("Read only".equals(authTokenType))
			return "Read only access to an Jourwee account";
		else
			return authTokenType + " (Label)";
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException
	{
		final Bundle result = new Bundle();
		result.putBoolean(KEY_BOOLEAN_RESULT, false);
		return result;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType)
	{
		return null;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException
	{
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException
	{
		return null;
	}
}

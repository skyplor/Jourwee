package com.algomized.android.jourwee.view;

import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.client.ClientProtocolException;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.util.NetworkUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@EActivity(R.layout.location)
public class LocationActivity extends Activity
{
	private static final String LOG_TAG = "LocationActivity";

	String username;

	@ViewById(R.id.logoutBtn)
	Button logout_button;

	@ViewById(R.id.testRequestBtn)
	Button test_request_button;

	@ViewById(R.id.userIDTxt)
	TextView user_id_text;

	@ViewById(R.id.usernameTxt)
	TextView username_text;

	@ViewById(R.id.nameTxt)
	TextView name_text;

	@Click(R.id.logoutBtn)
	public void OnLogoutBtnClicked(View v)
	{
		// Retrieve oauthtoken
		Account account = new Account(username, Constants.AM_ACCOUNT_TYPE);
		AccountManagerFuture<Bundle> accFut = AccountManager.get(this).getAuthToken(account, Constants.AM_AUTH_TYPE, null, this, null, null);
		Bundle authTokenBundle = null;
		try
		{
			authTokenBundle = accFut.getResult();

			String authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
			NetworkUtil nu = new NetworkUtil(this);

			// if (nu.logout(username, authToken))
			// {
			// TODO Also have to logout from AccountManager side. We have to remove only our accountstype.
			nu.removeAccounts();
			StartActivity_.intent(this).start();
			this.finish();

			// }
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (OperationCanceledException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (AuthenticatorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Click(R.id.testRequestBtn)
	public void OnTestRequestBtnClicked(View v)
	{
		testRequest();
	}

	@Background
	void testRequest()
	{
		NetworkUtil nu = new NetworkUtil(this);
		boolean flag = false;
		for (int i = 0; i < 10; i++)
		{
			try
			{
				if(nu.testRequest(false))
				{
					Log.d(LOG_TAG, "Get user successful");
					flag = true;
					break;
				}
				else
				{
					nu.checkLoginStatus(this);
				}
			}
			catch (IOException e)
			{
				Log.d(LOG_TAG, "Encountered IOException: " + e);
			}
		}
		if(!flag)
		{
			Log.d(LOG_TAG, "Unable to get the user. Please try again later.");
		}

	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

		// TODO AccountManager get username
		username = getIntent().getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
		Log.d(LOG_TAG, "username: " + username);

		// user_id_text.setText(user_id_value);
		username_text.setText(username);
	}

}

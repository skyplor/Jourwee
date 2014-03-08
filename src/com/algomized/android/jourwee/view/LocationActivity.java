package com.algomized.android.jourwee.view;

import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
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

//			if (nu.logout(username, authToken))
//			{
				// TODO Also have to logout from AccountManager side. We have to remove only our accountstype.
				nu.removeAccounts();
				StartActivity_.intent(this).start();
				this.finish();
				
//			}
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
		NetworkUtil nu = new NetworkUtil(this);
		nu.testRequest(false);
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

		// String user_id_value = getIntent().getExtras().getString("userId");
		// TODO AccountManager get username
		username = getIntent().getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
		Log.d(LOG_TAG, "username: " + username);

		// user_id_text.setText(user_id_value);
		// username_text.setText(username_value);
	}

	// public static void start(Activity activity)
	// {
	// start(activity, null);
	// }

	// public static void start(Activity activity, Uri uri)
	// {
	// Intent intent = new Intent(activity, LocationActivity_.class);
	// intent.putExtra("stayalive", true);
	// if (uri != null)
	// {
	// intent.putExtra("uploadAvatarUri", uri);
	// }
	// activity.setResult(-1, null);
	// activity.finish();
	// activity.startActivity(intent);
	// activity.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
	// }

}

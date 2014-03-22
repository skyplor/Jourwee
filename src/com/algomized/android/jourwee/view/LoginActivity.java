package com.algomized.android.jourwee.view;

import java.io.IOException;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.client.ClientProtocolException;
import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.model.JourUser;
import com.algomized.android.jourwee.util.NetworkUtil;
import com.android.volley.VolleyLog;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.login)
public class LoginActivity extends AccountAuthenticatorActivity
{
	String LOG_TAG = "Login";
	ProgressDialog dialog;
	Bundle data;
	Intent intent = null;

	@ViewById(R.id.loginBtn)
	Button loginBtn;

	@ViewById(R.id.loginTxtBox)
	EditText loginTxtBox;

	@ViewById(R.id.pwTxtBox)
	EditText pwTxtBox;

	@Click(R.id.loginBtn)
	void onLoginBtnClick(View v)
	{
		String username = loginTxtBox.getText().toString();
		String password = pwTxtBox.getText().toString();
		if (username.equals("") || password.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Oops,  please check your username and password", Toast.LENGTH_LONG).show();
		}
		else
		{
			dialog = new ProgressDialog(this);
			dialog.setMessage("Please hold on while we log you in...");
			dialog.show();
			authenticate(username, password);
		}
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		data = new Bundle();
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	}

	@Background
	public void authenticate(String username, String password)
	{
		JourUser user;
		NetworkUtil nu = new NetworkUtil(username, password, this);
		nu.removeAccounts();
		data.clear();

		try
		{
			user = nu.login();

			if (user.getAccess_token() == null || user.getAccess_token() == "")
			{
				// login is unsuccessful
				publishProgress(-1);

			}
			else
			{
				publishProgress(50);
				data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
				data.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.AM_ACCOUNT_TYPE);
				data.putString(AccountManager.KEY_AUTHTOKEN, user.getAccess_token());
				data.putString(Constants.AM_KEY_REFRESH_TOKEN, user.getRefresh_token());
				data.putString(Constants.AM_KEY_EXPIRES_IN, user.getExpires_in());
				data.putInt(Constants.KEY_USERTYPE, user.getUser_type());
				publishProgress(100);
			}

		}
		catch (ClientProtocolException e1)
		{
			publishProgress(-2);
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e1.toString());
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			publishProgress(-2);
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e1.toString());
			e1.printStackTrace();
		}
		catch (Exception e)
		{
			publishProgress(-2);
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e.toString());
			Log.e(LOG_TAG, "Exception: " + e.toString());
		}
	}

	@UiThread
	public void publishProgress(int progress)
	{
		// Update progress views
		if (progress == 50)
		{
			intent = new Intent(this, LocationActivity_.class);
		}

		else if (progress == 100)
		{
			dialog.dismiss();

			if (data != null)
			{
				intent.putExtras(data);

				if (intent.hasExtra(AccountManager.KEY_ERROR_MESSAGE))
				{
					Toast.makeText(this, intent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
				}
				else
				{
					String oauth = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
					String refresh_token = intent.getStringExtra(Constants.AM_KEY_REFRESH_TOKEN);
					String expires_in = intent.getStringExtra(Constants.AM_KEY_EXPIRES_IN);
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
					LoginActivity.this.setAccountAuthenticatorResult(intent.getExtras());
					LoginActivity.this.setResult(RESULT_OK, intent);
					LoginActivity.this.finish();
				}
			}
		}
		else if (progress == -1)
		{
			dialog.dismiss();
			Toast.makeText(getBaseContext(), "Username and password do not match. Please try again.", Toast.LENGTH_SHORT).show();
		}
		else if (progress == -2)
		{
			dialog.dismiss();
			Toast.makeText(getBaseContext(), "Something went wrong. It's probably our fault. Please try again later.", Toast.LENGTH_SHORT).show();
		}

	}
}
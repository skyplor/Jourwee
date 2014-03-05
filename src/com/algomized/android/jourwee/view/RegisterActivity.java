package com.algomized.android.jourwee.view;

import java.io.IOException;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.client.ClientProtocolException;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.model.User;
import com.algomized.android.jourwee.util.NetworkUtil;
import com.algomized.android.jourwee.Constants;
import com.android.volley.VolleyLog;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.register)
public class RegisterActivity extends Activity
{

	private static final String LOG_TAG = "RegisterActivity";
	ProgressDialog dialog;
	Bundle data;
	Intent intent = null;

	@ViewById(R.id.registrationBtn)
	Button registrationBtn;

	@ViewById(R.id.usernameTxtBox)
	EditText usernameTxtBox;

	@ViewById(R.id.passwordTxtBox)
	EditText passwordTxtBox;

	@Click(R.id.registrationBtn)
	void onRegistrationBtnClick(View v)
	{
		String username = usernameTxtBox.getText().toString();
		String password = passwordTxtBox.getText().toString();
		if (username.equals("") || password.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Oops,  please check your username and password", Toast.LENGTH_LONG).show();
		}
		else
		{
			dialog = new ProgressDialog(this);
			dialog.setMessage("Registering into the system...");
			dialog.show();
			createAccount(username, password);
		}
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
		data = new Bundle();
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
	}

	@Background
	public void createAccount(String username, String password)
	{
		NetworkUtil nu = new NetworkUtil(username, password, this);
		nu.removeAccounts();

		User user_reg;
		try
		{
			user_reg = nu.register();

			if (!user_reg.isStatus())
			{
				// login is unsuccessful
				publishProgress(-1);

			}
			else
			{
				publishProgress(50);
				data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
				data.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
				data.putString(AccountManager.KEY_AUTHTOKEN, user_reg.getMessage());
				publishProgress(100);
			}

		}
		catch (ClientProtocolException e1)
		{
			// TODO Auto-generated catch block
			publishProgress(-2);
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e1.toString());
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
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
					AccountManager mAccountManager = AccountManager.get(getBaseContext());
					String username = intent.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
					final Account account = new Account(username, Constants.ACCOUNT_TYPE);
					if (mAccountManager.addAccountExplicitly(account, "", intent.getExtras()))
					{
						VolleyLog.d("Successfully added account: %s", username);
						mAccountManager.setAuthToken(account, Constants.AUTH_TYPE, oauth);
					}
					else
					{
						VolleyLog.d("Unable to add account");
					}
					RegisterActivity.this.setResult(RESULT_OK, intent);
					RegisterActivity.this.finish();
				}
			}
		}
		else if (progress == -1)
		{
			Toast.makeText(getBaseContext(), "Username and password do not match. Please try again.", Toast.LENGTH_SHORT).show();
		}
		else if (progress == -2)
		{
			Toast.makeText(getBaseContext(), "Something went wrong. It's probably our fault. Please try again later.", Toast.LENGTH_SHORT).show();
		}

	}
}

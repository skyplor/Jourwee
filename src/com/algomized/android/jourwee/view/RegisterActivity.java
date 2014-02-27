package com.algomized.android.jourwee.view;

import java.io.UnsupportedEncodingException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;
import com.algomized.android.jourwee.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.register)
public class RegisterActivity extends Activity
{

	private static final String LOG_TAG = "RegisterActivity";

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
			createAccount(username, password);
		}
	}

	@Background
	public void createAccount(String username, String password)
	{
		ConnectServer cs = new ConnectServer(username, password, this);
		try
		{
			String oauth = cs.register();
			
			AccountManager mAccountManager = AccountManager.get(getBaseContext());
			final Account account = new Account(username, Constants.ACCOUNT_TYPE);
			mAccountManager.addAccountExplicitly(account, "", null);

			Log.d(LOG_TAG, mAccountManager.toString());

			final Intent intent = new Intent();
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
			intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth);//"com.algomized.android.jourwee:$2a$12$H8P4No9L.SiGZmeca94Lte2XGvKJFIU3m9WofWW4O7s1T.YAvgvVm;oauth2");
			// this.setAccountAuthenticatorResult(intent.getExtras());
			this.setResult(RESULT_OK, intent);
			this.finish();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
	}
}

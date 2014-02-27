package com.algomized.android.jourwee.view;

import java.io.UnsupportedEncodingException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;
import com.algomized.android.jourwee.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.login)
public class LoginActivity extends AccountAuthenticatorActivity
{
	String LOG_TAG = "Login";
//	EditText login_idBox, passwordBox;

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
			// connectToServer(username, password);
			authenticate(username, password);
		}
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		ConnectServer cs = new ConnectServer(this);
		// TODO Check whether we have a valid auth token by going to the accountmanager via jourweeauthenticator and get oauth token 
		//cs.testRequest(true);
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	}

	/**
	 * Called when the activity is first created.
	 * 
	 * @param password
	 * @param username
	 */
	// @Override
	// public void onCreate(Bundle savedInstanceState)
	// {
	// super.onCreate(savedInstanceState);
	// no matter what, we have to check if user has a session first.

	// ConnectServer cs = new ConnectServer(this);
	// cs.testRequest(true);

	// setContentView(R.layout.login);

	// login_idBox = (EditText) findViewById(R.id.loginTxtBox);
	// passwordBox = (EditText) findViewById(R.id.pwTxtBox);
	// Button connectServ = (Button) findViewById(R.id.loginBtn);
	// connectServ.setOnClickListener(new OnClickListener()
	// {
	//
	// @Override
	// public void onClick(View v)
	// {
	// String username = login_idBox.getText().toString();
	// String password = passwordBox.getText().toString();
	// if (username.equals("") || password.equals(""))
	// {
	// Toast.makeText(getApplicationContext(), "Oops,  please check your username and password", Toast.LENGTH_LONG).show();
	// }
	// else
	// {
	// connectToServer(username, password);
	// }
	// }
	// });

	// checkLoginStatus();

	// }

	// User is not logged in, proceed to try connecting to server for credentials verification
	// public void connectToServer(String username, String password)
	// {
	// ConnectServer cs = new ConnectServer(username, password, this);
	// try
	// {
	// cs.login();
	// }
	// catch (JsonProcessingException e)
	// {
	// e.printStackTrace();
	// }
	// catch (UnsupportedEncodingException e)
	// {
	// e.printStackTrace();
	// }
	// }

//	@Background
	public void authenticate(String username, String password)
	{
		// TODO Connect to server to get oauth
		ConnectServer cs = new ConnectServer(username, password, this);
		try
		{
			String oauth = cs.login();
			AccountManager mAccountManager = AccountManager.get(getBaseContext());
			final Account account = new Account(username, Constants.ACCOUNT_TYPE);
			mAccountManager.addAccountExplicitly(account, "", null);

			Log.d(LOG_TAG, mAccountManager.toString());

			final Intent intent = new Intent();
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
			intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth);//"com.algomized.android.jourwee:$2a$12$H8P4No9L.SiGZmeca94Lte2XGvKJFIU3m9WofWW4O7s1T.YAvgvVm;oauth2");
			this.setAccountAuthenticatorResult(intent.getExtras());
			this.setResult(RESULT_OK, intent);
			this.finish();
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

}
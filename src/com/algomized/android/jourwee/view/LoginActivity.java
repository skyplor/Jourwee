package com.algomized.android.jourwee.view;

import java.io.UnsupportedEncodingException;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;
import com.algomized.android.jourwee.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;

import android.app.Activity;
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

public class LoginActivity extends ActionBarActivity
{
	String LOG_TAG = "Login";
	EditText login_idBox, passwordBox;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// no matter what, we have to check if user has a session first.
		
		ConnectServer cs = new ConnectServer(this);
		cs.testRequest(true);

		setContentView(R.layout.login);

		login_idBox = (EditText) findViewById(R.id.loginTxtBox);
		passwordBox = (EditText) findViewById(R.id.pwTxtBox);
		Button connectServ = (Button) findViewById(R.id.loginBtn);
		connectServ.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String username = login_idBox.getText().toString();
				String password = passwordBox.getText().toString();
				if (username.equals("") || password.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Oops,  please check your username and password", Toast.LENGTH_LONG).show();
				}
				else
				{
					connectToServer(username, password);
				}
			}
		});

		// checkLoginStatus();

	}

	// User is not logged in, proceed to try connecting to server for credentials verification
	public void connectToServer(String username, String password)
	{
		ConnectServer cs = new ConnectServer(username, password, this);
		try
		{
			cs.login();
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
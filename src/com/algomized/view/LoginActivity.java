package com.algomized.view;

import java.io.UnsupportedEncodingException;

import com.algomized.R;
import com.algomized.R.id;
import com.algomized.R.layout;
import com.algomized.controller.ConnectServer;
import com.fasterxml.jackson.core.JsonProcessingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	String LOG_TAG = "Login";
	EditText login_idBox, passwordBox;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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
		
		checkLoginStatus();
	}

	private void checkLoginStatus()
	{
		// TODO Auto-generated method stub
		ConnectServer cs = new ConnectServer(this);
		if(cs.checkLoginStatus())
		{
			startLocationActivity();
		}
		
	}

	public void connectToServer(String username, String password)
	{
		ConnectServer cs = new ConnectServer(username, password, this);
		try
		{
			if(cs.login())
			{
				startLocationActivity();
			}
		}
		catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.d(LOG_TAG,cs.printUserDetails());
	}

	private void startLocationActivity()
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginActivity.this, LocationActivity.class);
		this.startActivity(intent);
		this.finish();
	}
}
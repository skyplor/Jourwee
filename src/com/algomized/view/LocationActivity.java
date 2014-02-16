package com.algomized.view;

import com.algomized.R;
import com.algomized.controller.ConnectServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LocationActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		
		Button logout_button = (Button) findViewById(R.id.logoutBtn);
		logout_button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				logout();
			}
		});
	}

	private void logout()
	{
		// TODO Auto-generated method stub
		ConnectServer cs = new ConnectServer(this);
		if(cs.logout())
		{
			Intent intent = new Intent(LocationActivity.this, LoginActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

}

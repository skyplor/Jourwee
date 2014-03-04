package com.algomized.android.jourwee.view;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
//		String user_id_value = getIntent().getExtras().getString("userId");
		// TODO AccountManager get username
//		String username_value = getIntent().getExtras().getString("username");
		
		Button logout_button = (Button) findViewById(R.id.logoutBtn);
		Button test_request_button = (Button) findViewById(R.id.testRequestBtn);
		TextView user_id_text = (TextView) findViewById(R.id.userIDTxt);
		TextView username_text = (TextView) findViewById(R.id.usernameTxt);
		TextView name_text = (TextView) findViewById(R.id.nameTxt);
		
		logout_button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				logout();
			}
		});
		
		test_request_button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				testRequest();
			}
		});
		
//		user_id_text.setText(user_id_value);
//		username_text.setText(username_value);
	}

	private void testRequest()
	{
		ConnectServer cs = new ConnectServer(this);
		cs.testRequest(false);
	}

	private void logout()
	{
		ConnectServer cs = new ConnectServer(this);
		cs.logout();
	}
	
	public static void start(Activity activity)
    {
        start(activity, null);
    }

    public static void start(Activity activity, Uri uri)
    {
        Intent intent = new Intent(activity, LocationActivity.class);
        intent.putExtra("stayalive", true);
        if (uri != null)
        {
            intent.putExtra("uploadAvatarUri", uri);
        }
        activity.setResult(-1, null);
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
    }

}

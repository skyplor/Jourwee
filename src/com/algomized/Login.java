package com.algomized;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity
{
    String LOG_TAG = "Login";
    EditText email, password;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        email = (EditText) findViewById(R.id.emailTxtBox);
//		password = (EditText) findViewById(R.id.pwTxtBox);
        Button connectServ = (Button) findViewById(R.id.loginBtn);
        connectServ.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				connectToServer();
			}
		});
    }
    public void connectToServer()
    {
    	ConnectServer cs = new ConnectServer("abc", this);
//        Log.d(LOG_TAG,cs.printUserDetails());
    }
}
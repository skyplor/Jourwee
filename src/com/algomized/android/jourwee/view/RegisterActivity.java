package com.algomized.android.jourwee.view;

import java.io.UnsupportedEncodingException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;
import com.fasterxml.jackson.core.JsonProcessingException;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.register)
public class RegisterActivity extends Activity
{
	
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
			connectToServer(username, password);
		}
	}
	
	private void connectToServer(String username, String password)
	{
		ConnectServer cs = new ConnectServer(username, password, this);
		try
		{
			cs.register();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JsonProcessingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_from_bottom,R.anim.slide_out_to_top);
	}
	
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_from_top,R.anim.slide_out_to_bottom);
	}
}

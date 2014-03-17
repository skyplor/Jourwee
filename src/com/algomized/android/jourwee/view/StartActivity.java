package com.algomized.android.jourwee.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.util.NetworkUtil;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@EActivity(R.layout.start)
// @OptionsMenu(R.menu.start_activity_actions)
public class StartActivity extends ActionBarActivity // implements AuthListener
{
	private static final int REGISTER = 0, LOGIN = 1;
	EditText login_idBox, passwordBox;
	String TAG = "Jourwee - StartActivity.class";

	@ViewById(R.id.RegBtn)
	Button regButton;

	@ViewById(R.id.signinTxt)
	TextView signinTxt;

	@AfterViews
	void init()
	{
		signinTxt.setClickable(true);
		overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
		// Check if a valid token is in AccountManager. If yes, go to LocationActivity_.class. If no, stay here.
		doFirstCheck();
	}

	@Background
	void doFirstCheck()
	{
		NetworkUtil nu = new NetworkUtil(this);
		Bundle bundle = nu.checkLoginStatus(this);
		if (bundle != null && bundle.getString(AccountManager.KEY_AUTHTOKEN) != null)
		{
			startLocationActivity(bundle);
		}

	}

	@UiThread
	void startLocationActivity(Bundle bundle)
	{
		Intent locationIntent = new Intent(this, LocationActivity_.class);
		locationIntent.putExtras(bundle);
		// Start LocationActivity
		startActivity(locationIntent);
		this.finish();

	}

	@Click(R.id.RegBtn)
	void regBtnClicked()
	{
		RegisterActivity_.intent(this).startForResult(REGISTER);
	}

	@Click(R.id.signinTxt)
	void signinTxtClicked()
	{
		LoginActivity_.intent(this).startForResult(LOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (data != null)
		{
			Bundle bundle = data.getExtras();
			if (resultCode == RESULT_OK)
			{
				Intent locationIntent = new Intent(this, LocationActivity_.class);
				locationIntent.putExtras(bundle);
				startActivity(locationIntent);
				this.finish();
			}
		}
	}
}

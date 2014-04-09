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
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.start)
// @OptionsMenu(R.menu.start_activity_actions)
public class StartActivity extends ActionBarActivity // implements AuthListener
{
	private static final int REGISTER = 0, LOGIN = 1;
	EditText login_idBox, passwordBox;
	String LOG_TAG = StartActivity.class.getName();

	private final static String FACEBOOK_SIGIN_URL = "http://algomizedwebserver.elasticbeanstalk.com/api/signin/facebook";
	private static final int FACEBOOK_SIGNIN_REQUEST = 2;

	private final static String TWITTER_SIGIN_URL = "http://algomizedwebserver.elasticbeanstalk.com/api/signin/twitter";
	private static final int TWITTER_SIGNIN_REQUEST = 3;

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
			// startLocationActivity(bundle);
			startRouteActivity(bundle);
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

	@UiThread
	void startRouteActivity(Bundle bundle)
	{
		Intent routeIntent = new Intent(this, RouteActivity_.class);
		routeIntent.putExtras(bundle);
		// Start RouteActivity
		startActivity(routeIntent);
		this.finish();
	}

	@Click
	void RegBtn()
	{
		RegisterActivity_.intent(this).startForResult(REGISTER);
	}

	@Click(R.id.signinTxt)
	void signinTxtClicked()
	{
		LoginActivity_.intent(this).startForResult(LOGIN);
	}

	@Click
	void btn_fb()
	{
		startWebView(FACEBOOK_SIGIN_URL, "scope=publish_stream,offline_access", FACEBOOK_SIGNIN_REQUEST);
	}

	@Click
	void btn_twitter()
	{
		startWebView(TWITTER_SIGIN_URL, null, TWITTER_SIGNIN_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (data != null)
		{
			Bundle bundle = data.getExtras();
			if (resultCode == RESULT_OK)
			{
				// startLocationActivity(bundle);
				switch (requestCode)
				{
					case REGISTER:
						startRouteActivity(bundle);

						break;
					case LOGIN:
						startRouteActivity(bundle);

						break;
					case FACEBOOK_SIGNIN_REQUEST:
						String accessToken = data.getStringExtra("access_token");
						Log.d(LOG_TAG, "Facebook: " + accessToken);
						Toast.makeText(this, accessToken, Toast.LENGTH_LONG).show();
						break;
					case TWITTER_SIGNIN_REQUEST:
						String accessToken1 = data.getStringExtra("access_token");
						Log.d(LOG_TAG, "Twitter: " + accessToken1);
						Toast.makeText(this, accessToken1, Toast.LENGTH_LONG).show();
						break;
				}
			}
		}
	}

	private void startWebView(String url, String params, int requestCode)
	{
		Intent intent = new Intent(this, WebViewActivity_.class);
		intent.putExtra("url", url);
		if (params != null)
		{
			intent.putExtra("params", params);
		}
		startActivityForResult(intent, requestCode);

		if (params != null)
		{

		}
	}
}

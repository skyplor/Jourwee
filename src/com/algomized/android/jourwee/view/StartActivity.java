package com.algomized.android.jourwee.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.util.NetworkUtil;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

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
	private static final int REGISTER = 0, LOGIN = 1, FACEBOOK_SIGNIN_REQUEST = 2, TWITTER_SIGNIN_REQUEST = 3;
	// EditText login_idBox, passwordBox;
	String LOG_TAG = StartActivity.class.getName();

	@ViewById
	TextView signinTxt;

	@ViewById
	LoginButton btn_fb;

	@AfterViews
	void init()
	{
		signinTxt.setClickable(true);
		overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
		// btn_fb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		// btn_fb.setBackgroundResource(R.drawable.btn_facebook);

		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback()
		{

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception)
			{
				if (session.isOpened())
				{
					// make request to the /me API
					Request.newMeRequest(session, new GraphUserCallback()
					{

						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response)
						{
							if (user != null)
							{
//								TextView welcome = (TextView) findViewById(R.id.welcome);
								signinTxt.setText("Hello " + user.getName() + "!");
							}
						}
					});
				}
			}
		});
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

	// @Click
	// void btn_fb()
	// {
	// String fb_url = Constants.BASE_URL + Constants.URL_FBLOGIN;
	// Log.d(LOG_TAG, "Facebook URL: " + fb_url);
	// // startWebView(fb_url, "scope=publish_stream,offline_access", FACEBOOK_SIGNIN_REQUEST);
	// startWebView(fb_url, "publish_stream,offline_access", FACEBOOK_SIGNIN_REQUEST);
	// }

	@Click
	void btn_twitter()
	{
		String tw_url = Constants.BASE_URL + Constants.URL_TWLOGIN;
		Log.d(LOG_TAG, "Twitter URL: " + tw_url);
		startWebView(tw_url, null, TWITTER_SIGNIN_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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
						startRouteActivity(bundle);
						break;
					case TWITTER_SIGNIN_REQUEST:
						String accessToken1 = data.getStringExtra("access_token");
						Log.d(LOG_TAG, "Twitter: " + accessToken1);
						Toast.makeText(this, accessToken1, Toast.LENGTH_LONG).show();
						startRouteActivity(bundle);
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

		// if (params != null)
		// {
		//
		// }
	}
}

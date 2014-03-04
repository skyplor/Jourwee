package com.algomized.android.jourwee.view;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;
import com.algomized.android.jourwee.model.User;
import com.android.volley.VolleyLog;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.login)
public class LoginActivity extends AccountAuthenticatorActivity
{
	String LOG_TAG = "Login";
	ProgressDialog dialog;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	Bundle data;
	Intent intent = null;
	// EditText login_idBox, passwordBox;

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
			dialog = new ProgressDialog(this);
			dialog.setMessage("Please hold on while we log you in...");
			dialog.show();
			authenticate(username, password);
			// LoginTask login = new LoginTask(username, password, this);
			// login.execute();
		}
	}

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		data = new Bundle();
		// ConnectServer cs = new ConnectServer(this);
		// TODO Check whether we have a valid auth token by going to the accountmanager via jourweeauthenticator and get oauth token
		// cs.testRequest(true);
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

	// @Background
	// @UiThread
	// public void authenticate(String username, String password)
	// {
	// // TODO Connect to server to get oauth
	// ConnectServer cs = new ConnectServer(username, password, this);
	// try
	// {
	// cs.removeAccounts();
	// Intent intent = cs.login();
	// if (intent != null)
	// {
	// if (intent.hasExtra("ERROR_MESSAGE"))
	// {
	// Toast.makeText(getBaseContext(), intent.getStringExtra("ERROR_MESSAGE"), Toast.LENGTH_SHORT).show();
	// }
	// else
	// {
	// String oauth = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
	// AccountManager mAccountManager = AccountManager.get(getBaseContext());
	// final Account account = new Account(username, Constants.ACCOUNT_TYPE);
	// if (mAccountManager.addAccountExplicitly(account, "", intent.getExtras()))
	// {
	// VolleyLog.d("Successfully added account: %s", username);
	// mAccountManager.setAuthToken(account, Constants.AUTH_TYPE, oauth);
	// }
	// else
	// {
	// VolleyLog.d("Unable to add account");
	// }
	// // Log.d(LOG_TAG, mAccountManager.toString());
	//
	// // final Intent intent = new Intent();
	// // intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
	// // intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
	// // intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth);
	// this.setAccountAuthenticatorResult(intent.getExtras());
	// this.setResult(RESULT_OK, intent);
	// this.finish();
	// }
	// }
	// else
	// {
	// VolleyLog.d("Intent is null");
	// }
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

	@Background
	public void authenticate(String username, String password)
	{
		ConnectServer cs = new ConnectServer(username, password, this);
		cs.removeAccounts();
		nameValuePairs.add(new BasicNameValuePair("j_username", username));
		nameValuePairs.add(new BasicNameValuePair("j_password", password));

		try
		{
			// intent = cs.login();
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.LOGIN);

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			ObjectMapper mapper = new ObjectMapper();
			String result = EntityUtils.toString(response.getEntity());

			Log.d(LOG_TAG, "Result: " + result);

			User user = mapper.readValue(result, User.class);
			Log.d(LOG_TAG, "User message: " + user.getMessage());

			if (!user.isStatus())
			{
				// login is unsuccessful
				publishProgress(-1);

			}
			else
			{
				// // Log.d(LOG_TAG, "Cookies number: " + myCookieStore.getCookies().size());
				// // Cookie cookie = myCookieStore.getCookies().get(0);
				// // Log.d(LOG_TAG, "Cookie: " + cookie.getName() + " Value: " + cookie.getValue());
				// Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
				// Constants.LOGIN_STATUS = true;
				// Log.d(LOG_TAG, "Response: " + response);
				publishProgress(50);
				data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
				data.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
				data.putString(AccountManager.KEY_AUTHTOKEN, user.getMessage());
				publishProgress(100);
			}

		}
		catch (Exception e)
		{
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e.toString());
			Log.e(LOG_TAG, "Error in http connection " + e.toString());

		}
	}

	@UiThread
	public void publishProgress(int progress)
	{
		// Update progress views
		if (progress == 50)
		{
			intent = new Intent(this, LocationActivity.class);
		}

		else if (progress == 100)
		{
			dialog.dismiss();

			if (data != null)
			{
				intent.putExtras(data);
				
				if (intent.hasExtra(AccountManager.KEY_ERROR_MESSAGE))
				{
					Toast.makeText(this, intent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
				}
				else
				{
					String oauth = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
					AccountManager mAccountManager = AccountManager.get(getBaseContext());
					String username = intent.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
					final Account account = new Account(username, Constants.ACCOUNT_TYPE);
					if (mAccountManager.addAccountExplicitly(account, "", intent.getExtras()))
					{
						VolleyLog.d("Successfully added account: %s", username);
						mAccountManager.setAuthToken(account, Constants.AUTH_TYPE, oauth);
					}
					else
					{
						VolleyLog.d("Unable to add account");
					}
					// Log.d(LOG_TAG, mAccountManager.toString());

					// final Intent intent = new Intent();
					// intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
					// intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
					// intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth);
					LoginActivity.this.setAccountAuthenticatorResult(intent.getExtras());
					LoginActivity.this.setResult(RESULT_OK, intent);
					LoginActivity.this.finish();
				}
			}
		}
		else if (progress == -1)
		{
			Toast.makeText(getBaseContext(), "Username and password do not match. Please try again.", Toast.LENGTH_SHORT).show();
		}

	}

//	private final class LoginTask extends AsyncTask<Void, Void, Intent>
//	{
//		Context context;
//		String username, password;
//		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		Bundle data = new Bundle();
//
//		public LoginTask(String username, String password, Context context)
//		{
//			this.username = username;
//			this.password = password;
//			this.context = context;
//		}
//
//		@Override
//		protected void onPreExecute()
//		{
//			dialog = new ProgressDialog(getApplicationContext());
//			dialog.setMessage("Please hold on while we log you in...");
//			dialog.show();
//			nameValuePairs.add(new BasicNameValuePair("j_username", username));
//			nameValuePairs.add(new BasicNameValuePair("j_password", password));
//		}
//
//		@Override
//		protected Intent doInBackground(Void... params)
//		{
//			// TODO HTTP Post
//			Intent intent = null;
//			// ConnectServer cs = new ConnectServer(username, password, context);
//			try
//			{
//				// intent = cs.login();
//				HttpClient httpclient = new DefaultHttpClient();
//
//				HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.LOGIN);
//
//				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//				HttpResponse response = httpclient.execute(httppost);
//
//				ObjectMapper mapper = new ObjectMapper();
//				String result = EntityUtils.toString(response.getEntity());
//
//				User user = mapper.readValue(result, User.class);
//
//				if (!user.isStatus())
//				{
//					// login is unsuccessful
//					Toast.makeText(context, "Username and password do not match. Please try again.", Toast.LENGTH_SHORT).show();
//				}
//				else
//				{
//					// // Log.d(LOG_TAG, "Cookies number: " + myCookieStore.getCookies().size());
//					// // Cookie cookie = myCookieStore.getCookies().get(0);
//					// // Log.d(LOG_TAG, "Cookie: " + cookie.getName() + " Value: " + cookie.getValue());
//					// Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
//					// Constants.LOGIN_STATUS = true;
//					// Log.d(LOG_TAG, "Response: " + response);
//					intent = new Intent(context, LocationActivity.class);
//					data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
//					data.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
//					data.putString(AccountManager.KEY_AUTHTOKEN, user.getMessage());
//				}
//
//			}
//			catch (Exception e)
//			{
//				data.putString(AccountManager.KEY_ERROR_MESSAGE, e.toString());
//				Log.e(LOG_TAG, "Error in http connection " + e.toString());
//
//			}
//
//			intent.putExtras(data);
//			// }
//			// catch (JsonProcessingException e)
//			// {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//			// catch (UnsupportedEncodingException e)
//			// {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//			return intent;
//		}
//
//		@Override
//		protected void onPostExecute(Intent intent)
//		{
//			// Dismiss the progress dialog
//			dialog.dismiss();
//
//			if (intent != null)
//			{
//				if (intent.hasExtra("ERROR_MESSAGE"))
//				{
//					Toast.makeText(getBaseContext(), intent.getStringExtra("ERROR_MESSAGE"), Toast.LENGTH_SHORT).show();
//				}
//				else
//				{
//					String oauth = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
//					AccountManager mAccountManager = AccountManager.get(getBaseContext());
//					final Account account = new Account(username, Constants.ACCOUNT_TYPE);
//					if (mAccountManager.addAccountExplicitly(account, "", intent.getExtras()))
//					{
//						VolleyLog.d("Successfully added account: %s", username);
//						mAccountManager.setAuthToken(account, Constants.AUTH_TYPE, oauth);
//					}
//					else
//					{
//						VolleyLog.d("Unable to add account");
//					}
//					// Log.d(LOG_TAG, mAccountManager.toString());
//
//					// final Intent intent = new Intent();
//					// intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
//					// intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
//					// intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth);
//					LoginActivity.this.setAccountAuthenticatorResult(intent.getExtras());
//					LoginActivity.this.setResult(RESULT_OK, intent);
//					LoginActivity.this.finish();
//				}
//			}
//		}
//
//	}
}
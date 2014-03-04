package com.algomized.android.jourwee.view;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.controller.ConnectServer;
import com.algomized.android.jourwee.model.User;
import com.algomized.android.jourwee.Constants;
import com.android.volley.VolleyLog;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@EActivity(R.layout.register)
public class RegisterActivity extends Activity
{

	private static final String LOG_TAG = "RegisterActivity";
	ProgressDialog dialog;
	// ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	Bundle data;
	Intent intent = null;

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
			dialog = new ProgressDialog(this);
			dialog.setMessage("Registering into the system...");
			dialog.show();
			createAccount(username, password);
		}
	}

	// @Background
	// @UiThread
	// public void createAccount(String username, String password)
	// {
	// ConnectServer cs = new ConnectServer(username, password, this);
	// try
	// {
	// cs.removeAccounts();
	// Intent intent = cs.register();
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
	// VolleyLog.d("Successfully added account: %s AuthToken: %s", username, oauth);
	// mAccountManager.setAuthToken(account, Constants.AUTH_TYPE, oauth);
	// }
	// // Log.d(LOG_TAG, mAccountManager.toString());
	//
	// // final Intent intent = new Intent();
	// // intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
	// // intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
	// // intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth);// "com.algomized.android.jourwee:$2a$12$H8P4No9L.SiGZmeca94Lte2XGvKJFIU3m9WofWW4O7s1T.YAvgvVm;oauth2");
	// // this.setAccountAuthenticatorResult(intent.getExtras());
	// this.setResult(RESULT_OK, intent);
	// this.finish();
	// }
	// }
	// else
	// {
	// Log.d(LOG_TAG, "intent is null");
	// }
	// }
	// catch (UnsupportedEncodingException e)
	// {
	// e.printStackTrace();
	// }
	// catch (JsonProcessingException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
		data = new Bundle();
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
	}

	@Background
	public void createAccount(String username, String password)
	{
		ConnectServer cs = new ConnectServer(username, password, this);
		cs.removeAccounts();

		User user_reg = new User();
		user_reg.setUsername(username);
		user_reg.setPassword(password);
		user_reg.setEnabled(true);
		Map<String, String> user_role_map = new HashMap<String, String>();
		user_role_map.put("authority", "ROLE_USER");
		List<Map<String, String>> user_role = new ArrayList<Map<String, String>>();
		user_role.add(user_role_map);
		user_reg.setUserRoles(user_role);
		ObjectMapper mapper_reg = new ObjectMapper();
		mapper_reg.setSerializationInclusion(Include.NON_NULL);
		String jsonString;
		try
		{
			jsonString = mapper_reg.writeValueAsString(user_reg);

			Log.d(LOG_TAG, "JSON STRING: " + jsonString);
			StringEntity sEntity;

			sEntity = new StringEntity(jsonString, "UTF-8");

			// intent = cs.login();
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(Constants.BASE_URL + Constants.REGISTER);

			httppost.setHeader("Content-Type", "application/json");

			httppost.setEntity(sEntity);

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
		catch (JsonProcessingException e1)
		{
			// TODO Auto-generated catch block
			publishProgress(-2);
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e1.toString());
			e1.printStackTrace();
		}
		catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			publishProgress(-2);
			data.putString(AccountManager.KEY_ERROR_MESSAGE, e1.toString());
			e1.printStackTrace();
		}
		catch (Exception e)
		{
			publishProgress(-2);
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
					RegisterActivity.this.setResult(RESULT_OK, intent);
					RegisterActivity.this.finish();
				}
			}
		}
		else if (progress == -1)
		{
			Toast.makeText(getBaseContext(), "Username and password do not match. Please try again.", Toast.LENGTH_SHORT).show();
		}
		else if (progress == -2)
		{
			Toast.makeText(getBaseContext(), "Something went wrong. It's probably our fault. Please try again later.", Toast.LENGTH_SHORT).show();
		}

	}
}

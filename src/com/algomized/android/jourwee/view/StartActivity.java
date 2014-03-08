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
		if(bundle != null && bundle.getString(AccountManager.KEY_AUTHTOKEN) != null)
		{
			startLocationActivity(bundle);
		}
		
	}

	@UiThread
	void startLocationActivity(Bundle bundle)
	{
		Intent locationIntent = new Intent(this, LocationActivity_.class);
		locationIntent.putExtras(bundle);
		// Go to LocationActivity
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

	// private void InitViewPager() {
	// // fragments = new ArrayList<BaseFragment>();
	//
	// regFragment_ = new RegisterFragment_();
	//
	// vpHospital.setAdapter(new FmtPagerAdapter(getSupportFragmentManager(), fragments));
	// vpHospital.setOnPageChangeListener(changeListener);
	// tvProfiles.setTextColor(getResources().getColor(R.color.black_txt));
	// }

	// @OptionsItem({ R.id.action_search, R.id.action_search2 })
	// void multipleMenuItems()
	// {
	// Log.d(TAG, "menu item clicked!");
	//
	// }
	// private boolean mAllowRegister;

	/** Called when the activity is first created. */
	// @Override
	// public void onCreate(Bundle savedInstanceState)
	// {
	// super.onCreate(savedInstanceState);
	//
	// ActionBar localActionBar = getSupportActionBar();
	// if (localActionBar != null)
	// localActionBar.hide();
	// if (Util.checkLoginStatus(this))
	// {
	// Log.d("jourwee", "login > signed in! gotoDashboard");
	// gotoLocation();
	// return;
	// }
	// if ((getIntent() != null) && (getIntent().getBooleanExtra("preventRegister", false)))
	// ;
	// for (boolean bool = false;; bool = true)
	// {
	// mAllowRegister = bool;
	// if (savedInstanceState != null)
	// break;
	// if (mAllowRegister)
	// {
	// FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
	// localFragmentTransaction.replace(R.id.content, StaticFragment.newInstance(R.layout.intro_notlogged));
	// localFragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	// localFragmentTransaction.commit();
	// }
	// if ((getIntent() == null) || (!getIntent().getBooleanExtra("jumpToSignin", false)))
	// break;
	// jumpToSignin(false);
	// return;
	// }
	// setContentView(R.layout.start);
	// }
	//
	// public void GotoSignIn(View paramView)
	// {
	// jumpToSignin(true);
	// }
	//
	// private void jumpToSignin(boolean paramBoolean)
	// {
	// getWindow().setSoftInputMode(0);
	// getSupportActionBar().show();
	// FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
	// if (paramBoolean)
	// localFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.flip_in, R.anim.flip_out);
	// while (true)
	// {
	// localFragmentTransaction.replace(R.id.content, SignInFragment.newInstance(this));
	// if (this.mAllowRegister)
	// localFragmentTransaction.addToBackStack("signin");
	// localFragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	//
	// localFragmentTransaction.commit();
	//
	// return;
	// }
	// }
	//
	// protected void gotoLocation()
	// {
	// LocationActivity.start(this);
	// }
	//
	// public void SignIn(View paramView)
	// {
	// login_idBox = (EditText) findViewById(R.id.loginTxtBox);
	// passwordBox = (EditText) findViewById(R.id.pwTxtBox);
	// String str1 = getTextSafe(login_idBox);
	// String str2 = getTextSafe(passwordBox);
	// boolean bool1 = TextUtils.isEmpty(str1);
	// boolean bool2 = TextUtils.isEmpty(str2);
	// if ((!bool1) && (!bool2))
	// SignIn(str1, str2);
	// do
	// {
	// if ((bool1) && (login_idBox != null))
	// login_idBox.setError(getString(R.string.error_email));
	// }
	// while ((!bool2) || (passwordBox == null));
	// passwordBox.setError(getString(R.string.error_password));
	// }
	//
	// public void SignIn(String paramString1, String paramString2)
	// {
	// showDialog(100);
	// // JourweeApe localJourweeApe = JourweeApe.getInstance(this);
	// // Preferences.configureProxy(this, localJourweeApe);
	// // localJourweeApe.authenticate(new UserPassCreds(paramString1, paramString2), this);
	// Util.authenticate(new UserPassCreds(paramString1, paramString2), this, this.getApplicationContext());
	// }
	//
	// private static String getTextSafe(TextView paramTextView)
	// {
	// if (paramTextView == null)
	// ;
	// CharSequence localCharSequence;
	// do
	// {
	// localCharSequence = paramTextView.getText();
	// }
	// while (localCharSequence == null);
	// return localCharSequence.toString();
	// }
	//
	// @Override
	// public void onAuthResult(Result result)
	// {
	// switch (result.getType())
	// {
	// default:
	// removeDialog(100);
	// if (passwordBox != null)
	// {
	// if ((result.getJsonErrorResponse() != null) || (result.getType() == Result.Type.ERROR_CLIENT))
	// passwordBox.setError(getString(R.string.signin_error));
	// }
	// else
	// return;
	// break;
	// case SUCCESS:
	// JourweeApe.getInstance(this).getActiveUser(this);
	// return;
	// }
	// String str = StatusToast.pickErrorString(this, result);
	// JourweeDialogBuilder localJourweeDialogBuilder = new JourweeDialogBuilder(this);
	// try
	// {
	// localJourweeDialogBuilder.setMessage(str).setTitle(R.string.error_signin).setPositiveButton(R.string.ok, null).show();
	// return;
	// }
	// catch (Exception localBadTokenException)
	// {
	// }
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
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

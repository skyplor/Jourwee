package com.algomized.android.jourwee.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

@EActivity(R.layout.route)
public class RouteActivity extends Activity
{
	private static final String LOG_TAG = RouteActivity.class.getName();
	int userType;
	private final String driverStr = "Find a rider";
	private final String riderStr = "Find a driver";

	@ViewById
	EditText originInput;

	@ViewById
	EditText destInput;

	@ViewById
	Button searchBtn;

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			userType = bundle.getInt(Constants.KEY_USERTYPE);
			if (userType == 0)
				searchBtn.setText(driverStr);
			else
				searchBtn.setText(riderStr);
		}
		else
			searchBtn.setText("Do a search");
	}

	@Click
	void searchBtn()
	{
		// Button is clicked...
		Log.d(LOG_TAG, "Search Button is clicked!");
		// Go to MapActivity
		Intent mapIntent = new Intent(this, MapsActivity_.class);
		startActivity(mapIntent);
		this.finish();
	}

}

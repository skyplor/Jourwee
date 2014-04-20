package com.algomized.android.jourwee.view.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.R;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

@EFragment(R.layout.user_details_fragment)
public class UserDetailsFragment extends Fragment
{
	// This displays the selected user's details (when user taps on the markers on the map)

	@ViewById
	ScrollView userDetails;

	@ViewById
	TextView userName;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		setUserName("Hello World!");
	}

	public void setUserName(String username)
	{
		userName.setText(username);
	}

}

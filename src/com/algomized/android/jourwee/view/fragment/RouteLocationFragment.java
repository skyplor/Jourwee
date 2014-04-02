package com.algomized.android.jourwee.view.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.util.Communicator;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

@EFragment(R.layout.route_fragment)
public class RouteLocationFragment extends Fragment
{
	private static final String LOG_TAG = RouteLocationFragment.class.getName();

	@ViewById
	AutoCompleteTextView originInput;

	@ViewById
	AutoCompleteTextView destInput;

	Communicator comm;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		comm = (Communicator) getActivity();
	}

	@Touch
	void originInput(View v, MotionEvent event)
	{
		if (MotionEvent.ACTION_UP == event.getAction())
		{

			// Intent intent = new Intent(this, SearchLocationActivity_.class);
			// intent.setAction(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, "");
			// startActivityForResult(intent, ORIGIN);

			// Send back to main activity to show action bar for searching of location
			// getActivity().sendBroadcast(new Intent().setAction("SEARCH_ORIGIN"));
			Log.d(LOG_TAG, "Captured touch event");
			comm.startActionBarSearch(Constants.ORIGIN);
		}
	}

	@Touch
	void destInput(View v, MotionEvent event)
	{
		if (MotionEvent.ACTION_UP == event.getAction())
		{

			// Intent intent = new Intent(this, SearchLocationActivity_.class);
			// intent.setAction(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, "");
			// startActivityForResult(intent, ORIGIN);

			// Send back to main activity to show action bar for searching of location
			// getActivity().sendBroadcast(new Intent().setAction("SEARCH_DEST"));
			Log.d(LOG_TAG, "Captured touch event");
			comm.startActionBarSearch(Constants.DESTINATION);
		}
	}

	public void insertText(String data, int inputType)
	{
		if (inputType == Constants.ORIGIN)
		{
			originInput.setText(data);
		}
		else if (inputType == Constants.DESTINATION)
		{
			destInput.setText(data);
		}
		else
		{
			// Don't know what to do if not this 2.
		}
	}
}

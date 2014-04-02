package com.algomized.android.jourwee.view.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.model.JourLocation;
import com.algomized.android.jourwee.util.Communicator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@EFragment(R.layout.locationlist_fragment)
public class LocationListFragment extends Fragment
{
	@ViewById
	ListView locationList;
	
	Communicator comm;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return null;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.locations, android.R.layout.simple_list_item_1);
		locationList.setAdapter(adapter);
		comm = (Communicator) getActivity();
	}
	
	@ItemClick
	void locationList(String location)
	{
		comm.respond(location, Constants.DESTINATION);
	}
}

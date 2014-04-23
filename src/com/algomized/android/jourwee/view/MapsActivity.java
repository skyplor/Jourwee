package com.algomized.android.jourwee.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Touch;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.events.BusProvider;
import com.algomized.android.jourwee.events.JourUserAvailableEvent;
import com.algomized.android.jourwee.model.JourLocation;
import com.algomized.android.jourwee.model.JourUser;
import com.algomized.android.jourwee.view.fragment.UserDetailsFragment;
import com.algomized.android.jourwee.view.fragment.UserListFragment;
import com.android.volley.VolleyLog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

@EActivity(R.layout.map)
public class MapsActivity extends Activity
{
	private GoogleMap mMap;

	@FragmentById
	MapFragment map_fragment;

	@FragmentById
	// UserDetailsFragment userDetails_fragment;
	UserListFragment userList_fragment;

	// @Extra
	List<JourUser> jUserList;

	// Injected object
	@Bean
	BusProvider busProvider;

	// the JourUser being displayed.
	private JourUser jUser;

	// private Thread thread;

	@AfterViews
	void init()
	{
		busProvider.getEventBus().register(jUserSummaryEventHandler);
		mMap = map_fragment.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// TODO Centers on the origin's position
//		final LatLng CIU = new LatLng(1.3667, 103.8);
		LatLng CIU = new LatLng(1.3667, 103.8);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(CIU).zoom(11).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//		Marker ciu = mMap.addMarker(new MarkerOptions().position(CIU).title("Singapore").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
		HashMap<Integer, Marker> markerMap = new HashMap<Integer, Marker>();
		// TODO add all markers into hashmap, and denote each with an ID (maybe the user's id?)
		// TODO then to display, we loop through the hashmap, get each marker and add to map.
		// TODO when user clicks on marker, we can use it as key? and get the relevant information (maybe jouruser object?) and populate the scrollview

		/*
		 * Some method in network util to communicate with server and retrieve list of users. Maybe have to implement sync task? do we want to make users to wait? maybe 1min for both side? Think of the design..user input route den wait for how long? cna he input beforehand? or isit Now i wanna leave?
		 */

		if (this.jUser != null)
		{
			jUserList = new ArrayList<JourUser>();
			jUserList.add(jUser);

			for (JourUser jUser : jUserList)
			{
				// If the marker isn't already being displayed
				if (!markerMap.containsKey(jUser.getId()))
				{
					// Add the Marker to the Map and keep track of it
					VolleyLog.d("jUser Location: " + jUser.getRoute().getOrigin().getGeometry().getLocation().getLat() + ", " + jUser.getRoute().getOrigin().getGeometry().getLocation().getLng());
					markerMap.put(jUser.getId(), this.mMap.addMarker(getMarkerForObject(jUser)));
				}
			}

			// Also populate listview fragment
			userList_fragment.setAdapter(jUserList);

		}
		// TODO Use a OnInfoWindowClickListener to find the object id of the tapped marker in your Map and do something with the corresponding data,
	}

	private MarkerOptions getMarkerForObject(JourUser user)
	{
		// TODO Based on JourUser's lat and lng, we create a markeroptions object
		JourLocation jLocation = user.getRoute().getOrigin().getGeometry().getLocation();
		LatLng CIU = new LatLng(jLocation.getLat(), jLocation.getLng());
//		LatLng CIU = new LatLng(1.3667, 103.8);
		MarkerOptions markerOp = new MarkerOptions();
		markerOp.position(CIU);
		markerOp.title(user.getUsername());
		markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start));
		return markerOp;
	}

	/**
	 * Injection has taken place. Register to retrieve the latest contact object from the bus. We use an anonymous class to handle the events (see comments at the bottom of the class).
	 */
	@AfterInject
	public void postInjection()
	{

	}

	/**
	 * Unregister from the eventbus.
	 */
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		busProvider.getEventBus().unregister(jUserSummaryEventHandler);
	}

	/**
	 * Otto has a limitation (as per design) that it will only find methods on the immediate class type. As a result, if at runtime this instance actually points to a subclass implementation, the methods registered in this class will not be found. This immediately becomes a problem when using the AndroidAnnotations framework as it always produces a subclass of annotated classes.
	 * 
	 * To get around the class hierarchy limitation, one can use a separate anonymous class to handle the events.
	 */
	private Object jUserSummaryEventHandler = new Object()
	{
		/**
		 * Subscription method to receive the latest contact
		 */
		@Subscribe
		public void jUserAvailable(JourUserAvailableEvent event)
		{
			jUser = event.getJUser();
		}
	};

	/*
	 * @Touch public void userDetails_fragment(View v, MotionEvent event) { userDetails_fragment.setUserName("Hello World Sky!"); }
	 */

	// @Override
	// public boolean onTouchEvent(MotionEvent evt)
	// {
	// if(evt.getAction() == MotionEvent.ACTION_DOWN)
	// {
	// userDetails_fragment.setUserName("Hello World Sky!");
	// }
	// return true;
	// }
}

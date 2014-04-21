package com.algomized.android.jourwee.view;

import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Touch;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.model.JourLocation;
import com.algomized.android.jourwee.model.JourUser;
import com.algomized.android.jourwee.view.fragment.UserDetailsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
	UserDetailsFragment userDetails_fragment;
	
//	private Thread thread;

	@AfterViews
	void init()
	{
		mMap = map_fragment.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// TODO Centers on the origin's position
		final LatLng CIU = new LatLng(1.3667, 103.8);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(CIU).zoom(11).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		Marker ciu = mMap.addMarker(new MarkerOptions().position(CIU).title("Singapore").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
		HashMap<Integer, Marker> markerMap = new HashMap<Integer, Marker>();
		// TODO add all markers into hashmap, and denote each with an ID (maybe the user's id?)
		// TODO then to display, we loop through the hashmap, get each marker and add to map.
		// TODO when user clicks on marker, we can use it as key? and get the relevant information (maybe jouruser object?) and populate the scrollview
		List<JourUser> jUsers = null;
		for(JourUser obj : jUsers)
		{
		     //If the marker isn't already being displayed
		     if(!markerMap.containsKey(obj.getId()))
		     {
		         //Add the Marker to the Map and keep track of it 
		         markerMap.put(obj.getId(), this.mMap.addMarker(getMarkerForObject(obj)));
		     }
		}
		// TODO Use a OnInfoWindowClickListener to find the object id of the tapped marker in your Map and do something with the corresponding data,
	}
	
	private MarkerOptions getMarkerForObject(JourUser user)
	{
		// TODO Based on JourUser's lat and lng, we create a markeroptions object
		JourLocation jLocation = user.getRoute().getOrigin().getGeometry().getLocation();
		LatLng CIU = new LatLng(jLocation.getLat(), jLocation.getLat());
		MarkerOptions markerOp = new MarkerOptions();
		markerOp.position(CIU);
		markerOp.title("Singapore");
		markerOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start));
		return markerOp;
	}

	@Touch
	public void userDetails_fragment(View v, MotionEvent event)
	{
		userDetails_fragment.setUserName("Hello World Sky!");
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent evt)
//	{
//	    if(evt.getAction() == MotionEvent.ACTION_DOWN)
//	    {
//	    	userDetails_fragment.setUserName("Hello World Sky!");
//	    }
//	    return true;
//	}    
}

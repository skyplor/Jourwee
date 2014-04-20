package com.algomized.android.jourwee.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Touch;

import com.algomized.android.jourwee.R;
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
		final LatLng CIU = new LatLng(1.3667, 103.8);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(CIU).zoom(11).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		Marker ciu = mMap.addMarker(new MarkerOptions().position(CIU).title("Singapore").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
		// thread= new Thread(){
		// @Override
		// public void run(){
		// try {
		// synchronized(this){
		// wait(3000);
		// userDetails_fragment.setUserName("Hello World Sky!");
		// }
		// }
		// catch(InterruptedException ex){
		// }
		//
		// // TODO
		// }
		// };
		//
		// thread.start();
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

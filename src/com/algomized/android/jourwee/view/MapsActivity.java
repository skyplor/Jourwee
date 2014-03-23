package com.algomized.android.jourwee.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import com.algomized.android.jourwee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;

@EActivity(R.layout.map)
public class MapsActivity extends Activity
{
	private GoogleMap mMap;
	
	@FragmentById
	MapFragment map;

	@AfterViews
	void init()
	{
		mMap = map.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		final LatLng CIU = new LatLng(1.3667, 103.8);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(CIU).zoom(11).build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		Marker ciu = mMap.addMarker(new MarkerOptions().position(CIU).title("Singapore").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
	}
}

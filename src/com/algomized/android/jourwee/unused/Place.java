package com.algomized.android.jourwee.unused;

import java.io.Serializable;

import android.database.Cursor;
import android.util.Log;

import com.google.api.client.util.Key;

/**
 * Implement this class from "Serializable" So that you can pass this class Object to another using Intents Otherwise you can't pass to another actitivy
 * */
public class Place implements Serializable
{

	@Key
	public String id;

	@Key
	public String name;

	@Key
	public String reference;

	@Key
	public String icon;

	@Key
	public String vicinity;

	@Key
	public Geometry geometry;

	@Key
	public String formatted_address;

	@Key
	public String formatted_phone_number;

	public static Place newInstance(Cursor c)
	{
		Place place = new Place();
		// For Search: 0 = _id, 1 = description, 2 = lat, 3 = lng
		place.id = c.getString(c.getColumnIndex("_id"));
		place.name = c.getString(c.getColumnIndex("description"));
		Log.d("PLACE", "lat: " + c.getColumnIndex("lat"));
		Geometry geometry = new Geometry();
		Location location = new Location();
		location.lat = Double.parseDouble(c.getString(c.getColumnIndex("lat")));
		location.lng = Double.parseDouble(c.getString(c.getColumnIndex("lng")));
		// location.lat = c.getString(c.getColumnIndex("lat"));
		// location.lng = c.getString(c.getColumnIndex("lng"));

		return place;
	}

	@Override
	public String toString()
	{
		return name + " - " + id + " - " + reference;
	}

	public static class Geometry implements Serializable
	{
		@Key
		public Location location;
	}

	public static class Location implements Serializable
	{
		@Key
		 public double lat;
//		public String lat;

		@Key
		 public double lng;
//		public String lng;
	}

}

package com.algomized.android.jourwee.util.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.algomized.android.jourwee.model.JourPlace;
import com.algomized.android.jourwee.model.JourPlaceDetails;
import com.algomized.android.jourwee.model.JourPlaceList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class PlaceJSONParser<T>
{
//	 T stands for "Type"
	private Class<T> targetClass;

	private T jPlace;

	// boolean search_flag = false;
	//
	// public PlaceJSONParser(int search)
	// {
	// if (search == 1)
	// {
	// search_flag = true;
	// }
	// }
	// public void set(T t)
	// {
	// this.targetClass = t;
	// }
	//
	// public T getT()
	// {
	// return targetClass;
	// }
	
	public PlaceJSONParser(Class<T> cls)
	{
		targetClass = cls;
	}
	
	public T unmarshal(String json)
	{
		// Class<T> jPlaceList = new
		// JourPlaceDetails jPlaceDetails = new JourPlaceDetails();
		ObjectMapper mapper = new ObjectMapper();

		// AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		//
		// mapper.getDeserializationConfig().withInsertedAnnotationIntrospector(introspector);
		//
		// mapper.getSerializationConfig().withInsertedAnnotationIntrospector(introspector);
		//
		// JavaType type = mapper.getTypeFactory().constructCollectionType(targetClass.getClass(), T);
		try
		{
			// if (getT().getClass() == JourPlaceList.class)
			// {
			// jPlaceList = mapper.readValue(json, JourPlaceList.class);
			// return jPlaceList;
			// }
			// else if (getT().getClass() == JourPlaceDetails.class)
			// {
			// jPlaceDetails = mapper.readValue(json, JourPlaceDetails.class);
			// }
			jPlace = mapper.readValue(json, targetClass);

		}
		catch (JsonParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JsonMappingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jPlace;
	}

	// /** Receives a JSONObject and returns a list */
	// public List<HashMap<String, String>> parse(JSONObject jObject)
	// {
	//
	// JSONArray jPlaces = null;
	// try
	// {
	// /** Retrieves all the elements in the 'places' array */
	// if (search_flag)
	// {
	// jPlaces = jObject.getJSONArray("results");
	// }
	// else
	// {
	// jPlaces = jObject.getJSONArray("predictions");
	//
	// }
	// }
	// catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// /**
	// * Invoking getPlaces with the array of json object where each json object represent a place
	// */
	// return getPlaces(jPlaces);
	// }
	//
	// private List<HashMap<String, String>> getPlaces(JSONArray jPlaces)
	// {
	// int placesCount = jPlaces.length();
	// List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
	// HashMap<String, String> place = null;
	//
	// /** Taking each place, parses and adds to list object */
	// for (int i = 0; i < placesCount; i++)
	// {
	// try
	// {
	// /** Call getPlace with place JSON object to parse the place */
	// place = getPlace((JSONObject) jPlaces.get(i));
	// placesList.add(place);
	//
	// }
	// catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// }
	//
	// return placesList;
	// }
	//
	// /** Parsing the Place JSON object */
	// private HashMap<String, String> getPlace(JSONObject jPlace)
	// {
	//
	// HashMap<String, String> place = new HashMap<String, String>();
	//
	// String id = "";
	// String reference = "";
	// String description = "";
	// String formatted_address = "";
	// String icon ="";
	// String name ="";
	//
	// try
	// {
	//
	// if (search_flag)
	// {
	// formatted_address = jPlace.getString("formatted_address");
	// icon = jPlace.optString("icon");
	// name = jPlace.optString("name");
	// }
	// else
	// {
	// description = jPlace.getString("description");
	// id = jPlace.getString("id");
	// reference = jPlace.getString("reference");
	//
	// place.put("description", description);
	// place.put("_id", id);
	// place.put("reference", reference);
	// }
	//
	// }
	// catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// return place;
	// }
}

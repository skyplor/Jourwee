package com.algomized.android.jourwee.model;

import android.database.Cursor;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JourPlace
{

	private String id;

	private String name;

	private String description;

	private String reference;

	private String icon;

	private String vicinity;

	private JourGeometry geometry;

	private String formatted_address;

	private String formatted_phone_number;

	public JourPlace()
	{
		// TODO Auto-generated constructor stub
	}

	public static JourPlace newInstance(Cursor c)
	{
		JourPlace jPlace = new JourPlace();
		// For Search: 0 = _id, 1 = description, 2 = lat, 3 = lng
		jPlace.setId(c.getString(c.getColumnIndex("_id")));
		jPlace.setDescription(c.getString(c.getColumnIndex("description")));
		Log.d("PLACE", "lat: " + c.getColumnIndex("lat"));
		JourGeometry jGeometry = jPlace.getGeometry();
		JourLocation location = jGeometry.getLocation();
		location.setLat(Double.parseDouble(c.getString(c.getColumnIndex("lat"))));
		location.setLng(Double.parseDouble(c.getString(c.getColumnIndex("lng"))));

		return jPlace;
	}

	@Override
	public String toString()
	{
		return name + " - " + id + " - " + reference;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the reference
	 */
	public String getReference()
	{
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference)
	{
		this.reference = reference;
	}

	/**
	 * @return the icon
	 */
	public String getIcon()
	{
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	/**
	 * @return the vicinity
	 */
	public String getVicinity()
	{
		return vicinity;
	}

	/**
	 * @param vicinity
	 *            the vicinity to set
	 */
	public void setVicinity(String vicinity)
	{
		this.vicinity = vicinity;
	}

	/**
	 * @return the geometry
	 */
	public JourGeometry getGeometry()
	{
		return geometry;
	}

	/**
	 * @param geometry
	 *            the geometry to set
	 */
	public void setGeometry(JourGeometry geometry)
	{
		this.geometry = geometry;
	}

	/**
	 * @return the formatted_address
	 */
	public String getFormatted_address()
	{
		return formatted_address;
	}

	/**
	 * @param formatted_address
	 *            the formatted_address to set
	 */
	public void setFormatted_address(String formatted_address)
	{
		this.formatted_address = formatted_address;
	}

	/**
	 * @return the formatted_phone_number
	 */
	public String getFormatted_phone_number()
	{
		return formatted_phone_number;
	}

	/**
	 * @param formatted_phone_number
	 *            the formatted_phone_number to set
	 */
	public void setFormatted_phone_number(String formatted_phone_number)
	{
		this.formatted_phone_number = formatted_phone_number;
	}
}

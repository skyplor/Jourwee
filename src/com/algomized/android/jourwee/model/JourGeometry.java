package com.algomized.android.jourwee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JourGeometry
{
	private JourLocation jLocation;

	public JourGeometry()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the location
	 */
	public JourLocation getLocation()
	{
		return jLocation;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(JourLocation jLocation)
	{
		this.jLocation = jLocation;
	}
}

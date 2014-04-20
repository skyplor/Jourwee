package com.algomized.android.jourwee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JourPlaceDetails
{
	private String status;
	private JourPlace result;

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the result
	 */
	public JourPlace getResult()
	{
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(JourPlace result)
	{
		this.result = result;
	}

}


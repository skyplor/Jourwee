package com.algomized.android.jourwee.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JourPlaceList
{
	private String status;
	private List<JourPlace> results, predictions;

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
	 * @return the results
	 */
	public List<JourPlace> getResults()
	{
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<JourPlace> results)
	{
		this.results = results;
	}

	/**
	 * @return the predictions
	 */
	public List<JourPlace> getPredictions()
	{
		return predictions;
	}

	/**
	 * @param predictions the predictions to set
	 */
	public void setPredictions(List<JourPlace> predictions)
	{
		this.predictions = predictions;
	}

}

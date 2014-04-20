package com.algomized.android.jourwee.model;

public class JourRoute
{
	private JourPlace origin, destination;

	/**
	 * @return the origin
	 */
	public JourPlace getOrigin()
	{
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(JourPlace origin)
	{
		this.origin = origin;
	}

	/**
	 * @return the destination
	 */
	public JourPlace getDestination()
	{
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(JourPlace destination)
	{
		this.destination = destination;
	}

	private static final JourRoute jRoute = new JourRoute();

	public static JourRoute getInstance()
	{
		return jRoute;
	}
}

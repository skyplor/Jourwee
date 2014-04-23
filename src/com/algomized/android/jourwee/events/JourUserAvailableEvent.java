package com.algomized.android.jourwee.events;

import com.algomized.android.jourwee.model.JourUser;

/**
 * Simple event class that will be used in our event bus to carry a jUser object
 *
 */
public class JourUserAvailableEvent
{
	// the contact object being sent using the bus
	private JourUser jUser;

	public JourUserAvailableEvent(JourUser jUser)
	{
		this.jUser = jUser;
	}

	/**
	 * @return the jUser
	 */
	public JourUser getJUser()
	{
		return jUser;
	}
}

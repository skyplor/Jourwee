package com.algomized.android.jourwee.auth;

import org.androidannotations.annotations.EService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

@EService
public class JourweeAuthenticatorService extends Service
{
	private static JourweeAuthenticator jAccountAuthenticator = null;

	public JourweeAuthenticatorService()
	{
		super();
	}

	@Override
	public IBinder onBind(Intent intent)
	{

		JourweeAuthenticator authenticator = this.getAuthenticator();
		return authenticator.getIBinder();
	}

	private JourweeAuthenticator getAuthenticator()
	{
		if (jAccountAuthenticator == null)
		{
			jAccountAuthenticator = new JourweeAuthenticator(this);
		}
		return jAccountAuthenticator;
	}

}

package com.algomized.android.jourwee.auth;

import com.algomized.android.jourwee.view.LoginActivity_;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class JourweeAuthenticatorService extends Service
{
	private static final String TAG = "AccountAuthenticatorService";
	private static JourweeAuthenticator jAccountAuthenticator = null;

	public JourweeAuthenticatorService()
	{
		super();
	}

	@Override
	public IBinder onBind(Intent intent)
	{

		JourweeAuthenticator authenticator = new JourweeAuthenticator(this);
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

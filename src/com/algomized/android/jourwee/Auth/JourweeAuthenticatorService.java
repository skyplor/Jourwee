package com.algomized.android.jourwee.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class JourweeAuthenticatorService extends Service
{
	@Override
	public IBinder onBind(Intent intent)
	{

		JourweeAuthenticator authenticator = new JourweeAuthenticator(this);
		return authenticator.getIBinder();
	}
}

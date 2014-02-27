package com.algomized.android.jourwee.util;

//import com.algomized.android.jourwee.JourweeApplication;
import com.algomized.android.jourwee.JourweeApplication;
import com.algomized.android.jourwee.auth.AuthListener;
import com.algomized.android.jourwee.auth.JourweeAuth;
import com.algomized.android.jourwee.auth.UserPassCreds;
import com.android.volley.RequestQueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class Util
{
	private static boolean hasRestoredCreds;
	final static RequestQueue queue = JourweeApplication.getInstance().getRequestQueue();

	public static boolean checkLoginStatus(Context context)
	{
		SharedPreferences localSharedPreferences = context.getSharedPreferences("accountPrefs", 0);
		JourweeAuth auth = new JourweeAuth(context.getApplicationContext(), new TokenStoreImpl(localSharedPreferences));

		if (!hasRestoredCreds)
		{
			auth.onRestoreCreds();
			hasRestoredCreds = true;
		}
		// return JourweeApplication.getInstance().getRequestQueue()..isAuthValid();
		return auth.isValid();
	}

	public static void authenticate(UserPassCreds paramCreds, AuthListener paramAuthListener, Context context)
	{
		SharedPreferences localSharedPreferences = context.getSharedPreferences("accountPrefs", 0);
		JourweeAuth auth = new JourweeAuth(context.getApplicationContext(), new TokenStoreImpl(localSharedPreferences));
		InlineRequest localInlineRequest = auth.onBuildAuthRequest(paramCreds, paramAuthListener);
		if (localInlineRequest != null)
			queue.add(localInlineRequest);
	}
}

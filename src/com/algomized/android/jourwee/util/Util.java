package com.algomized.android.jourwee.util;

//import com.algomized.android.jourwee.JourweeApplication;
import com.algomized.android.jourwee.JourweeApplication;
import com.algomized.android.jourwee.Auth.AuthListener;
import com.algomized.android.jourwee.Auth.JourweeAuth;
import com.algomized.android.jourwee.Auth.UserPassCreds;
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

	// public static void implementActionLayout(final Fragment frag, Menu menu, final int menuResId)
	// {
	// final MenuItem item = menu.findItem(menuResId);
	// makeClickable(item, new Runnable()
	// {
	//
	// private final Fragment val$frag;
	// private final MenuItem val$item;
	// private final int val$menuResId;
	//
	// public void run()
	// {
	// Object aobj[] = new Object[1];
	// aobj[0] = Integer.valueOf(menuResId);
	// Log.d("minus:util", "actionLayoutClicked! (%d)", aobj);
	// frag.onOptionsItemSelected(item);
	// }
	//
	// {
	// menuResId = i;
	// frag = fragment;
	// item = menuitem;
	// super();
	// }
	// });
	// }

	// private static void makeClickable(MenuItem menuitem, final Runnable onClick)
	// {
	// final View final_view = MenuItemCompat.getActionView(menuitem);
	// if (android.os.Build.VERSION.SDK_INT <= 18)
	// {
	// new OnTouchClickListener(final_view)
	// {
	//
	// super(final_view);
	//
	// private final Runnable val$onClick;
	// onClick = runnable;
	//
	// @Override
	// public void onClick(View view, MotionEvent motionevent)
	// {
	// onClick.run();
	// }
	//
	// };
	// }
	// else
	// {
	// final_view.setOnClickListener(new android.view.View.OnClickListener()
	// {
	//
	// private final Runnable val$onClick;
	//
	// public void onClick(View view)
	// {
	// onClick.run();
	// }
	//
	// {
	// onClick = runnable;
	// super();
	// }
	// });
	// return;
	// }
	// }
}

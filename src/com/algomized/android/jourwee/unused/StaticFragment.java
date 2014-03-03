package com.algomized.android.jourwee.unused;

import com.algomized.android.jourwee.unused.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class StaticFragment extends Fragment
{

	public static int FLAG_SHOW_AB = 1;
	public static int FLAG_SHOW_KEYBOARD = 2;
	private View mFocused;
	private String mOldTitle;
	private boolean mWasShowingAb;

	public StaticFragment()
	{
	}

	static Bundle args(int i, int j, String s, int k, int l)
	{
		Bundle bundle = new Bundle();
		bundle.putInt("layout", i);
		bundle.putInt("flags", j);
		bundle.putString("title", s);
		bundle.putInt("menuResId", k);
		bundle.putInt("menuActionIfy", l);
		return bundle;
	}

	public static StaticFragment newInstance(int i)
	{
		return newInstance(i, 0);
	}

	public static StaticFragment newInstance(int i, int j)
	{
		return newInstance(i, j, null, 0, 0);
	}

	public static StaticFragment newInstance(int i, int j, String s, int k, int l)
	{
		Bundle bundle = args(i, j, s, k, l);
		StaticFragment staticfragment = new StaticFragment();
		staticfragment.setArguments(bundle);
		return staticfragment;
	}

	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		boolean flag;
		if (getArguments().getInt("menuResId") != 0)
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		setHasOptionsMenu(flag);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater)
	{
		if (getArguments().getInt("menuResId") != 0)
		{
			menuinflater.inflate(getArguments().getInt("menuResId"), menu);
			int i = getArguments().getInt("menuActionIfy");
			if (i != 0)
			{
				// Util.implementActionLayout(getActivity(), menu, i);
				Log.d("jourwee", "staticfragment > oncreateoptionsmenu");
			}
			return;
		}
		else
		{
			super.onCreateOptionsMenu(menu, menuinflater);
			return;
		}
	}

	public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
	{
		if (bundle != null)
		{
			mWasShowingAb = bundle.getBoolean("wasShowingAb");
			mOldTitle = bundle.getString("oldtitle");
		}
		return layoutinflater.inflate(getArguments().getInt("layout"), null, false);
	}

	public void onPause()
	{
		super.onPause();
		int i = getArguments().getInt("flags");
		ActionBarActivity actionbaractivity = (ActionBarActivity) getActivity();
		ActionBar actionbar = actionbaractivity.getSupportActionBar();
		if ((i & FLAG_SHOW_AB) != 0 && !mWasShowingAb && actionbar != null)
		{
			actionbar.hide();
		}
		if (getArguments().getString("title") != null && actionbar != null)
		{
			actionbar.setTitle(mOldTitle);
		}
		if (getArguments().getInt("menuResId") != 0)
		{
			((ActionBarActivity) getActivity()).supportInvalidateOptionsMenu();
		}
		if ((i & FLAG_SHOW_KEYBOARD) != 0 && mFocused != null)
		{
			((InputMethodManager) actionbaractivity.getSystemService("input_method")).hideSoftInputFromWindow(mFocused.getWindowToken(), 0);
		}
	}

	public void onResume()
	{
		super.onResume();
		int i = getArguments().getInt("flags");
		ActionBarActivity actionbaractivity = (ActionBarActivity) getActivity();
		ActionBar actionbar = actionbaractivity.getSupportActionBar();
		if (actionbar != null)
		{
			mWasShowingAb = actionbar.isShowing();
			if ((i & FLAG_SHOW_AB) != 0)
			{
				actionbar.show();
				actionbar.setDisplayHomeAsUpEnabled(true);
			}
			if (actionbar.getTitle() != null)
			{
				mOldTitle = actionbar.getTitle().toString();
			}
			String s = getArguments().getString("title");
			if (s != null)
			{
				actionbar.setTitle(s);
			}
		}
		if ((i & FLAG_SHOW_KEYBOARD) != 0)
		{
			InputMethodManager inputmethodmanager = (InputMethodManager) actionbaractivity.getSystemService("input_method");
			View view = getView();
			if (view != null)
			{
				View view1 = view.findFocus();
				if (view1 != null)
				{
					mFocused = view1;
					inputmethodmanager.showSoftInput(view1, 2);
				}
			}
		}
		if (getArguments().getInt("menuResId") != 0)
		{
			getActivity().supportInvalidateOptionsMenu();
		}
	}

	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		bundle.putBoolean("wasShowingAb", mWasShowingAb);
		bundle.putString("oldTitle", mOldTitle);
	}

}

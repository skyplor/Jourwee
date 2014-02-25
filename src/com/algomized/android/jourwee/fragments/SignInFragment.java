package com.algomized.android.jourwee.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.view.StartActivity;

public class SignInFragment //extends StaticFragment implements android.widget.TextView.OnEditorActionListener, android.view.View.OnKeyListener
{

	private TextView mPassView;

	public SignInFragment()
	{
	}

//	private void dispatchSignIn()
//	{
//		((StartActivity) getActivity()).SignIn(null);
//	}
//
//	public static Fragment newInstance(Context context)
//	{
//		Bundle bundle = StaticFragment.args(R.layout.login, StaticFragment.FLAG_SHOW_AB | StaticFragment.FLAG_SHOW_KEYBOARD, context.getString(R.string.title_signin), 0x7f10001e, 0x7f09022a);
//		SignInFragment signinfragment = new SignInFragment();
//		signinfragment.setArguments(bundle);
//		return signinfragment;
//	}
//
//	public void onAttach(Activity activity)
//	{
//		super.onAttach(activity);
//		if (!(activity instanceof StartActivity))
//		{
//			throw new IllegalArgumentException("SignInFragment must be attached to Intro!");
//		}
//		else
//		{
//			return;
//		}
//	}
//
//	public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
//	{
//		View view = super.onCreateView(layoutinflater, viewgroup, bundle);
//		mPassView = (TextView) view.findViewById(R.id.pwTxtBox);
//		mPassView.setOnEditorActionListener(this);
//		mPassView.setOnKeyListener(this);
//		return view;
//	}
//
//	public boolean onEditorAction(TextView textview, int i, KeyEvent keyevent)
//	{
//		if (i == 6)
//		{
//			dispatchSignIn();
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	public boolean onKey(View view, int i, KeyEvent keyevent)
//	{
//		if (i == 66)
//		{
//			dispatchSignIn();
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	public void onStart()
//	{
//		super.onStart();
//		// AnalyticsUtils.sendView("Login");
//	}
//
//	public void onStop()
//	{
//		super.onStop();
//		// Util.hideKeyboard(getActivity());
//	}
}

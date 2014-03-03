package com.algomized.android.jourwee.unused.util;

import android.view.MotionEvent;
import android.view.View;

public abstract class OnTouchClickListener implements View.OnTouchListener
{
	public OnTouchClickListener(View paramView)
	{
		paramView.setClickable(true);
		paramView.setOnTouchListener(this);
	}

	private static boolean pointInView(View paramView, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		return (paramFloat1 >= -paramFloat3) && (paramFloat2 >= -paramFloat3) && (paramFloat1 < paramFloat3 + (paramView.getRight() - paramView.getLeft())) && (paramFloat2 < paramFloat3 + (paramView.getBottom() - paramView.getTop()));
	}

	public static boolean pointInView(View paramView, MotionEvent paramMotionEvent)
	{
		return pointInView(paramView, paramMotionEvent.getX(), paramMotionEvent.getY(), 0.0F);
	}

	public abstract void onClick(View paramView, MotionEvent paramMotionEvent);

	public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
	{
		boolean bool = true;
		switch (255 & paramMotionEvent.getAction())
		{
			default:
				bool = false;
			case 0:
				paramView.setPressed(bool);
		        return bool;
			case 1:
				paramView.setPressed(false);
		        if (pointInView(paramView, paramMotionEvent))
		        {
		            onClick(paramView, paramMotionEvent);
		            return bool;
		        }
		        	
			case 2:
				if (!pointInView(paramView, paramMotionEvent))
		        {
					paramView.setPressed(false);
		        }
		        return false;
			case 3:
				if (!pointInView(paramView, paramMotionEvent))
		        {
					paramView.setPressed(false);
		        }
		        return false;
		}
	}
}

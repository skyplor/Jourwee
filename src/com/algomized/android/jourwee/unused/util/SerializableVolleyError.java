package com.algomized.android.jourwee.unused.util;

import com.android.volley.VolleyError;

public class SerializableVolleyError extends Throwable
{

	private static final long serialVersionUID = 0x7839291d70557f9fL;
	public byte data[];

	private SerializableVolleyError(VolleyError volleyerror, Throwable throwable)
	{
		super(volleyerror.getMessage(), throwable);
		if (volleyerror.networkResponse != null)
		{
			data = volleyerror.networkResponse.data;
		}
	}

	public static Throwable wrap(Throwable throwable)
	{
		Object obj;
		if (!(throwable instanceof VolleyError))
		{
			obj = throwable;
		}
		else
		{
			obj = (VolleyError) throwable;
			Throwable throwable1 = ((VolleyError) (obj)).getCause();
			if (((VolleyError) (obj)).networkResponse != null || throwable1 != null)
			{
				Throwable throwable2 = wrap(throwable1);
				if (((VolleyError) (obj)).networkResponse != null || throwable2 != throwable1)
				{
					SerializableVolleyError serializablevolleyerror = new SerializableVolleyError(((VolleyError) (obj)), throwable2);
					serializablevolleyerror.setStackTrace(throwable.getStackTrace());
					return serializablevolleyerror;
				}
			}
		}
		return ((Throwable) (obj));
	}
}

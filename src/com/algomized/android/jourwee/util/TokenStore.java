package com.algomized.android.jourwee.util;

public abstract interface TokenStore
{
	public abstract void commit();

	public abstract TokenStore edit();

	public abstract long getLong(String paramString, long paramLong);

	public abstract String getString(String paramString1, String paramString2);

	public abstract TokenStore putLong(String paramString, long paramLong);

	public abstract TokenStore putString(String paramString1, String paramString2);

	public abstract TokenStore remove(String paramString);
}

package com.algomized.android.jourwee.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TokenStoreImpl
implements TokenStore
{
private final SharedPreferences mPrefs;

public TokenStoreImpl(Context paramContext)
{
  this(PreferenceManager.getDefaultSharedPreferences(paramContext));
}

public TokenStoreImpl(SharedPreferences paramSharedPreferences)
{
  this.mPrefs = paramSharedPreferences;
}

public void commit()
{
  throw new UnsupportedOperationException("Cannot commit a non-editable PreferencesTokenStore. Call edit() and use the returned TokenStore");
}

public TokenStore edit()
{
  return new EditableTokenStore(this.mPrefs);
}

public long getLong(String paramString, long paramLong)
{
  return this.mPrefs.getLong(paramString, paramLong);
}

public String getString(String paramString1, String paramString2)
{
  return this.mPrefs.getString(paramString1, paramString2);
}

public TokenStore putLong(String paramString, long paramLong)
{
  throw new UnsupportedOperationException("Cannot edit a non-editable PreferencesTokenStore. Call edit() and use the returned TokenStore");
}

public TokenStore putString(String paramString1, String paramString2)
{
  throw new UnsupportedOperationException("Cannot edit a non-editable PreferencesTokenStore. Call edit() and use the returned TokenStore");
}

public TokenStore remove(String paramString)
{
  throw new UnsupportedOperationException("Cannot edit a non-editable PreferencesTokenStore. Call edit() and use the returned TokenStore");
}

@SuppressLint({"CommitPrefEdits"})
private static class EditableTokenStore extends TokenStoreImpl
{
  private final SharedPreferences.Editor mEditor;

  EditableTokenStore(SharedPreferences paramSharedPreferences)
  {
    super(paramSharedPreferences);
    this.mEditor = paramSharedPreferences.edit();
  }

  public void commit()
  {
    this.mEditor.commit();
  }

  public TokenStore edit()
  {
    return this;
  }

  public TokenStore putLong(String paramString, long paramLong)
  {
    this.mEditor.putLong(paramString, paramLong);
    return this;
  }

  public TokenStore putString(String paramString1, String paramString2)
  {
    this.mEditor.putString(paramString1, paramString2);
    return this;
  }

  public TokenStore remove(String paramString)
  {
    this.mEditor.remove(paramString);
    return this;
  }
}
}

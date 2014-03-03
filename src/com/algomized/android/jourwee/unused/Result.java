package com.algomized.android.jourwee.unused;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.algomized.android.jourwee.unused.util.SerializableVolleyError;
import com.android.volley.VolleyError;
import com.android.volley.NetworkError;

public class Result implements Serializable
{
	public static enum Type {
		SUCCESS("SUCCESS", 0), FAIL_AUTH("FAIL_AUTH", 1), ERROR("ERROR", 2), ERROR_NETWORK("ERROR_NETWORK", 3), ERROR_CLIENT("ERROR_CLIENT", 4), ERROR_SERVER("ERROR_SERVER", 5);

		private Type(String s, int i)
		{
			// super(s, i);
		}
	}

	public static final Result SUCCESS;
	private static final long serialVersionUID = 0x9cd613264d80ab56L;
	private final CacheResult.Type mCacheResult;
	private final Throwable mError;
	private final Type mType;

	private Result(Throwable throwable)
	{
		if (throwable instanceof NetworkError)
		{
			mType = Type.ERROR_NETWORK;
		}
		else if (throwable instanceof VolleyError)
		{
			VolleyError volleyerror = (VolleyError) throwable;
			if (volleyerror.networkResponse != null)
			{
				if (volleyerror.networkResponse.statusCode >= 500)
				{
					mType = Type.ERROR_SERVER;
				}
				else if (volleyerror.networkResponse.statusCode == 401)
				{
					mType = Type.FAIL_AUTH;
				}
				else
				{
					mType = Type.ERROR_CLIENT;
				}
			}
			else
			{
				mType = Type.ERROR;
			}
		}
		else
		{
			mType = Type.ERROR;
		}
		mError = SerializableVolleyError.wrap(throwable);
		mCacheResult = null;
	}

	private Result(Type type)
	{
		mType = type;
		mError = null;
		mCacheResult = null;
	}

	private Result(Type type, CacheResult.Type type1)
	{
		mType = type;
		mError = null;
		mCacheResult = type1;
	}

	public static Result cached(CacheResult.Type type)
	{
		return new Result(Type.SUCCESS, type);
	}

	public static Result error(VolleyError volleyerror)
	{
		return new Result(volleyerror);
	}

	public static Result fail(Type type)
	{
		return new Result(type);
	}

	public CacheResult.Type getCacheResultType()
	{
		return mCacheResult;
	}

	public Throwable getError()
	{
		return mError;
	}

	public JSONObject getJsonErrorResponse() throws JSONException
	{
		byte abyte0[] = getRawErrorResponse();
		if (abyte0 == null)
		{
			Log.w("ape:result", "Could not parse network response to json");
			return null;
		}
		else
		{
			String s1 = new String(abyte0);
			JSONObject jsonobject = new JSONObject(s1);
			return jsonobject;
		}
	}

	public byte[] getRawErrorResponse()
	{
		if (mError instanceof SerializableVolleyError)
		{
			SerializableVolleyError serializablevolleyerror = (SerializableVolleyError) mError;
			if (serializablevolleyerror.data != null)
			{
				return serializablevolleyerror.data;
			}
		}
		return null;
	}

	public Type getType()
	{
		return mType;
	}

	public boolean isCached()
	{
		return mCacheResult != null && mCacheResult != CacheResult.Type.MISS;
	}

	public boolean isFresh()
	{
		return isCached() && mCacheResult == CacheResult.Type.HIT;
	}

	public boolean isStale()
	{
		return isCached() && mCacheResult == CacheResult.Type.STALE;
	}

	public boolean success()
	{
		return mType == Type.SUCCESS;
	}

	public String toString()
	{
		Object aobj[] = new Object[3];
		aobj[0] = mType;
		aobj[1] = mCacheResult;
		aobj[2] = mError;
		return String.format("[Result=%s; Cache?=%s; mError=%s]", aobj);
	}

	static
	{
		SUCCESS = new Result(Type.SUCCESS);
	}
}

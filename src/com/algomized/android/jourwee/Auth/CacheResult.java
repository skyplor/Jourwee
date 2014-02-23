package com.algomized.android.jourwee.Auth;

public class CacheResult
{
    public static enum Type
    {
    	HIT("HIT", 0), STALE("STALE", 1), MISS("MISS",2);

        private Type(String s, int i)
        {
//            super(s, i);
        }
    }


    private final Cacheable mObject;
    private final Type mResult;

    private CacheResult(Type type, Cacheable cacheable)
    {
        mResult = type;
        mObject = cacheable;
    }

    static CacheResult from(Type type, Cacheable cacheable)
    {
        return new CacheResult(type, cacheable);
    }

    public Cacheable get()
    {
        return mObject;
    }

    public Type getType()
    {
        return mResult;
    }
}

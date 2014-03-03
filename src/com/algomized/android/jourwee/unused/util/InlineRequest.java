package com.algomized.android.jourwee.unused.util;

import com.algomized.android.jourwee.unused.JourweeAuth;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

public abstract class InlineRequest extends Request
{

    public InlineRequest(int method, String url, com.android.volley.Response.ErrorListener errorlistener)
    {
        super(method, url, errorlistener);
    }

    protected abstract void deliverResponse(JourweeAuth.JourweeAuthResponse jourweeAuthResponse);

    protected abstract Response parseNetworkResponse(NetworkResponse networkresponse);

}
package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonRequest<T> extends JsonRequest<T>
{
    private Class<T>    responseType;
    private ObjectMapper mapper;

    /**
     * Creates a new request.
     * 
     * @param method
     *            the HTTP method to use
     * @param url
     *            URL to fetch the JSON from
     * @param requestData
     *            A {@link String} to post and convert into json as the request. Null is allowed and indicates no parameters will be posted along with request.
     * @param listener
     *            Listener to receive the JSON response
     * @param errorListener
     *            Error listener, or null to ignore errors.
     */
    public JacksonJsonRequest(int method, String url, String requestData, Class<T> responseType, Listener<T> listener, ErrorListener errorListener)
    {
        super(method, url, requestData, listener, errorListener);
        this.responseType = responseType;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            mapper = new ObjectMapper();
            T Tresponse = mapper.readValue(jsonString, responseType);
            return Response.success(Tresponse, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
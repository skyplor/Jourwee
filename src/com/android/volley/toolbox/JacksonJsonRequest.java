package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonJsonRequest<T> extends Request<T>
{

	private ObjectMapper mMapper = new ObjectMapper();

	private static final String PROTOCOL_CHARSET = "utf-8";

	private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

	private final Response.Listener<T> mListener;
	private final T mRequestBody;
	private final Class<T> mClass;

	public JacksonJsonRequest(int method, String url, T requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> claz)
	{
		super(method, url, errorListener);

		mListener = listener;
		mRequestBody = requestBody;
		mClass = claz;
	}

	public JacksonJsonRequest(int method, String url, T requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> claz, ObjectMapper objectMapper)
	{
		this(method, url, requestBody, listener, errorListener, claz);

		mMapper = objectMapper;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response)
	{
		String jsonString = new String(response.data);
		try
		{
			VolleyLog.d("JSONSTRING: %s", jsonString);
			T result = mMapper.readValue(jsonString, mClass);
			VolleyLog.d("User result: %s", result.toString());
			return Response.success(result, getCacheEntry());
		}
		catch (IOException e)
		{
			VolleyLog.d("IOException mapping the json string %s", jsonString);
		}
		return null;
	}

	@Override
	protected void deliverResponse(T response, boolean b)
	{
		mListener.onResponse(response);
		VolleyLog.d("In DeliverResponse: response > %s", response.toString());
	}

	@Override
	public String getBodyContentType()
	{
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public byte[] getBody()
	{
		try
		{
			return mRequestBody == null ? null : mMapper.writeValueAsBytes(mRequestBody);
		}
		catch (JsonProcessingException e)
		{
			VolleyLog.wtf("JsonProcessing exception while trying to get the bytes of %s using %s", mRequestBody, PROTOCOL_CHARSET);
		}
		return null;
	}
}

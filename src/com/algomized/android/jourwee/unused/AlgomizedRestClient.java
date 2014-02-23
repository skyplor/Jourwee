package com.algomized.android.jourwee.unused;

import com.algomized.android.jourwee.Constants;
import com.loopj.android.http.*;

public class AlgomizedRestClient {

  private static AsyncHttpClient client = new AsyncHttpClient();

  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return Constants.BASE_URL + relativeUrl;
  }
}
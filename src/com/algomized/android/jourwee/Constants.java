package com.algomized.android.jourwee;

public class Constants
{
	public static Boolean LOGIN_STATUS;
	// public static final String BASE_URL = "http://algomized.elasticbeanstalk.com/api/";
	// public static final String BASE_URL = "http://algomizedwebserver.elasticbeanstalk.com/api/";
	public static final String BASE_URL = "http://algomizedwebserver.elasticbeanstalk.com/";
	public static final String URL_TOKEN = "oauth/token";
	public static final String URL_GET_USER = "resource/username";
	public static final String URL_LOGIN = "login";
	public static final String URL_REGISTER = "oauth/register";
	public static final String URL_TEST_API = "test";
	public static final String URL_LOGOUT = "logout";
	public static final String SHARED_PREF_NAME = "com.algomized.android.jourwee";
	public static final String PREF_SPRING_SECURITY_COOKIE = "spring_security_cookie";
	public static final String AM_ACCOUNT_TYPE = "com.algomized.android.jourwee";
	public static final String AM_AUTH_TYPE = "com.algomized.android.jourwee";
	public static final String GRANT_PASSWORD = "password";
	public static final String GRANT_REFRESH = "refresh_token";
	public static final String CLIENT_ID = "jourwee-android";
	public static final String CLIENT_SECRET = "GetStarted87";
	public static final String TOKEN_TYPE = "bearer";
	public static final String HEADER_BEARER = "Bearer";
	public static final String HEADER_BASIC = "Basic";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_GRANT_TYPE = "grant_type";
	public static final String KEY_CLIENT_ID = "client_id";
	public static final String KEY_CLIENT_SECRET = "client_secret";
	public static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_TOKEN_TYPE = "token_type";
	public static final String KEY_REFRESH_TOKEN = "refresh_token";
	public static final String KEY_EXPIRES_IN = "expires_in";
	public static final String KEY_HEADER_AUTH = "Authorization";
	public static final String AM_KEY_REFRESH_TOKEN = BASE_URL + "refresh";
	public static final String AM_KEY_EXPIRES_IN = BASE_URL + "expires";

	public static enum REGTYPE {
		DRIVER(0), RIDER(1);
		private int type;

		private REGTYPE(int type)
		{
			this.type = type;
		};

		public int getRegType()
		{
			return type;
		}
	}	
}
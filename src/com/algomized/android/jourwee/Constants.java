
package com.algomized.android.jourwee;

public class Constants
{
	public static Boolean LOGIN_STATUS;
	public static final String BASE_URL = "http://algomizedwebserver.elasticbeanstalk.com/";
	public static final String URL_REFRESH = "api/signin/refresh";
	public static final String URL_GET_USER = "api/account";
	public static final String URL_LOGIN = "api/signin/token";
	public static final String URL_FBLOGIN = "api/signin/facebook";
	public static final String URL_TWLOGIN = "api/signin/twitter";
	public static final String URL_REGISTER = "api/signup";

	public static final String SHARED_PREF_NAME = "com.algomized.android.jourwee";
	public static final String PREF_SPRING_SECURITY_COOKIE = "spring_security_cookie";
	public static final String AM_ACCOUNT_TYPE = "com.algomized.android.jourwee";
	public static final String AM_AUTH_TYPE = "com.algomized.android.jourwee";
//	public static final String GRANT_PASSWORD = "password";
//	public static final String GRANT_REFRESH = "refresh_token";
	public static final String CLIENT_ID = "jourwee-android";
	public static final String CLIENT_SECRET = "GetStarted87";
	public static final String TOKEN_TYPE = "bearer";
	public static final String HEADER_BEARER = "Bearer";
	public static final String HEADER_BASIC = "Basic";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
//	public static final String KEY_GRANT_TYPE = "grant_type";
	public static final String KEY_CLIENT_ID = "client_id";
	public static final String KEY_CLIENT_SECRET = "client_secret";
	public static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_TOKEN_TYPE = "token_type";
	public static final String KEY_REFRESH_TOKEN = "refresh_token";
	public static final String KEY_EXPIRES_IN = "expires_in";
	public static final String KEY_HEADER_AUTH = "Authorization";
	public static final String KEY_USERTYPE = "user_type";
	
	public static final String AM_KEY_REFRESH_TOKEN = BASE_URL + "refresh";
	public static final String AM_KEY_EXPIRES_IN = BASE_URL + "expires";
	public static final String BROWSER_KEY = "AIzaSyCoQowbIAVI-hzlKp6kRn4rzJUmVWn_lgs";
	public static final int ORIGIN = 0;
	public static final int DESTINATION = 1;

	public static enum USERTYPE {
		DRIVER("driver"), RIDER("rider");
		private String type = "";

		private USERTYPE(String type)
		{
			this.type = type;
		};

		public String getRegType()
		{
			return type;
		}
	}	
}
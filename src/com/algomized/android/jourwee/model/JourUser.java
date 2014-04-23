package com.algomized.android.jourwee.model;

import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.algomized.android.jourwee.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JourUser //implements Parcelable
{

	private String name = null;

	@JsonProperty("username")
	private String username = null;

	private String password = "";

	private boolean enabled;
	
	private int user_type = 0;

	@JsonIgnore
	private boolean status;

	private String message = null;
	
	private String access_token = "";
	
	private String token_type = "";
	
	private String refresh_token = "";
	
	private String expires_in = "";
	
	private String error = "";
	
	private String error_description = "";

	@JsonProperty("userId")
	private Integer id = null;

	private Integer age = null;

	private List<Map<String, String>> userRoles = null;
	
	private JourRoute route;

	/**
	 * @return the age
	 */
	public Integer getAge()
	{
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(Integer age)
	{
		this.age = age;
	}

	/**
	 * @return the id
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	@JsonIgnore
	public boolean isStatus()
	{
		return status;
	}

	@JsonProperty
	public void setStatus(boolean status)
	{
		this.status = status;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public List<Map<String, String>> getUserRoles()
	{
		return userRoles;
	}

	public void setUserRoles(List<Map<String, String>> userRoles)
	{
		this.userRoles = userRoles;
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token()
	{
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}

	/**
	 * @return the token_type
	 */
	public String getToken_type()
	{
		return token_type;
	}

	/**
	 * @param token_type the token_type to set
	 */
	public void setToken_type(String token_type)
	{
		this.token_type = token_type;
	}

	/**
	 * @return the refresh_token
	 */
	public String getRefresh_token()
	{
		return refresh_token;
	}

	/**
	 * @param refresh_token the refresh_token to set
	 */
	public void setRefresh_token(String refresh_token)
	{
		this.refresh_token = refresh_token;
	}

	/**
	 * @return the expires_in
	 */
	public String getExpires_in()
	{
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(String expires_in)
	{
		this.expires_in = expires_in;
	}

	/**
	 * @return the error
	 */
	public String getError()
	{
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error)
	{
		this.error = error;
	}

	/**
	 * @return the error_description
	 */
	public String getError_description()
	{
		return error_description;
	}

	/**
	 * @param error_description the error_description to set
	 */
	public void setError_description(String error_description)
	{
		this.error_description = error_description;
	}

	/**
	 * @return the user_type
	 */
	public int getUser_type()
	{
		return user_type;
	}

	/**
	 * @param user_type the user_type to set
	 */
	public void setUser_type(int user_type)
	{
		this.user_type = user_type;
	}

	/**
	 * @return the route
	 */
	public JourRoute getRoute()
	{
		return route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(JourRoute route)
	{
		this.route = route;
	}

	@Override
	public String toString()
	{
		if (access_token != null && expires_in != null && refresh_token != null && token_type != null)
		{
			StringBuilder builder = new StringBuilder();
			builder.append(Constants.KEY_ACCESS_TOKEN).append(":").append(access_token).append(System.getProperty("line.separator"));
			builder.append(Constants.KEY_EXPIRES_IN).append(":").append(expires_in).append(System.getProperty("line.separator"));
			builder.append(Constants.KEY_REFRESH_TOKEN).append(":").append(refresh_token).append(System.getProperty("line.separator"));
			builder.append(Constants.KEY_TOKEN_TYPE).append(":").append(token_type).append(System.getProperty("line.separator"));
			return builder.toString();
		}
		else
			return "null";
	}

//	@Override
//	public int describeContents()
//	{
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags)
//	{
//		dest.writeInt(id);
//		dest.writeString(username);
//        dest.writeString(access_token);
//        dest.writeString(expires_in);
//        dest.writeString(refresh_token);
//        dest.writeString(token_type);
//		
//	}

}
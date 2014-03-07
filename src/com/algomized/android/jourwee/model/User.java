package com.algomized.android.jourwee.model;

import java.util.List;
import java.util.Map;

import com.algomized.android.jourwee.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User
{

	private String name = null;

	@JsonProperty("username")
	private String username = null;

	private String password = "";

	private boolean enabled;

	@JsonIgnore
	private boolean status;

	private String message = null;
	
	private String access_token = "";
	
	private String token_type = "";
	
	private String refresh_token = "";
	
	private String expires_in = "";

	@JsonProperty("userId")
	private Integer id = null;

	private Integer age = null;

	private List<Map<String, String>> userRoles = null;

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

}
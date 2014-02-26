package com.algomized.android.jourwee.model;

import java.util.List;
import java.util.Map;

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

	public boolean isStatus()
	{
		return status;
	}

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

	@Override
	public String toString()
	{
		if (id != null && username != null && name != null)
		{
			StringBuilder builder = new StringBuilder();
			builder.append("id").append(id);
			builder.append("username").append(username);
			builder.append("name").append(name);
			return builder.toString();
		}
		else
			return "null";
	}

}
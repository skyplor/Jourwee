package com.algomized.android.jourwee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User
{

	private String name = null;
	
	@JsonProperty("userName")
	private String username = null;
	
	private String password = "";

	@JsonProperty("userId")
	private Integer id = null;

	private Integer age = null;

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
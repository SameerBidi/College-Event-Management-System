package com.cemsserver.model;

import java.util.ArrayList;

public class UserData
{
	private String userID;
	
	private UserType userType;
	
	private String name;
	
	private String year;
	private String branch;
	
	private String apiKey;
	
	public UserData()
	{
		super();
	}

	public UserData(String userID, UserType userType, String name, String year, String branch, String apiKey)
	{
		super();
		this.userID = userID;
		this.userType = userType;
		this.name = name;
		this.year = year;
		this.branch = branch;
		this.apiKey = apiKey;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public UserType getUserType()
	{
		return userType;
	}

	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getBranch()
	{
		return branch;
	}

	public void setBranch(String branch)
	{
		this.branch = branch;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	@Override
	public String toString()
	{
		return "UserData [userID=" + userID + ", userType=" + userType + ", name=" + name + ", year=" + year
				+ ", branch=" + branch + ", apiKey=" + apiKey + "]";
	}
}

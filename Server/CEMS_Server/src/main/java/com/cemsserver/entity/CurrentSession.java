package com.cemsserver.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.cemsserver.model.UserType;

@Entity
public class CurrentSession
{
	@Id
	String apiKey;
	
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	String userID;
	
	public CurrentSession() { super(); }

	public CurrentSession(String apiKey, UserType userType, String userID)
	{
		this.apiKey = apiKey;
		this.userType = userType;
		this.userID = userID;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public UserType getUserType()
	{
		return userType;
	}

	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	@Override
	public String toString()
	{
		return "CurrentSession [apiKey=" + apiKey + ", userType=" + userType + ", userID=" + userID + "]";
	}
}

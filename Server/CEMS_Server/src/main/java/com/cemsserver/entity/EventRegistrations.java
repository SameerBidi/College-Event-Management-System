package com.cemsserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EventRegistrations
{
	@Id
	String eventRegID;
	
	String eventID;
	String userID;
	String name;
	String rollno;
	String regDate;
	String regTime;
	
	public EventRegistrations() { super(); }

	public EventRegistrations(String eventRegID, String eventID, String userID, String name, String rollno,
			String regDate, String regTime)
	{
		super();
		this.eventRegID = eventRegID;
		this.eventID = eventID;
		this.userID = userID;
		this.name = name;
		this.rollno = rollno;
		this.regDate = regDate;
		this.regTime = regTime;
	}

	public String getEventRegID()
	{
		return eventRegID;
	}

	public void setEventRegID(String eventRegID)
	{
		this.eventRegID = eventRegID;
	}

	public String getEventID()
	{
		return eventID;
	}

	public void setEventID(String eventID)
	{
		this.eventID = eventID;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRollno()
	{
		return rollno;
	}

	public void setRollno(String rollno)
	{
		this.rollno = rollno;
	}

	public String getRegDate()
	{
		return regDate;
	}

	public void setRegDate(String regDate)
	{
		this.regDate = regDate;
	}

	public String getRegTime()
	{
		return regTime;
	}

	public void setRegTime(String regTime)
	{
		this.regTime = regTime;
	}

	@Override
	public String toString()
	{
		return "EventRegistrations [eventRegID=" + eventRegID + ", eventID=" + eventID + ", userID=" + userID
				+ ", name=" + name + ", rollno=" + rollno + ", regDate=" + regDate + ", regTime=" + regTime + "]";
	}
}

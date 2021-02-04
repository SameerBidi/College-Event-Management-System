package com.cemsserver.entity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.cemsserver.model.EventType;
import com.cemsserver.utility.StringArrayListConverter;

@Entity
public class Event
{
	@Id
	private String eventID;
	
	private String eventRegistrarID;
	private String eventRegistrarName;
	
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	private String eventDate;
	private String eventName;
	private String eventVenue;
	private String eventStartTime;
	private String eventEndTime;
	private String eventCost;
	
	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringArrayListConverter.class)
	private ArrayList<String> years = new ArrayList<>();
	
	@Column(columnDefinition = "TEXT")
	@Convert(converter = StringArrayListConverter.class)
	private ArrayList<String> branches = new ArrayList<>();
	
	public Event() { super(); }

	public Event(String eventID, String eventRegistrarID, String eventRegistrarName, EventType eventType,
			String eventDate, String eventName, String eventVenue, String eventStartTime, String eventEndTime,
			String eventCost, ArrayList<String> years, ArrayList<String> branches)
	{
		super();
		this.eventID = eventID;
		this.eventRegistrarID = eventRegistrarID;
		this.eventRegistrarName = eventRegistrarName;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.eventName = eventName;
		this.eventVenue = eventVenue;
		this.eventStartTime = eventStartTime;
		this.eventEndTime = eventEndTime;
		this.eventCost = eventCost;
		this.years = years;
		this.branches = branches;
	}

	public String getEventID()
	{
		return eventID;
	}

	public void setEventID(String eventID)
	{
		this.eventID = eventID;
	}

	public String getEventRegistrarID()
	{
		return eventRegistrarID;
	}

	public void setEventRegistrarID(String eventRegistrarID)
	{
		this.eventRegistrarID = eventRegistrarID;
	}

	public String getEventRegistrarName()
	{
		return eventRegistrarName;
	}

	public void setEventRegistrarName(String eventRegistrarName)
	{
		this.eventRegistrarName = eventRegistrarName;
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public String getEventDate()
	{
		return eventDate;
	}

	public void setEventDate(String eventDate)
	{
		this.eventDate = eventDate;
	}

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public String getEventVenue()
	{
		return eventVenue;
	}

	public void setEventVenue(String eventVenue)
	{
		this.eventVenue = eventVenue;
	}

	public String getEventStartTime()
	{
		return eventStartTime;
	}

	public void setEventStartTime(String eventStartTime)
	{
		this.eventStartTime = eventStartTime;
	}

	public String getEventEndTime()
	{
		return eventEndTime;
	}

	public void setEventEndTime(String eventEndTime)
	{
		this.eventEndTime = eventEndTime;
	}

	public String getEventCost()
	{
		return eventCost;
	}

	public void setEventCost(String eventCost)
	{
		this.eventCost = eventCost;
	}

	public ArrayList<String> getYears()
	{
		return years;
	}

	public void setYears(ArrayList<String> years)
	{
		this.years = years;
	}

	public ArrayList<String> getBranches()
	{
		return branches;
	}

	public void setBranches(ArrayList<String> branches)
	{
		this.branches = branches;
	}

	@Override
	public String toString()
	{
		return "Event [eventID=" + eventID + ", eventRegistrarID=" + eventRegistrarID + ", eventRegistrarName="
				+ eventRegistrarName + ", eventType=" + eventType + ", eventDate=" + eventDate + ", eventName="
				+ eventName + ", eventVenue=" + eventVenue + ", eventStartTime=" + eventStartTime + ", eventEndTime="
				+ eventEndTime + ", eventCost=" + eventCost + ", years=" + years + ", branches=" + branches + "]";
	}
}

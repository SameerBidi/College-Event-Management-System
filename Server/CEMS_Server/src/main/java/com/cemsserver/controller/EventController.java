package com.cemsserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cemsserver.entity.Event;
import com.cemsserver.entity.EventRegistrations;
import com.cemsserver.entity.User;
import com.cemsserver.model.Response;
import com.cemsserver.model.UserType;
import com.cemsserver.utility.CEMSMainRepo;

@RestController
public class EventController
{
	@Autowired
	CEMSMainRepo mainRepo;
	
	@PostMapping("postEvent")
	public Response postEvent(@RequestHeader("apiKey") String apiKey, @RequestBody Event event)
	{
		System.out.println("Got Event");
		System.out.println(event);
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		if(user.getUserType().equals(UserType.STUDENT))
		{
			return new Response(1, "Not Authorized");
		}
		
		event.setEventID(mainRepo.generateEventID());
		event.setEventRegistrarID(user.getUserID());
		event.setEventRegistrarName(user.getName());
		
		mainRepo.getEventRepo().save(event);
		
		return new Response(0, "success", "");
	}
	
	@PostMapping("getEvents")
	public Response getEvents(@RequestHeader("apiKey") String apiKey)
	{
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		ArrayList<Event> dbEvents = mainRepo.getEventRepo().findAll();
		if(dbEvents == null) dbEvents = new ArrayList<Event>();
		
		if(user.getUserType() == UserType.STUDENT)
		{
			ArrayList<Event> studentEvents = new ArrayList<>();
			
			for(Event event : dbEvents)
			{
				if(event.getYears().contains(user.getYear()) && event.getBranches().contains(user.getBranch()))
				{
					studentEvents.add(event);
				}
			}
			
			return new Response(0, "success", mainRepo.getGson().toJson(studentEvents));
		}
		
		return new Response(0, "success", mainRepo.getGson().toJson(dbEvents));
	}
	
	@PostMapping("getMyEvents")
	public Response getMyEvents(@RequestHeader("apiKey") String apiKey)
	{
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		ArrayList<Event> dbEvents = mainRepo.getEventRepo().findByEventRegistrarID(user.getUserID());
		if(dbEvents == null) dbEvents = new ArrayList<Event>();
		
		return new Response(0, "success", mainRepo.getGson().toJson(dbEvents));
	}
	
	@PostMapping("getRegisteredEvents")
	public Response getRegisteredEvents(@RequestHeader("apiKey") String apiKey)
	{
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		if(!user.getUserType().equals(UserType.STUDENT))
		{
			return new Response(1, "Not Allowed");
		}
		
		ArrayList<EventRegistrations> registeredEventRegistrations = mainRepo.getEventRegistrationsRepo().findByUserID(user.getUserID());
		if(registeredEventRegistrations == null) registeredEventRegistrations = new ArrayList<EventRegistrations>();
		
		ArrayList<Event> registeredEvents = new ArrayList<>();
		
		for(EventRegistrations eventRegistrations : registeredEventRegistrations)
		{
			Event event = mainRepo.getEventFromEventID(eventRegistrations.getEventID());
			
			if(event != null)
			{
				registeredEvents.add(event);
			}
		}
		
		return new Response(0, "success", mainRepo.getGson().toJson(registeredEvents));
	}
	
	@PostMapping("registerToEvent")
	public Response registerToEvent(@RequestHeader("apiKey") String apiKey, @RequestBody String eventID)
	{
		eventID = mainRepo.getGson().fromJson(eventID, String.class);
		
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		if(!user.getUserType().equals(UserType.STUDENT))
		{
			return new Response(1, "Not Allowed");
		}
		
		if(!mainRepo.checkEventExists(eventID))
		{
			return new Response(1, "Invalid Event");
		}
		
		if(mainRepo.checkAlreadyRegisteredToEvent(user.getUserID(), eventID))
		{
			return new Response(1, "Already Registered");
		}
		
		EventRegistrations eventRegistrations = new EventRegistrations(mainRepo.generateEventRegID(), eventID, user.getUserID(), user.getName(), user.getRollno(), mainRepo.getCurrentDate(), mainRepo.getCurrentTime());
		
		mainRepo.getEventRegistrationsRepo().save(eventRegistrations);
		
		return new Response(0, "success", "");
	}
	
	@PostMapping("checkRegistered")
	public Response checkRegistered(@RequestHeader("apiKey") String apiKey, @RequestBody String eventID)
	{
		eventID = mainRepo.getGson().fromJson(eventID, String.class);
		
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		if(!user.getUserType().equals(UserType.STUDENT))
		{
			return new Response(1, "Not Allowed");
		}
		
		boolean registered = mainRepo.checkAlreadyRegisteredToEvent(user.getUserID(), eventID);
		
		return new Response(0, "success", mainRepo.getGson().toJson(registered));
	}
	
	@PostMapping("getEventRegistrations")
	public Response getEventRegistrations(@RequestHeader("apiKey") String apiKey, @RequestBody String eventID)
	{
		eventID = mainRepo.getGson().fromJson(eventID, String.class);
		
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		if(user.getUserType().equals(UserType.STUDENT))
		{
			return new Response(1, "Not Allowed");
		}
		
		ArrayList<EventRegistrations> eventRegistrations = mainRepo.getEventRegistrationsRepo().findByEventID(eventID);
		if(eventRegistrations == null) eventRegistrations = new ArrayList<EventRegistrations>();
		
		return new Response(0, "success", mainRepo.getGson().toJson(eventRegistrations));
	}
	
	@PostMapping("deleteEvent")
	public Response deleteEvent(@RequestHeader("apiKey") String apiKey, @RequestBody String eventID)
	{
		eventID = mainRepo.getGson().fromJson(eventID, String.class);
		
		User user = mainRepo.getUserFromApiKey(apiKey);
		
		if(user == null)
		{
			return new Response(1, "Invalid API Key Or Invalid User");
		}
		
		if(user.getUserType().equals(UserType.STUDENT))
		{
			return new Response(1, "Not Allowed");
		}
		
		Event event = mainRepo.getEventFromEventID(eventID);
		
		if(event == null)
		{
			return new Response(1, "Invalid Event");
		}
		
		if(user.getUserType().equals(UserType.STAFF) && !user.getUserID().equals(event.getEventRegistrarID()))
		{
			return new Response(1, "Not Allowed");
		}
		
		mainRepo.getEventRepo().delete(event);
		mainRepo.getEventRegistrationsRepo().deleteAll(mainRepo.getEventRegistrationsRepo().findByEventID(eventID));
		
		return new Response(0, "success", "");
	}
}

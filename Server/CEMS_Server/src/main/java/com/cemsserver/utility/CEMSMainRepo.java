package com.cemsserver.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cemsserver.entity.CurrentSession;
import com.cemsserver.entity.Event;
import com.cemsserver.entity.EventRegistrations;
import com.cemsserver.entity.IDGenCount;
import com.cemsserver.entity.User;
import com.cemsserver.model.UserType;
import com.cemsserver.repository.CurrentSessionRepo;
import com.cemsserver.repository.EventRegistrationsRepo;
import com.cemsserver.repository.EventRepo;
import com.cemsserver.repository.IDGenCountRepo;
import com.cemsserver.repository.UserRepo;
import com.google.gson.Gson;

@Component
public class CEMSMainRepo
{
	@Autowired
	private CurrentSessionRepo currentSessionRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private IDGenCountRepo iDGenCountRepo;
	
	@Autowired
	private EventRepo eventRepo;
	
	@Autowired
	private EventRegistrationsRepo eventRegistrationsRepo;
	
	@Autowired
	private Gson gson;
	
	public String getCurrentDate()
	{
		return new SimpleDateFormat("dd MMM yyyy").format(new Date());
	}
	
	public String getCurrentTime()
	{
		return new SimpleDateFormat("hh:mm a").format(new Date());
	}
	
	public String InitSession(UserType userType, String id)
	{
		String apiKey = generateRandomID(30);
		
		CurrentSession currentSession = new CurrentSession(apiKey, userType, id);
		currentSessionRepo.save(currentSession);
		
		return apiKey;
	}
	
	public CurrentSession getCurrentSessionFromApiKey(String apiKey)
	{
		Optional<CurrentSession> currentSessionOpt = currentSessionRepo.findByApiKey(apiKey);
		
		if(currentSessionOpt.isPresent())
		{
			return currentSessionOpt.get();
		}
		
		return null;
	}
	
	public User getUserFromApiKey(String apiKey)
	{
		Optional<CurrentSession> currentSessionOpt = currentSessionRepo.findByApiKey(apiKey);
		
		if(currentSessionOpt.isPresent())
		{
			CurrentSession currentSession = currentSessionOpt.get();
			
			Optional<User> userOpt = userRepo.findById(currentSession.getUserID());
			
			if(userOpt.isPresent())
			{
				return userOpt.get();
			}
			
			return null;
		}
		
		return null;
	}
	
	public Event getEventFromEventID(String eventID)
	{
		Optional<Event> eventOpt = eventRepo.findById(eventID);
		
		if(eventOpt.isPresent()) return eventOpt.get();
		
		return null;
	}
	
	public boolean checkEventExists(String eventID)
	{
		Optional<Event> eventOpt = eventRepo.findById(eventID);
		
		return eventOpt.isPresent();
	}
	
	public boolean checkAlreadyRegisteredToEvent(String userID, String eventID)
	{
		Optional<EventRegistrations> eventRegistrationsOptional = eventRegistrationsRepo.findByUserIDAndEventID(userID, eventID);
		
		return eventRegistrationsOptional.isPresent();
	}
	
	public String generateStudentID()
	{
		String id = null;

		Optional<IDGenCount> idGenCountOpt = iDGenCountRepo.findById(1);

		if (idGenCountOpt.isPresent())
		{
			IDGenCount idGenCount = idGenCountOpt.get();

			id = "SD-" + (idGenCount.getStudentIDCount() + 1);

			iDGenCountRepo.save(new IDGenCount(1, idGenCount.getStudentIDCount() + 1, idGenCount.getStaffIDCount(), idGenCount.getEventIDCount(), idGenCount.getEventRegIDCount()));
		} 
		else
		{
			id = "SD-1";

			iDGenCountRepo.save(new IDGenCount(1, 1, 0, 0, 0));
		}

		return id;
	}
	
	public String generateStaffID()
	{
		String id = null;

		Optional<IDGenCount> idGenCountOpt = iDGenCountRepo.findById(1);

		if (idGenCountOpt.isPresent())
		{
			IDGenCount idGenCount = idGenCountOpt.get();

			id = "ST-" + (idGenCount.getStaffIDCount() + 1);

			iDGenCountRepo.save(new IDGenCount(1, idGenCount.getStudentIDCount(), idGenCount.getStaffIDCount() + 1, idGenCount.getEventIDCount(), idGenCount.getEventRegIDCount()));
		} 
		else
		{
			id = "ST-1";

			iDGenCountRepo.save(new IDGenCount(1, 0, 1, 0, 0));
		}

		return id;
	}
	
	public String generateEventID()
	{
		String id = null;

		Optional<IDGenCount> idGenCountOpt = iDGenCountRepo.findById(1);

		if (idGenCountOpt.isPresent())
		{
			IDGenCount idGenCount = idGenCountOpt.get();

			id = "ET-" + (idGenCount.getEventIDCount() + 1);

			iDGenCountRepo.save(new IDGenCount(1, idGenCount.getStudentIDCount(), idGenCount.getStaffIDCount(), idGenCount.getEventIDCount() + 1, idGenCount.getEventRegIDCount()));
		} 
		else
		{
			id = "ET-1";

			iDGenCountRepo.save(new IDGenCount(1, 0, 0, 1, 0));
		}

		return id;
	}
	
	public String generateEventRegID()
	{
		String id = null;

		Optional<IDGenCount> idGenCountOpt = iDGenCountRepo.findById(1);

		if (idGenCountOpt.isPresent())
		{
			IDGenCount idGenCount = idGenCountOpt.get();

			id = "ER-" + (idGenCount.getEventRegIDCount() + 1);

			iDGenCountRepo.save(new IDGenCount(1, idGenCount.getStudentIDCount(), idGenCount.getStaffIDCount(), idGenCount.getEventIDCount(), idGenCount.getEventRegIDCount() + 1));
		} 
		else
		{
			id = "ER-1";

			iDGenCountRepo.save(new IDGenCount(1, 0, 0, 0, 1));
		}

		return id;
	}
	
	public String generateRandomID(int length)
	{
		char[][] pairs = {{'a', 'z'}, {'A', 'Z'}, {'0', '9'}};
		
		return new RandomStringGenerator.Builder().withinRange(pairs).build().generate(length);
	}

	public CurrentSessionRepo getCurrentSessionRepo()
	{
		return currentSessionRepo;
	}

	public UserRepo getUserRepo()
	{
		return userRepo;
	}

	public IDGenCountRepo getiDGenCountRepo()
	{
		return iDGenCountRepo;
	}

	public EventRepo getEventRepo()
	{
		return eventRepo;
	}

	public EventRegistrationsRepo getEventRegistrationsRepo()
	{
		return eventRegistrationsRepo;
	}

	public Gson getGson()
	{
		return gson;
	}
}

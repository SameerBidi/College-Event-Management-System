package com.cemsserver.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cemsserver.entity.EventRegistrations;

public interface EventRegistrationsRepo extends CrudRepository<EventRegistrations, String>
{
	Optional<EventRegistrations> findByUserIDAndEventID(String userID, String eventID);
	ArrayList<EventRegistrations> findByEventID(String eventID);
	ArrayList<EventRegistrations> findByUserID(String userID);
}

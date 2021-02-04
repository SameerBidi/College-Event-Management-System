package com.cemsserver.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.cemsserver.entity.Event;

public interface EventRepo extends CrudRepository<Event, String>
{
	ArrayList<Event> findAll();
	ArrayList<Event> findByEventRegistrarID(String eventRegistrarID);
}

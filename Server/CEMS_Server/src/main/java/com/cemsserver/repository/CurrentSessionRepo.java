package com.cemsserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cemsserver.entity.CurrentSession;

public interface CurrentSessionRepo extends CrudRepository<CurrentSession, String>
{
	Optional<CurrentSession> findByApiKey(String apiKey);
}

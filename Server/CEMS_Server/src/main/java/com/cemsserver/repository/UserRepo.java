package com.cemsserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cemsserver.entity.User;

public interface UserRepo extends CrudRepository<User, String>
{
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailConfirmID(String confirmID);
	Optional<User> findByRollno(String rollno);
	Optional<User> findByCno(String cno);
}

package com.gdsc.boilerplate.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdsc.boilerplate.springboot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String username);

	boolean existsByEmail(String email);

}

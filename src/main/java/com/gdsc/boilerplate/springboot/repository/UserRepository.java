package com.gdsc.boilerplate.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdsc.boilerplate.springboot.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String username);

	Optional<User> findById(Long id);

	boolean existsByEmail(String email);

}

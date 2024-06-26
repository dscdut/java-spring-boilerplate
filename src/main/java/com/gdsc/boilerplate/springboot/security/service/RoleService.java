package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.model.Role;
import com.gdsc.boilerplate.springboot.model.User;

import java.util.Optional;

public interface RoleService {
    Role findById(Long id);
    Role findByName(String name);
}

package com.gdsc.boilerplate.springboot.security.service;

import com.gdsc.boilerplate.springboot.model.Role;
import com.gdsc.boilerplate.springboot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}
package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.role.Role;
import com.lovejazz.gymsession.repository.RoleRepository;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository repository) {
        this.roleRepository = repository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Integer id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new RunNotFoundExceptions();
        }
        return new Role(role.get().id(),role.get().name());
    }

    public void create(Role role) {
        roleRepository.create(role);
    }

    public void update(Role role, Integer id) {
        roleRepository.update(role, id);
    }

    public void delete(Integer id) {
        roleRepository.delete(id);
    }

}

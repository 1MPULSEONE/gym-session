package com.lovejazz.gymsession.controller;


import com.lovejazz.gymsession.model.role.Role;
import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService service) {
        this.roleService = service;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    List<Role> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Role findById(@PathVariable Integer id) {
        return roleService.findById(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    void create(@Valid @RequestBody Role role) {
        roleService.create(role);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    void update(@Valid @RequestBody Role role, @PathVariable Integer id) {
        roleService.update(role, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    void delete(@PathVariable Integer id) {
        roleService.delete(id);
    }


}

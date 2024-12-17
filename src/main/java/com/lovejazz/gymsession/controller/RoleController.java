package com.lovejazz.gymsession.controller;


import com.lovejazz.gymsession.model.role.Role;
import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    List<Role> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    Role findById(@PathVariable Integer id) {
        return roleService.findById(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Role role) {
        roleService.create(role);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Role role, @PathVariable Integer id) {
        roleService.update(role, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        roleService.delete(id);
    }


}

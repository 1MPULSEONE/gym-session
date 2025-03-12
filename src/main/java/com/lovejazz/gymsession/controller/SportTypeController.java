package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.service.SportTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sportType")
public class SportTypeController {

    private final SportTypeService sportTypeService;

    public SportTypeController(SportTypeService service) {
        this.sportTypeService = service;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('client_admin')")
    List<SportTypeDAO> findAll() {
        return sportTypeService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('client_user') and #id == authentication.principal.id")
    SportTypeDAO findById(@PathVariable Integer id) {
        return sportTypeService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @PreAuthorize("hasRole('client_admin')")
    void create(@Valid @RequestBody SportTypeDAO sportTypeDAO) {
        sportTypeService.create(sportTypeDAO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    void update(@Valid @RequestBody SportTypeDAO sportTypeDAO, @PathVariable Integer id) {
        sportTypeService.update(sportTypeDAO, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")

    void delete(@PathVariable Integer id) {
        sportTypeService.delete(id);
    }

}

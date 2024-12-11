package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.service.SportTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    List<SportTypeDAO> findAll() {
        return sportTypeService.findAll();
    }

    @GetMapping("/{id}")
    SportTypeDAO findById(@PathVariable Integer id) {
        return sportTypeService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody SportTypeDAO sportTypeDAO) {
        sportTypeService.create(sportTypeDAO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody SportTypeDAO sportTypeDAO, @PathVariable Integer id) {
        sportTypeService.update(sportTypeDAO, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        sportTypeService.delete(id);
    }

}

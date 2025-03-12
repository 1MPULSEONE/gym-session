package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.model.sportType.SportTypeDTO;
import com.lovejazz.gymsession.repository.SportTypeRepository;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SportTypeService {
    private final SportTypeRepository sportTypeRepository;

    public SportTypeService(SportTypeRepository repository) {
        this.sportTypeRepository = repository;
    }

    public List<SportTypeDAO> findAll() {
        List<SportTypeDAO> sportTypeDAOList = new ArrayList<SportTypeDAO>();

        List<SportTypeDTO> sportTypeDTOList = sportTypeRepository.findAll();

        for (SportTypeDTO sportTypeDTO : sportTypeDTOList) {
            SportTypeDAO sportTypeDAO = new SportTypeDAO(sportTypeDTO.id(), sportTypeDTO.title());
            sportTypeDAOList.add(sportTypeDAO);
        }

        return sportTypeDAOList;
    }

    public SportTypeDAO findById(Integer id) {
        Optional<SportTypeDTO> sportTypeDTO = sportTypeRepository.findById(id);
        //TODO: add error handling
        if (sportTypeDTO.isEmpty()) {
            throw new RunNotFoundExceptions();
        }
        return new SportTypeDAO(sportTypeDTO.get().id(), sportTypeDTO.get().title());
    }

    public void create(SportTypeDAO sportTypeDAO) {
        SportTypeDTO sportTypeDTO = new SportTypeDTO(sportTypeDAO.id(), sportTypeDAO.title());
        sportTypeRepository.create(sportTypeDTO);
    }

    public void update(SportTypeDAO sportTypeDAO, Integer id) {
        SportTypeDTO sportTypeDTO = new SportTypeDTO(sportTypeDAO.id(), sportTypeDAO.title());
        sportTypeRepository.update(sportTypeDTO, id);
    }

    public void delete(Integer id) {
        sportTypeRepository.delete(id);
    }
}

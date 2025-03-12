package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.model.sportType.SportTypeDTO;
import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDAO;
import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDTO;
import com.lovejazz.gymsession.repository.SportTypeRepository;
import com.lovejazz.gymsession.repository.TrainingDiaryRepository;
import com.lovejazz.gymsession.security.jwt.AuthEntryPointJwt;
import com.lovejazz.gymsession.security.service.UserDetailsImpl;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingDiaryService {
    private final TrainingDiaryRepository trainingDiaryRepository;
    private final SportTypeRepository sportTypeRepository;
    private static final Logger logger = LoggerFactory.getLogger(TrainingDiaryService.class);


    public TrainingDiaryService(TrainingDiaryRepository trainingDiaryRepository, SportTypeRepository sportTypeRepository) {
        this.trainingDiaryRepository = trainingDiaryRepository;
        this.sportTypeRepository = sportTypeRepository;
    }

    public List<TrainingDiaryDAO> findAll() {
        List<TrainingDiaryDAO> trainingDiaryDAOList = new ArrayList<>();

        List<TrainingDiaryDTO> trainingDiaryDTOList = trainingDiaryRepository.findAll();

        for (TrainingDiaryDTO trainingDiaryDTO : trainingDiaryDTOList) {
            Optional<SportTypeDTO> sportTypeDTO = sportTypeRepository.findById(trainingDiaryDTO.sportTypeId());
            if (sportTypeDTO.isEmpty()) {
                throw new RunNotFoundExceptions();
            }
            SportTypeDAO sportTypeDAO = new SportTypeDAO(sportTypeDTO.get().id(), sportTypeDTO.get().title());
            TrainingDiaryDAO trainingDiaryDAO = new TrainingDiaryDAO(trainingDiaryDTO.id(), trainingDiaryDTO.userId(), trainingDiaryDTO.name(), sportTypeDAO);
            trainingDiaryDAOList.add(trainingDiaryDAO);
        }
        return trainingDiaryDAOList;
    }

    public TrainingDiaryDAO findById(Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                Optional<TrainingDiaryDTO> trainingDiaryDTO = trainingDiaryRepository.findById(id, userDetails.getId());
                if (trainingDiaryDTO.isEmpty()) {
                    throw new RunNotFoundExceptions();
                }
                Optional<SportTypeDTO> sportTypeDTO = sportTypeRepository.findById(trainingDiaryDTO.get().sportTypeId());
                if (sportTypeDTO.isEmpty()) {
                    throw new RunNotFoundExceptions();
                }
                SportTypeDAO sportTypeDAO = new SportTypeDAO(sportTypeDTO.get().id(), sportTypeDTO.get().title());

                return new TrainingDiaryDAO(trainingDiaryDTO.get().id(), trainingDiaryDTO.get().userId(), trainingDiaryDTO.get().name(), sportTypeDAO);
            } else {
                throw new RunNotFoundExceptions();
            }
        } else {
            throw new RunNotFoundExceptions();
        }

    }

    public void create(TrainingDiaryDAO trainingDiaryDAO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                TrainingDiaryDTO trainingDiaryDTO = new TrainingDiaryDTO(trainingDiaryDAO.id(), trainingDiaryDAO.userId(), trainingDiaryDAO.name(), trainingDiaryDAO.sportType().id());
                trainingDiaryRepository.create(trainingDiaryDTO, userDetails.getId());
            } else {
                throw new RunNotFoundExceptions();

            }

        } else {
            throw new RunNotFoundExceptions();
        }

    }

    public void update(TrainingDiaryDAO trainingDiaryDAO, Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                TrainingDiaryDTO trainingDiaryDTO = new TrainingDiaryDTO(trainingDiaryDAO.id(), trainingDiaryDAO.userId(), trainingDiaryDAO.name(), trainingDiaryDAO.sportType().id());
                trainingDiaryRepository.update(trainingDiaryDTO, id, userDetails.getId());
            } else {
                throw new RunNotFoundExceptions();

            }
        } else {
            throw new RunNotFoundExceptions();
        }

    }

    public void delete(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                trainingDiaryRepository.delete(id, userDetails.getId());
            } else {
                throw new RunNotFoundExceptions();

            }

        } else {
            throw new RunNotFoundExceptions();

        }
    }
}

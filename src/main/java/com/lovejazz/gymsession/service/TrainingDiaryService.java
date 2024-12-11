package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.model.sportType.SportTypeDTO;
import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDAO;
import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDTO;
import com.lovejazz.gymsession.repository.SportTypeRepository;
import com.lovejazz.gymsession.repository.TrainingDiaryRepository;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingDiaryService {
    private final TrainingDiaryRepository trainingDiaryRepository;
    private final SportTypeRepository sportTypeRepository;

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
        Optional<TrainingDiaryDTO> trainingDiaryDTO = trainingDiaryRepository.findById(id);
        if (trainingDiaryDTO.isEmpty()) {
            throw new RunNotFoundExceptions();
        }
        Optional<SportTypeDTO> sportTypeDTO = sportTypeRepository.findById(trainingDiaryDTO.get().sportTypeId());
        if (sportTypeDTO.isEmpty()) {
            throw new RunNotFoundExceptions();
        }
        SportTypeDAO sportTypeDAO = new SportTypeDAO(sportTypeDTO.get().id(), sportTypeDTO.get().title());

        return new TrainingDiaryDAO(trainingDiaryDTO.get().id(), trainingDiaryDTO.get().userId(), trainingDiaryDTO.get().name(), sportTypeDAO);
    }

    public void create(TrainingDiaryDAO trainingDiaryDAO) {
        TrainingDiaryDTO trainingDiaryDTO = new TrainingDiaryDTO(trainingDiaryDAO.id(), trainingDiaryDAO.userId(), trainingDiaryDAO.name(), trainingDiaryDAO.sportType().id());
        trainingDiaryRepository.create(trainingDiaryDTO);
    }

    public void update(TrainingDiaryDAO trainingDiaryDAO, Integer id) {
        TrainingDiaryDTO trainingDiaryDTO = new TrainingDiaryDTO(trainingDiaryDAO.id(), trainingDiaryDAO.userId(), trainingDiaryDAO.name(), trainingDiaryDAO.sportType().id());
        trainingDiaryRepository.update(trainingDiaryDTO, id);
    }

    public void delete(Integer id) {
        trainingDiaryRepository.delete(id);

    }
}

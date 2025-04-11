package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import com.lovejazz.gymsession.model.sportType.SportTypeDTO;
import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDAO;
import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDTO;
import com.lovejazz.gymsession.repository.SportTypeRepository;
import com.lovejazz.gymsession.repository.TrainingDiaryRepository;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.UUID;

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
            if (principal instanceof Jwt jwtPrincipal) {
                UUID userId = getUserIdFromJwt(jwtPrincipal);
                Optional<TrainingDiaryDTO> trainingDiaryDTO = trainingDiaryRepository.findById(id, userId);
                if (trainingDiaryDTO.isEmpty()) {
                    throw new RunNotFoundExceptions();
                }
                Optional<SportTypeDTO> sportTypeDTO = sportTypeRepository.findById(trainingDiaryDTO.get().sportTypeId());
                if (sportTypeDTO.isEmpty()) {
                    logger.error("Не найден SportType с id={}, связанный с TrainingDiary id={}", trainingDiaryDTO.get().sportTypeId(), id);
                    throw new RunNotFoundExceptions();
                }
                SportTypeDAO sportTypeDAO = new SportTypeDAO(sportTypeDTO.get().id(), sportTypeDTO.get().title());

                return new TrainingDiaryDAO(trainingDiaryDTO.get().id(), trainingDiaryDTO.get().userId(), trainingDiaryDTO.get().name(), sportTypeDAO);
            } else {
                logger.warn("Principal не является объектом Jwt: {}", principal.getClass().getName());
                throw new IllegalStateException("Неподдерживаемый тип аутентификации для этого действия.");
            }
        } else {
            logger.error("Аутентификация не найдена в SecurityContext.");
            throw new RunNotFoundExceptions();
        }
    }

    public void create(TrainingDiaryDAO trainingDiaryDAO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwtPrincipal) {

                UUID userId = getUserIdFromJwt(jwtPrincipal);

                TrainingDiaryDTO trainingDiaryDTO = new TrainingDiaryDTO(
                        trainingDiaryDAO.id(),
                        userId,
                        trainingDiaryDAO.name(),
                        trainingDiaryDAO.sportType().id()
                );

                trainingDiaryRepository.create(trainingDiaryDTO, userId);

            } else {
                logger.warn("Principal не является объектом Jwt: {}", principal.getClass().getName());
                throw new IllegalStateException("Неподдерживаемый тип аутентификации для этого действия.");
            }

        } else {
            logger.error("Аутентификация не найдена в SecurityContext.");
            throw new RunNotFoundExceptions();
        }

    }

    public void update(TrainingDiaryDAO trainingDiaryDAO, Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt jwtPrincipal) {
                UUID userId = getUserIdFromJwt(jwtPrincipal);
                TrainingDiaryDTO trainingDiaryDTO = new TrainingDiaryDTO(
                        trainingDiaryDAO.id(),
                        userId,
                        trainingDiaryDAO.name(),
                        trainingDiaryDAO.sportType().id()
                );
                trainingDiaryRepository.update(trainingDiaryDTO, id, userId);
            } else {
                logger.warn("Principal не является объектом Jwt: {}", principal.getClass().getName());
                throw new IllegalStateException("Неподдерживаемый тип аутентификации для этого действия.");
            }
        } else {
            logger.error("Аутентификация не найдена в SecurityContext.");
            throw new RunNotFoundExceptions();
        }
    }

    public void delete(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt jwtPrincipal) {
                UUID userId = getUserIdFromJwt(jwtPrincipal);
                trainingDiaryRepository.delete(id, userId);
            } else {
                logger.warn("Principal не является объектом Jwt: {}", principal.getClass().getName());
                throw new IllegalStateException("Неподдерживаемый тип аутентификации для этого действия.");
            }
        } else {
            logger.error("Аутентификация не найдена в SecurityContext.");
            throw new RunNotFoundExceptions();
        }
    }

    private UUID getUserIdFromJwt(Jwt jwtPrincipal) {
        String keycloakUserIdString = jwtPrincipal.getSubject();
        try {
            return UUID.fromString(keycloakUserIdString);
        } catch (IllegalArgumentException e) {
            logger.error("Не удалось преобразовать sub claim '{}' в UUID.", keycloakUserIdString, e);
            throw new RuntimeException("Неверный формат ID пользователя в токене.", e);
        }
    }
}

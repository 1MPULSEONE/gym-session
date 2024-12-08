package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.SportType;
import com.lovejazz.gymsession.model.TrainingDiary;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class TrainingDiaryRepository {
    private final List<TrainingDiary> trainingDiaryList = new ArrayList<>();

    public List<TrainingDiary> findAll() {
        return trainingDiaryList;
    }

    public Optional<TrainingDiary> findById(Integer id) {
        return trainingDiaryList.stream().filter(run -> run.id().equals(id))
                .findFirst();
    }


    public void create(TrainingDiary diary) {
        trainingDiaryList.add(diary);
    }

    public void update(TrainingDiary diary, Integer id) {
        for (int i = 0; i < trainingDiaryList.size(); i++) {
            if (trainingDiaryList.get(i).id().equals(id)) {
                TrainingDiary existingDiary = trainingDiaryList.get(i);
                trainingDiaryList.set(i, new TrainingDiary(
                        existingDiary.id(),
                        diary.userId() != null ? diary.userId() : existingDiary.userId(),
                        diary.name() != null ? diary.name() : existingDiary.name(),
                        diary.sportType() != null ? diary.sportType() : existingDiary.sportType()
                ));
                break;
            }
        }
    }

    public void delete(Integer id) {
        trainingDiaryList.removeIf(diary -> diary.id().equals(id));
    }

    @PostConstruct
    private void init() {
        trainingDiaryList.add(new TrainingDiary(
                1, 1, "MyTrainingDiary1", SportType.GYM
        ));
        trainingDiaryList.add(new TrainingDiary(
                2, 1, "MyTrainingDiary2", SportType.POOL
        ));
    }
}

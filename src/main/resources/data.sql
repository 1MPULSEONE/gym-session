INSERT INTO Sport_Type (title) VALUES
('Gym'),
('Swimming');

INSERT INTO Training_Diary (user_id, name, sport_type_id) VALUES
(1, 'GymDiary', (SELECT id FROM Sport_Type WHERE title = 'Gym')),
(1, 'Swimming', (SELECT id FROM Sport_Type WHERE title = 'Swimming')),
(1, 'GymDiary2', (SELECT id FROM Sport_Type WHERE title = 'Gym')),
(1, 'Swimming2', (SELECT id FROM Sport_Type WHERE title = 'Swimming')),
(1, 'GymDiary3', (SELECT id FROM Sport_Type WHERE title = 'Gym'));
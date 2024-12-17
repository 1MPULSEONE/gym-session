INSERT INTO sport_type (id,title)
VALUES (1,'Gym'),
       (2,'Swimming');

INSERT INTO training_diary (id,user_id, name, sport_type_id)
VALUES (1,1, 'GymDiary', (SELECT id FROM sport_type WHERE title = 'Gym')),
       (2,1, 'Swimming', (SELECT id FROM sport_type WHERE title = 'Swimming')),
       (3,1, 'GymDiary2', (SELECT id FROM sport_type WHERE title = 'Gym')),
       (4,1, 'Swimming2', (SELECT id FROM sport_type WHERE title = 'Swimming')),
       (5,1, 'GymDiary3', (SELECT id FROM sport_type WHERE title = 'Gym'));

INSERT INTO role (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');


--password -> qwerty123
    INSERT INTO  user_data (id,password,username)
    VALUES
        (3,'$2a$10$H.x3e7KzCdDB5YhoBQ0wm.In0kfR.zxEmS1cr.y49SHEgCR2M.ode','admin'),
        (4,'$2a$10$H.x3e7KzCdDB5YhoBQ0wm.In0kfR.zxEmS1cr.y49SHEgCR2M.ode','vanya');

INSERT INTO  user_roles (role_id, user_id) VALUES (1,3),(2,4);


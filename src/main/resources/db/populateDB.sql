DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2023-06-19 9:40:00', 'Завтрак', 500, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2023-06-19 13:40:00', 'Обед', 800, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2023-06-19 19:40:00', 'Ужин', 1000, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2023-06-20 7:30:00', 'Завтрак', 400, 100000);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2023-06-19 12:40:00', 'Админ-Ланч', 400, 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2023-06-19 19:10:00', 'Админ-Ужин', 1400, 100001);

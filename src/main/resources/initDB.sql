DROP TABLE dishes IF EXISTS CASCADE;
DROP TABLE restaurants IF EXISTS cascade ;
DROP TABLE users IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE global_seq AS BIGINT START WITH 1;

CREATE TABLE restaurants
(
    id            BIGINT       GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name          VARCHAR(60)  NOT NULL,
    votesCount    INTEGER      NOT NULL
);

CREATE TABLE users
(
    id                        BIGINT        GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name                      VARCHAR(60)   NOT NULL,
    email                     VARCHAR(255)  NOT NULL,
    password                  VARCHAR(60)   NOT NULL,
    role                      VARCHAR(5)    NOT NULL,
    chosenRestaurantId        BIGINT        REFERENCES restaurants(id)
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE dishes (
    id                BIGINT      GENERATED BY DEFAULT AS SEQUENCE global_seq PRIMARY KEY,
    name              VARCHAR(60) NOT NULL,
    price             DOUBLE      NOT NULL,
    restaurantId      BIGINT      NOT NULL,
    FOREIGN KEY (restaurantId) REFERENCES restaurants (id) ON DELETE CASCADE
);

INSERT INTO users (name, email, password, role, chosenRestaurantId)
VALUES ('User1', 'user1@gmail.com', 'user1', 'USER', null),
       ('User2', 'user2@gmail.com', 'user2', 'USER', null),
       ('Admin1', 'admin1@gmail.com', 'admin1', 'ADMIN', null);

INSERT INTO restaurants(name, votesCount)
VALUES ('Some restaurant', 0),
       ('Another restaurant', 0);

INSERT INTO dishes(name, price, restaurantId)
VALUES ('Fish', 23.4, 4),
       ('Steak', 45.5, 4),
       ('Salad', 12.4, 5),
       ('Soup', 20.5, 5);


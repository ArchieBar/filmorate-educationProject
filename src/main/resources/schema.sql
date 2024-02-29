-- Создание таблицы user, если она еще не существует
CREATE TABLE IF NOT EXISTS users (
  id_user INT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  login VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  birthday DATE NOT NULL,
  PRIMARY KEY (id_user)
);

-- Создание таблицы acceptance_status_list, если она еще не существует
CREATE TABLE IF NOT EXISTS acceptance_status_list (
  id_acceptance_status INT NOT NULL AUTO_INCREMENT,
  acceptance_status VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_acceptance_status)
);

-- Создание таблицы users_friendship, если она еще не существует
CREATE TABLE IF NOT EXISTS users_friendship (
  id_user_1 INT NOT NULL,
  id_user_2 INT NOT NULL,
  id_acceptance_status INT NOT NULL,
  PRIMARY KEY (id_user_1, id_user_2),
  FOREIGN KEY (id_user_1) REFERENCES users (id_user),
  FOREIGN KEY (id_user_2) REFERENCES users (id_user),
  FOREIGN KEY (id_acceptance_status) REFERENCES acceptance_status_list (id_acceptance_status)
);

-- Создание таблицы ratings_list, если она еще не существует
CREATE TABLE IF NOT EXISTS mpa_list (
  id_mpa INT NOT NULL AUTO_INCREMENT,
  mpa VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_mpa)
);

-- Создание таблицы film, если она еще не существует
CREATE TABLE IF NOT EXISTS film (
  id_film INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  release_date DATE NOT NULL,
  duration INT NOT NULL,
  rate DOUBLE DEFAULT 0.0,
  id_mpa INT,
  PRIMARY KEY (id_film),
  FOREIGN KEY (id_mpa) REFERENCES mpa_list (id_mpa)
);

-- Создание таблицы film_likes, если она еще не существует
CREATE TABLE IF NOT EXISTS film_likes (
  id_film INT NOT NULL,
  id_user INT NOT NULL,
  PRIMARY KEY (id_film, id_user),
  FOREIGN KEY (id_film) REFERENCES film (id_film),
  FOREIGN KEY (id_user) REFERENCES users (id_user)
);

-- Создание таблицы genres_list, если она еще не существует
CREATE TABLE IF NOT EXISTS genres_list (
  id_genre INT NOT NULL AUTO_INCREMENT,
  genre VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_genre)
);

-- Создание таблицы genres_film, если она еще не существует
CREATE TABLE IF NOT EXISTS genres_film (
  id_film INT NOT NULL,
  id_genre INT NOT NULL,
  PRIMARY KEY (id_film, id_genre),
  FOREIGN KEY (id_film) REFERENCES film (id_film),
  FOREIGN KEY (id_genre) REFERENCES genres_list (id_genre)
);
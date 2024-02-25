-- Вставка данных в таблицу acceptance_status_list, если она еще не существует
INSERT INTO acceptance_status_list (id_acceptance_status, acceptance_status)
SELECT 1, 'Запрос на дружбу'
WHERE NOT EXISTS (
    SELECT 1
    FROM acceptance_status_list
    WHERE id_acceptance_status = 1
);

INSERT INTO acceptance_status_list (id_acceptance_status, acceptance_status)
SELECT 2, 'Запрос принят'
WHERE NOT EXISTS (
    SELECT 1
    FROM acceptance_status_list
    WHERE id_acceptance_status = 2
);

INSERT INTO acceptance_status_list (id_acceptance_status, acceptance_status)
SELECT 3, 'Запрос отклонен'
WHERE NOT EXISTS (
    SELECT 1
    FROM acceptance_status_list
    WHERE id_acceptance_status = 3
);

-- Вставка данных в таблицу ratings_list, если она еще не существует
INSERT INTO ratings_list (id_rating, rating)
SELECT 1, 'G'
WHERE NOT EXISTS (
    SELECT 1
    FROM ratings_list
    WHERE id_rating = 1
);

INSERT INTO ratings_list (id_rating, rating)
SELECT 2, 'PG'
WHERE NOT EXISTS (
    SELECT 1
    FROM ratings_list
    WHERE id_rating = 2
);

INSERT INTO ratings_list (id_rating, rating)
SELECT 3, 'PG-13'
WHERE NOT EXISTS (
    SELECT 1
    FROM ratings_list
    WHERE id_rating = 3
);

INSERT INTO ratings_list (id_rating, rating)
SELECT 4, 'R'
WHERE NOT EXISTS (
    SELECT 1
    FROM ratings_list
    WHERE id_rating = 4
);

INSERT INTO ratings_list (id_rating, rating)
SELECT 5, 'NC-17'
WHERE NOT EXISTS (
    SELECT 1
    FROM ratings_list
    WHERE id_rating = 5
);

-- Вставка данных в таблицу genres_list, если она еще не существует
INSERT INTO genres_list (id_genre, genre)
SELECT 1, 'Боевик'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 1
);

INSERT INTO genres_list (id_genre, genre)
SELECT 2, 'Комедия'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 2
);

INSERT INTO genres_list (id_genre, genre)
SELECT 3, 'Драма'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 3
);

INSERT INTO genres_list (id_genre, genre)
SELECT 4, 'Фантастика'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 4
);

INSERT INTO genres_list (id_genre, genre)
SELECT 5, 'Ужасы'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 5
);

INSERT INTO genres_list (id_genre, genre)
SELECT 6, 'Триллер'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 6
);

INSERT INTO genres_list (id_genre, genre)
SELECT 7, 'Приключения'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 7
);

INSERT INTO genres_list (id_genre, genre)
SELECT 8, 'Фэнтези'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 8
);

INSERT INTO genres_list (id_genre, genre)
SELECT 9, 'Вестерн'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 9
);
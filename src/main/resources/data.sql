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

-- Вставка данных в таблицу mpa_list, если она еще не существует
INSERT INTO mpa_list (id_mpa, mpa)
SELECT 1, 'G'
WHERE NOT EXISTS (
    SELECT 1
    FROM mpa_list
    WHERE id_mpa = 1
);

INSERT INTO mpa_list (id_mpa, mpa)
SELECT 2, 'PG'
WHERE NOT EXISTS (
    SELECT 1
    FROM mpa_list
    WHERE id_mpa = 2
);

INSERT INTO mpa_list (id_mpa, mpa)
SELECT 3, 'PG-13'
WHERE NOT EXISTS (
    SELECT 1
    FROM mpa_list
    WHERE id_mpa = 3
);

INSERT INTO mpa_list (id_mpa, mpa)
SELECT 4, 'R'
WHERE NOT EXISTS (
    SELECT 1
    FROM mpa_list
    WHERE id_mpa = 4
);

INSERT INTO mpa_list (id_mpa, mpa)
SELECT 5, 'NC-17'
WHERE NOT EXISTS (
    SELECT 1
    FROM mpa_list
    WHERE id_mpa = 5
);

-- Вставка данных в таблицу genres_list, если она еще не существует
INSERT INTO genres_list (id_genre, genre)
SELECT 1, 'Комедия'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 1
);

INSERT INTO genres_list (id_genre, genre)
SELECT 2, 'Драма'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 2
);

INSERT INTO genres_list (id_genre, genre)
SELECT 3, 'Мультфильм'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 3
);

INSERT INTO genres_list (id_genre, genre)
SELECT 4, 'Триллер'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 4
);

INSERT INTO genres_list (id_genre, genre)
SELECT 5, 'Документальный'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 5
);

INSERT INTO genres_list (id_genre, genre)
SELECT 6, 'Боевик'
WHERE NOT EXISTS (
    SELECT 1
    FROM genres_list
    WHERE id_genre = 6
);
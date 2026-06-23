CREATE TABLE IF NOT EXISTS libros (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    categoria VARCHAR(40) NOT NULL,
    cantidad_total INT NOT NULL,
    cantidad_disponible INT NOT NULL,
    activo BIT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_libros_isbn (isbn)
);

INSERT IGNORE INTO libros ( -- usamos ignore para decirle a sql que ignore los duplicados (isbn) y no genere error
    titulo,
    autor,
    isbn,
    categoria,
    cantidad_total,
    cantidad_disponible,
    activo
) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'SOFTWARE', 3, 3, b'1'),
('Don Quijote de la Mancha', 'Miguel de Cervantes', '9788491050297', 'NOVELA', 3, 3, b'1'),
('Dracula', 'Bram Stoker', '9780486411095', 'TERROR', 3, 3, b'1'),
('Hamlet', 'William Shakespeare', '9780743477123', 'DRAMA', 3, 3, b'1'),
('Dune', 'Frank Herbert', '9780441172719', 'CIENCIA_FICCION', 3, 3, b'1'),
('The Hobbit', 'J. R. R. Tolkien', '9780547928227', 'FANTASIA', 3, 3, b'1'),
('Sapiens', 'Yuval Noah Harari', '9780062316097', 'HISTORIA', 3, 3, b'1'),
('Steve Jobs', 'Walter Isaacson', '9781451648539', 'BIOGRAFIA', 3, 3, b'1'),
('Veinte poemas de amor y una cancion desesperada', 'Pablo Neruda', '9788497592722', 'POESIA', 3, 3, b'1'),
('El principito', 'Antoine de Saint-Exupery', '9780156012195', 'INFANTIL', 3, 3, b'1');

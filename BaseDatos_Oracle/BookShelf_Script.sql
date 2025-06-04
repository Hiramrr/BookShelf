CREATE TYPE Usuario_t AS OBJECT (id int,nombre VARCHAR(50), correo VARCHAR(100), contraseña varchar(70));
CREATE TYPE Libro_t AS OBJECT (id int, titulo VARCHAR(80), autor VARCHAR(50),editorial varchar(50), num_copias int, sinopsis varchar(1000),categorias Categoria_tab_t);
CREATE TYPE Categoria_t AS OBJECT (nombre varchar(50));
CREATE TYPE Prestamo_t AS OBJECT (id int, fecha_solicitud date, fecha_devolucion date, estado int, id_usuario int, id_libro int);

CREATE TABLE Prestamos OF Prestamo_t;

CREATE TYPE Categoria_tab_t AS TABLE OF Categoria_t;


CREATE TABLE Usuarios OF Usuario_t
  (PRIMARY KEY(id));

CREATE TABLE Libros OF Libro_t (
  PRIMARY KEY(id)
)
NESTED TABLE categorias STORE AS categorias_tabla;

CREATE TABLE Prestamos OF Prestamo_t;


DROP TABLE PRESTAMOS;
DROP TYPE PRESTAMO_t;

SELECT * FROM PRESTAMOS;


INSERT INTO Usuarios VALUES (Usuario_t(1, 'Miyu Maldonado', 'miyu@gmail.com', 'contra123'));
INSERT INTO Usuarios VALUES (Usuario_t(2, 'Coco', 'coco@hotmail.com', 'pass456'));

INSERT INTO Libros VALUES (
  Libro_t(
    101,
    'Miyu el huerfanito',
    'Coco UV',
    'SEV',
    5,
    'Una novela dramatica que narra la vida de miyu',
    Categoria_tab_t(Categoria_t('Drama'), Categoria_t('Novela'))
  )
);

INSERT INTO Libros VALUES (
  Libro_t(
    102,
    'Introduccion a base de datos',
    'Edgar',
    'UV',
    3,
    'Introduccion a las bases de datos objeto relacional.',
    Categoria_tab_t(Categoria_t('Alegre'), Categoria_t('Educativo'))
  )
);


DROP TYPE Usuario_t;
DROP TYPE Libro_t;
DROP TYPE Prestamo_t;


--Consultas

SELECT * From usuarios;

--libro y categoria
SELECT l.id, l.titulo, c.nombre AS categoria
FROM Libros l,
     TABLE(l.categorias) c;

--Libros info
SELECT l.titulo, l.autor, l.num_copias
FROM Libros l;

     
--reseñas
SELECT r.id,
       r.texto,
       r.calificacion,
       r.fecha,
       DEREF(r.usuario).nombre AS usuario_nombre,
       DEREF(r.libro).titulo   AS libro_titulo
FROM Reseñas r;


--Prestamos faltan datos

SELECT DEREF(p.usuario).nombre AS usuario,
       DEREF(p.libro).titulo AS libro,
       p.fecha_solicitud,
       p.fecha_devolucion,
       p.estado
FROM Prestamos p
WHERE DEREF(p.usuario).id = 2;

SELECT DEREF(p.usuario).nombre AS usuario,
       DEREF(p.libro).titulo   AS libro,
       p.fecha_devolucion
FROM Prestamos p
WHERE p.estado = 1 ;

SELECT * FROM Usuarios;

SELECT l.titulo, l.autor
FROM Libros l;

CREATE OR REPLACE VIEW TodosLibros AS
SELECT l.id, l.titulo, l.autor, l.editorial, l.num_copias, l.sinopsis,
  LISTAGG(c.nombre, ', ') AS categorias
FROM Libros l,
     TABLE(l.categorias) c
GROUP BY l.id, l.titulo, l.autor, l.editorial, l.num_copias, l.sinopsis;

SELECT * FROM TodosLibros;

DROP VIEW TodosLibros;

SELECT DISTINCT c.nombre
FROM Libros l,
TABLE(l.categorias) c;

SELECT * FROM USUARIOS;

UPDATE USUARIOS SET CORREO = 'prueba@gmail.com' WHERE ID = 1;
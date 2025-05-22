CREATE TYPE Usuario_t AS OBJECT (id int,nombre VARCHAR(50), correo VARCHAR(100), contraseña varchar(70));
CREATE TYPE Libro_t AS OBJECT (id int, titulo VARCHAR(80), autor VARCHAR(50),editorial varchar(50), num_copias int, sinopsis varchar(1000),categorias Categoria_tab_t);
CREATE TYPE Reseña_t AS OBJECT (id int, texto varchar(1000), calificacion int , fecha date, usuario REF Usuario_t,libro REF Libro_t);
CREATE TYPE Categoria_t AS OBJECT (nombre varchar(50));
CREATE TYPE Prestamo_t AS OBJECT (id int, fecha_solicitud date, fecha_recogida date, estado varchar(50), usuario REF Usuario_t, libro REF Libro_t);


CREATE TYPE Categoria_tab_t AS TABLE OF Categoria_t;


CREATE TABLE Usuarios OF Usuario_t
  (PRIMARY KEY(id));

CREATE TABLE Libros OF Libro_t (
  PRIMARY KEY(id)
)
NESTED TABLE categorias STORE AS categorias_tabla;

CREATE TABLE Reseñas OF Reseña_t;

CREATE TABLE Prestamos OF Prestamo_t;



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

INSERT INTO Reseñas
SELECT Reseña_t(
  1001,
  'Una obra maestra de la literatura',
  5,
  DATE '2024-06-01',
  REF(u),
  REF(l)
)
FROM Usuarios u, Libros l
WHERE u.id = 1 AND l.id = 101;

INSERT INTO Reseñas
SELECT Reseña_t(
  1002,
  'Increible.',
  4,
  DATE '2024-07-10',
  REF(u),
  REF(l)
)
FROM Usuarios u, Libros l
WHERE u.id = 2 AND l.id = 102;



DROP TYPE Usuario_t;
DROP TYPE Libro_t;
DROP TYPE Reseña_t;
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

--Usuarios que subieron reseñas
SELECT DISTINCT DEREF(r.usuario).nombre AS usuario
FROM Reseñas r;

--Prestamos faltan datos

SELECT DEREF(p.usuario).nombre AS usuario,
       DEREF(p.libro).titulo AS libro,
       p.fecha_solicitud,
       p.fecha_recogida,
       p.estado
FROM Prestamos p
WHERE DEREF(p.usuario).id = 2;

SELECT DEREF(p.usuario).nombre AS usuario,
       DEREF(p.libro).titulo   AS libro,
       p.fecha_recogida
FROM Prestamos p
WHERE p.estado = 'Entregado';

SELECT * FROM Usuarios;

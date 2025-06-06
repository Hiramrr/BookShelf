--------------------------------------------------------
-- Archivo creado  - martes-junio-03-2025
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Type CATEGORIA_T
--------------------------------------------------------

CREATE OR REPLACE EDITIONABLE TYPE "BIBLIOTECA"."CATEGORIA_T" AS OBJECT (nombre varchar(50));

/
--------------------------------------------------------
--  DDL for Type CATEGORIA_TAB_T
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TYPE "BIBLIOTECA"."CATEGORIA_TAB_T" AS TABLE OF Categoria_t;

/
--------------------------------------------------------
--  DDL for Type LIBRO_T
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TYPE "BIBLIOTECA"."LIBRO_T" AS OBJECT (id int, titulo VARCHAR(80), autor VARCHAR(50),editorial varchar(50), num_copias int, sinopsis varchar(1000),categorias Categoria_tab_t);

/
--------------------------------------------------------
--  DDL for Type PRESTAMO_T
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TYPE "BIBLIOTECA"."PRESTAMO_T" AS OBJECT (id int, fecha_solicitud date, fecha_devolucion date, estado int, id_usuario int, id_libro int);

/
--------------------------------------------------------
--  DDL for Type RESEÑA_T
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TYPE "BIBLIOTECA"."RESEÑA_T" AS OBJECT (id int, texto varchar(1000), calificacion int , fecha date, usuario REF Usuario_t,libro REF Libro_t);

/
--------------------------------------------------------
--  DDL for Type USUARIO_T
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TYPE "BIBLIOTECA"."USUARIO_T" AS OBJECT (id int,nombre VARCHAR(50), correo VARCHAR(100), contraseña varchar(70));

/
--------------------------------------------------------
--  DDL for Table LIBROS
--------------------------------------------------------

CREATE TABLE "BIBLIOTECA"."LIBROS" OF "BIBLIOTECA"."LIBRO_T"
    OIDINDEX  ( PCTFREE 10 INITRANS 2 MAXTRANS 255
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" )
 SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"
 NESTED TABLE "CATEGORIAS" STORE AS "CATEGORIAS_TABLA"
 (PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
 NOCOMPRESS
  TABLESPACE "USERS" ) RETURN AS VALUE;
--------------------------------------------------------
--  DDL for Table PRESTAMOS
--------------------------------------------------------

CREATE TABLE "BIBLIOTECA"."PRESTAMOS" OF "BIBLIOTECA"."PRESTAMO_T"
    OIDINDEX  ( PCTFREE 10 INITRANS 2 MAXTRANS 255
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" )
 SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table RESEÑAS
--------------------------------------------------------

CREATE TABLE "BIBLIOTECA"."RESEÑAS" OF "BIBLIOTECA"."RESEÑA_T"
    OIDINDEX  ( PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS" )
 SEGMENT CREATION DEFERRED
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table USUARIO
--------------------------------------------------------

CREATE TABLE "BIBLIOTECA"."USUARIO"
(	"ID" NUMBER(10,0),
     "CONTRASEÑA" VARCHAR2(255 CHAR),
     "CORREO" VARCHAR2(255 CHAR),
     "NOMBRE" VARCHAR2(255 CHAR)
) SEGMENT CREATION DEFERRED
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table USUARIOS
--------------------------------------------------------

CREATE TABLE "BIBLIOTECA"."USUARIOS" OF "BIBLIOTECA"."USUARIO_T"
    OIDINDEX  ( PCTFREE 10 INITRANS 2 MAXTRANS 255
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" )
 SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for View TODOSLIBROS
--------------------------------------------------------

CREATE OR REPLACE FORCE EDITIONABLE VIEW "BIBLIOTECA"."TODOSLIBROS" ("ID", "TITULO", "AUTOR", "EDITORIAL", "NUM_COPIAS", "SINOPSIS", "CATEGORIAS") AS
SELECT l.id, l.titulo, l.autor, l.editorial, l.num_copias, l.sinopsis,
       LISTAGG(c.nombre, ', ') AS categorias
FROM Libros l,
     TABLE(l.categorias) c
GROUP BY l.id, l.titulo, l.autor, l.editorial, l.num_copias, l.sinopsis
;
REM INSERTING into BIBLIOTECA.LIBROS
SET DEFINE OFF;
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (324432,'Otra vez una prueba de mongo','Miyu','prueba',12,'Prueba de mongoo',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Educativo')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (312564,'prueba123','miyu','dhejw',5,'fhjgdshj',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Alegre')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (21321312,'miyu','coco','coco',22,'oeowiewoiew',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Educativo')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (31232121,'Punpun','inio asano','miyu',19,'Punpun',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Recuentos de la vida'), BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Triste')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (432432,'3782','312321','312321',312312,'312312',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Alegre')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (31829313,'Haku no hana','Shuzo Oshimi','Kamite',20,'El tomo trata sobre la historia de Kasuga y Nakamura, quienes intentan poner en marcha su plan en la noche del festival de verano. El plan fracasa en el último momento, y el tiempo pasa, llevando a Kasuga a una nueva ciudad y a nuevas amistades. Sin embargo, Kasuga se siente como un cascarón vacío, persiguiendo la sombra de Nakamura, quien ha cortado contacto con él. ',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Thriller psicológico')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (103,'Oracle editado','Miyu Maldonado','Coco',5,'Este libro es una introduccion a Oracle',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Educativo')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (6666,'Coco prueba mongo','hiram','uv',1,'Esto es una prueba para ver si se guarda la ruta de la imagen en mongo',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Drama')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (101,'Miyu el huerfanito','Coco UV','SEV',5,'Una novela dramatica que narra la vida de miyu',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Novela')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (102,'Introduccion a base de datos','Edgar','UV',3,'Introduccion a las bases de datos objeto relacional.',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Alegre'), BIBLIOTECA.CATEGORIA_T('Educativo')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (978142158,'Goodnight Punpun','Inio Asano','Viz Media',10,'Punpun mira el mundo con los ojos de un pollito, porque tal vez así puede soportar que su amado padre sea un alcohólico violento; su madre, una víctima golpeada; y el amor de su vida, una compañera de clase que se marcha lejos',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Recuentos de la vida')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (6075509,'La hipótesis del amor','Ali Hazelwood','Alianza de Novela',4,'La hipótesis del amor sigue a Olive, personaje principal, en su intento de convencer a su mejor amiga, Anh, de que piense que su vida amorosa va muy bien. Para convencer a Anh, finge salir con su profesor, Adam. Olive y Adam intentan convencer a todos los que les rodean de que están enamorados. Pero mientras convencen a todos, olvidan que se supone que sus sentimientos son falsos. ¿Cuánto tiempo les tomará a Olive y Adam darse cuenta de que ya no quieren fingir?',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Romance')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (3165267,'Miyucoco','Coco','Miyuki',4,'Blanca',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Novela'), BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Coco')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (21321,'Prueba 55','Coco','miyu',444,'prueba de subir libro',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Educativo'), BIBLIOTECA.CATEGORIA_T('Alegre')));
Insert into BIBLIOTECA.LIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (432804,'Rastros de sangre','Shouzo Oshimi','Kamite',19,'Seiichi es un estudiante de la escuela primaria, es un poco inseguro y tímido, con pocos amigos. Su madre es muy sobreprotectora con él, Seiichi se siente a gusto únicamente con ella a su lado pues lo consiente y trata con mucho cariño, incluso no le gusta estar con otros familiares. La madre poco a poco siente que su hijo se aleja de ella. Familia y amigos se relacionan con Seiichi pero la madre siente un fuerte deseo protector e intenta alejarlo de esas influencias, tomando acciones amenazantes para las otras personas, sean su propio esposo, familia y amigos de su hijo.',BIBLIOTECA.CATEGORIA_TAB_T(BIBLIOTECA.CATEGORIA_T('Drama'), BIBLIOTECA.CATEGORIA_T('Thriller psicológico'), BIBLIOTECA.CATEGORIA_T('Seinen')));
REM INSERTING into BIBLIOTECA.PRESTAMOS
SET DEFINE OFF;
Insert into BIBLIOTECA.PRESTAMOS (ID,FECHA_SOLICITUD,FECHA_DEVOLUCION,ESTADO,ID_USUARIO,ID_LIBRO) values (19223,to_date('02/06/25','DD/MM/RR'),to_date('11/06/25','DD/MM/RR'),1,41644,6075509);
Insert into BIBLIOTECA.PRESTAMOS (ID,FECHA_SOLICITUD,FECHA_DEVOLUCION,ESTADO,ID_USUARIO,ID_LIBRO) values (41938,to_date('03/06/25','DD/MM/RR'),to_date('18/06/25','DD/MM/RR'),0,41644,432804);
Insert into BIBLIOTECA.PRESTAMOS (ID,FECHA_SOLICITUD,FECHA_DEVOLUCION,ESTADO,ID_USUARIO,ID_LIBRO) values (24262,to_date('02/06/25','DD/MM/RR'),to_date('11/06/25','DD/MM/RR'),0,41644,31232121);
Insert into BIBLIOTECA.PRESTAMOS (ID,FECHA_SOLICITUD,FECHA_DEVOLUCION,ESTADO,ID_USUARIO,ID_LIBRO) values (17553,to_date('03/06/25','DD/MM/RR'),to_date('19/06/25','DD/MM/RR'),1,41644,31232121);
REM INSERTING into BIBLIOTECA."RESEÑAS"
SET DEFINE OFF;
REM INSERTING into BIBLIOTECA.USUARIO
SET DEFINE OFF;
REM INSERTING into BIBLIOTECA.USUARIOS
SET DEFINE OFF;
Insert into BIBLIOTECA.USUARIOS (ID,NOMBRE,CORREO,"CONTRASEÑA") values (41644,'Coco','miyu@gmail.com','123');
Insert into BIBLIOTECA.USUARIOS (ID,NOMBRE,CORREO,"CONTRASEÑA") values (64745,'Blanca','123@gmail.com','123');
Insert into BIBLIOTECA.USUARIOS (ID,NOMBRE,CORREO,"CONTRASEÑA") values (34480,'Jazmín','miyu123@gmail.com','123');
Insert into BIBLIOTECA.USUARIOS (ID,NOMBRE,CORREO,"CONTRASEÑA") values (1,'Miyu Maldonado','prueba@gmail.com','contra123');
Insert into BIBLIOTECA.USUARIOS (ID,NOMBRE,CORREO,"CONTRASEÑA") values (55580,'Hiram','manzanita77723@gmail.com','123');
REM INSERTING into BIBLIOTECA.TODOSLIBROS
SET DEFINE OFF;
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (21321,'Prueba 55','Coco','miyu',444,'prueba de subir libro','Educativo, Alegre');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (31232121,'Punpun','inio asano','miyu',19,'Punpun','Recuentos de la vida, Drama, Triste');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (102,'Introduccion a base de datos','Edgar','UV',3,'Introduccion a las bases de datos objeto relacional.','Alegre, Educativo');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (324432,'Otra vez una prueba de mongo','Miyu','prueba',12,'Prueba de mongoo','Educativo');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (432432,'3782','312321','312321',312312,'312312','Alegre');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (432804,'Rastros de sangre','Shouzo Oshimi','Kamite',19,'Seiichi es un estudiante de la escuela primaria, es un poco inseguro y tímido, con pocos amigos. Su madre es muy sobreprotectora con él, Seiichi se siente a gusto únicamente con ella a su lado pues lo consiente y trata con mucho cariño, incluso no le gusta estar con otros familiares. La madre poco a poco siente que su hijo se aleja de ella. Familia y amigos se relacionan con Seiichi pero la madre siente un fuerte deseo protector e intenta alejarlo de esas influencias, tomando acciones amenazantes para las otras personas, sean su propio esposo, familia y amigos de su hijo.','Drama, Thriller psicológico, Seinen');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (21321312,'miyu','coco','coco',22,'oeowiewoiew','Educativo');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (6666,'Coco prueba mongo','hiram','uv',1,'Esto es una prueba para ver si se guarda la ruta de la imagen en mongo','Drama');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (101,'Miyu el huerfanito','Coco UV','SEV',5,'Una novela dramatica que narra la vida de miyu','Drama, Novela');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (312564,'prueba123','miyu','dhejw',5,'fhjgdshj','Alegre');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (978142158,'Goodnight Punpun','Inio Asano','Viz Media',10,'Punpun mira el mundo con los ojos de un pollito, porque tal vez así puede soportar que su amado padre sea un alcohólico violento; su madre, una víctima golpeada; y el amor de su vida, una compañera de clase que se marcha lejos','Drama, Recuentos de la vida');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (3165267,'Miyucoco','Coco','Miyuki',4,'Blanca','Novela, Drama, Coco');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (31829313,'Haku no hana','Shuzo Oshimi','Kamite',20,'El tomo trata sobre la historia de Kasuga y Nakamura, quienes intentan poner en marcha su plan en la noche del festival de verano. El plan fracasa en el último momento, y el tiempo pasa, llevando a Kasuga a una nueva ciudad y a nuevas amistades. Sin embargo, Kasuga se siente como un cascarón vacío, persiguiendo la sombra de Nakamura, quien ha cortado contacto con él. ','Drama, Thriller psicológico');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (6075509,'La hipótesis del amor','Ali Hazelwood','Alianza de Novela',4,'La hipótesis del amor sigue a Olive, personaje principal, en su intento de convencer a su mejor amiga, Anh, de que piense que su vida amorosa va muy bien. Para convencer a Anh, finge salir con su profesor, Adam. Olive y Adam intentan convencer a todos los que les rodean de que están enamorados. Pero mientras convencen a todos, olvidan que se supone que sus sentimientos son falsos. ¿Cuánto tiempo les tomará a Olive y Adam darse cuenta de que ya no quieren fingir?','Romance');
Insert into BIBLIOTECA.TODOSLIBROS (ID,TITULO,AUTOR,EDITORIAL,NUM_COPIAS,SINOPSIS,CATEGORIAS) values (103,'Oracle editado','Miyu Maldonado','Coco',5,'Este libro es una introduccion a Oracle','Drama, Educativo');
--------------------------------------------------------
--  DDL for Index SYS_C008437
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008437" ON "BIBLIOTECA"."USUARIO" ("ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008439
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008439" ON "BIBLIOTECA"."USUARIOS" ("ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008440
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008440" ON "BIBLIOTECA"."USUARIOS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008441
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008441" ON "BIBLIOTECA"."LIBROS" ("ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008442
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008442" ON "BIBLIOTECA"."LIBROS" ("SYS_NC0000900010$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008443
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008443" ON "BIBLIOTECA"."LIBROS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008444
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008444" ON "BIBLIOTECA"."RESEÑAS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008452
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008452" ON "BIBLIOTECA"."PRESTAMOS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_FK0000071534N00009$
--------------------------------------------------------

CREATE INDEX "BIBLIOTECA"."SYS_FK0000071534N00009$" ON "BIBLIOTECA"."CATEGORIAS_TABLA" ("NESTED_TABLE_ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_FK0000071534N00009$
--------------------------------------------------------

CREATE INDEX "BIBLIOTECA"."SYS_FK0000071534N00009$" ON "BIBLIOTECA"."CATEGORIAS_TABLA" ("NESTED_TABLE_ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008441
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008441" ON "BIBLIOTECA"."LIBROS" ("ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008442
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008442" ON "BIBLIOTECA"."LIBROS" ("SYS_NC0000900010$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008443
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008443" ON "BIBLIOTECA"."LIBROS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008452
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008452" ON "BIBLIOTECA"."PRESTAMOS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008444
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008444" ON "BIBLIOTECA"."RESEÑAS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008437
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008437" ON "BIBLIOTECA"."USUARIO" ("ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008440
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008440" ON "BIBLIOTECA"."USUARIOS" ("SYS_NC_OID$")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index SYS_C008439
--------------------------------------------------------

CREATE UNIQUE INDEX "BIBLIOTECA"."SYS_C008439" ON "BIBLIOTECA"."USUARIOS" ("ID")
    PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Procedure OBTENER_CATEGORIAS_LIBRO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "BIBLIOTECA"."OBTENER_CATEGORIAS_LIBRO" (
  p_libro_id IN NUMBER,
  p_categorias OUT Categoria_tab_t
) AS
BEGIN
SELECT c.nombre
           BULK COLLECT INTO p_categorias
FROM Libros l,
     TABLE(l.categorias) c
WHERE l.id = p_libro_id;
END OBTENER_CATEGORIAS_LIBRO;

/
--------------------------------------------------------
--  Constraints for Table LIBROS
--------------------------------------------------------

ALTER TABLE "BIBLIOTECA"."LIBROS" ADD PRIMARY KEY ("ID")
    USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
ALTER TABLE "BIBLIOTECA"."LIBROS" ADD UNIQUE ("CATEGORIAS") RELY
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
ALTER TABLE "BIBLIOTECA"."LIBROS" ADD UNIQUE ("SYS_NC_OID$") RELY
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table PRESTAMOS
--------------------------------------------------------

ALTER TABLE "BIBLIOTECA"."PRESTAMOS" ADD UNIQUE ("SYS_NC_OID$") RELY
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table RESEÑAS
--------------------------------------------------------

ALTER TABLE "BIBLIOTECA"."RESEÑAS" ADD UNIQUE ("SYS_NC_OID$") RELY
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table USUARIO
--------------------------------------------------------

ALTER TABLE "BIBLIOTECA"."USUARIO" MODIFY ("ID" NOT NULL ENABLE);
ALTER TABLE "BIBLIOTECA"."USUARIO" ADD PRIMARY KEY ("ID")
    USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "USERS"  ENABLE;
--------------------------------------------------------
--  Constraints for Table USUARIOS
--------------------------------------------------------

ALTER TABLE "BIBLIOTECA"."USUARIOS" ADD PRIMARY KEY ("ID")
    USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
ALTER TABLE "BIBLIOTECA"."USUARIOS" ADD UNIQUE ("SYS_NC_OID$") RELY
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;

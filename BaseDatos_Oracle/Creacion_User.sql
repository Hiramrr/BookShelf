CREATE USER biblioteca IDENTIFIED BY biblioteca123;
GRANT CONNECT, RESOURCE TO biblioteca;


ALTER USER biblioteca DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS;

SELECT * FROM all_tab_privs WHERE grantee = 'biblioteca';

SELECT table_name FROM user_tables;


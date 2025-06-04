package BaseDatosOracle;

import controllers.Categoria;
import controllers.Libro;
import controllers.Prestamo;
import controllers.Usuario;
import BaseDatosMongo.BaseDatosMongo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosOracle {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
    private static final String USUARIO = "biblioteca";
    private static final String CONTRASEÑA = "biblioteca123";

    private static Connection con;
    private BaseDatosMongo conexionMongo;


    public Connection conectar() {
        try {
            con = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión exitosa a Oracle");
        } catch (SQLException e) {
            System.err.println("Error al conectar con Oracle: " + e.getMessage());
        }
        return con;
    }

    public Usuario consultarUsuario(String correo, String contraseña) {
        String query = "SELECT * FROM USUARIOS WHERE CORREO = ? AND CONTRASEÑA = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, correo);
            statement.setString(2, contraseña);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getInt("ID"),
                            resultSet.getString("NOMBRE"),
                            resultSet.getString("CORREO"),
                            resultSet.getString("CONTRASEÑA")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage());
        }
        return null;
    }

    public boolean registroUsuario(int id, String nombre, String correo, String contraseña) {
        String query = "INSERT INTO Usuarios Values (Usuario_t(?,?,?,?))";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, nombre);
            statement.setString(3, correo);
            statement.setString(4, contraseña);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Algo salio mal nooo");
        }
        return false;
    }

    public List<Libro> obtener_libros() {
        List<Libro> libros = new ArrayList<Libro>();

        String query = "SELECT * FROM TodosLibros";

        if (conexionMongo == null) {
            conexionMongo = new BaseDatosMongo();
        }

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                List<Categoria> categorias = new ArrayList<>();
                int ID = rs.getInt("ID");
                String TITULO = rs.getString("TITULO");
                String AUTOR = rs.getString("AUTOR");
                String EDITORIAL = rs.getString("EDITORIAL");
                int NUM_COPIAS = rs.getInt("NUM_COPIAS");
                String SINOPSIS = rs.getString("SINOPSIS");
                String TEMP = rs.getString("CATEGORIAS");
                String[] categoriasArray = TEMP.split(",");
                categorias.clear();
                for (String categoria : categoriasArray) {
                    categorias.add(new Categoria(categoria.trim()));
                }

                Libro libro = new Libro(ID, TITULO, AUTOR, EDITORIAL, NUM_COPIAS, SINOPSIS, categorias);
                String urlImagen = conexionMongo.obtenerUrlImagen("imagenes-libro", ID);
                libro.setUrlImagen(urlImagen);
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public ObservableList<Categoria> obtenerCategorias() {
        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT c.nombre FROM Libros l, TABLE(l.categorias) c";


        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Categoria categoria = new Categoria(rs.getString("NOMBRE"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public boolean subirLibro(int id, String titulo, String autor, String editorial, int numCopias, String sinopsis, String categoriasSQL) {
        String query = "INSERT INTO Libros VALUES (Libro_t(?, ?, ?, ?, ?, ?, " + categoriasSQL + "))";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, titulo);
            stmt.setString(3, autor);
            stmt.setString(4, editorial);
            stmt.setInt(5, numCopias);
            stmt.setString(6, sinopsis);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al subir el libro: " + e.getMessage());
            return false;
        }
    }

    public String obtenerNombreUsuario(int idUsuario) {
        String query = "SELECT NOMBRE FROM USUARIOS WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("NOMBRE");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre de usuario: " + e.getMessage());
        }
        return null;
    }

    public Libro obtenerLibroPorId(int idLibro) {
        String query = "SELECT * FROM TodosLibros WHERE ID = ?";
        if (conexionMongo == null) {
            conexionMongo = new BaseDatosMongo();
        }

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Categoria> categorias = new ArrayList<>();
                    int ID = rs.getInt("ID");
                    String TITULO = rs.getString("TITULO");
                    String AUTOR = rs.getString("AUTOR");
                    String EDITORIAL = rs.getString("EDITORIAL");
                    int NUM_COPIAS = rs.getInt("NUM_COPIAS");
                    String SINOPSIS = rs.getString("SINOPSIS");
                    String TEMP = rs.getString("CATEGORIAS");
                    String[] categoriasArray = TEMP.split(",");
                    categorias.clear();
                    for (String categoria : categoriasArray) {
                        categorias.add(new Categoria(categoria.trim()));
                    }

                    Libro libro = new Libro(ID, TITULO, AUTOR, EDITORIAL, NUM_COPIAS, SINOPSIS, categorias);
                    String urlImagen = conexionMongo.obtenerUrlImagen("imagenes-libro", ID);
                    libro.setUrlImagen(urlImagen);
                    return libro;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean editarLibro(int id, String titulo, String autor, String editorial, int numCopias, String sinopsis, String categoriaAntigua, String categoriaNueva) {
        String queryLibro = "UPDATE Libros SET TITULO = ?, AUTOR = ?, EDITORIAL = ?, NUM_COPIAS = ?, SINOPSIS = ? WHERE ID = ?";

        String queryCategoria = "UPDATE TABLE (SELECT CATEGORIAS FROM Libros WHERE ID = ?) c SET c.COLUMN_VALUE = ? WHERE c.COLUMN_VALUE = ?";

        try {
            try (PreparedStatement stmtLibro = con.prepareStatement(queryLibro)) {
                stmtLibro.setString(1, titulo);
                stmtLibro.setString(2, autor);
                stmtLibro.setString(3, editorial);
                stmtLibro.setInt(4, numCopias);
                stmtLibro.setString(5, sinopsis);
                stmtLibro.setInt(6, id);
                stmtLibro.executeUpdate();
            }

            if (categoriaAntigua != null && categoriaNueva != null && !categoriaAntigua.isEmpty() && !categoriaNueva.isEmpty()) {
                try (PreparedStatement stmtCategoria = con.prepareStatement(queryCategoria)) {
                    stmtCategoria.setInt(1, id);
                    stmtCategoria.setString(2, categoriaNueva);
                    stmtCategoria.setString(3, categoriaAntigua);
                    stmtCategoria.executeUpdate();
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarNombreUsuario(int id, String nombre) {
        String query = "UPDATE USUARIOS SET NOMBRE = ? WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el nombre de usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean solicitarPrestamo(int id, Date fecha_Solicitud, Date fecha_devolucion, int id_usuario, int id_libro) {
        String query = "INSERT INTO Prestamos VALUES (Prestamo_t(?, ?, ?, 0, ?, ?))";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setDate(2, fecha_Solicitud);
            stmt.setDate(3, fecha_devolucion);
            stmt.setInt(4, id_usuario);
            stmt.setInt(5, id_libro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al solicitar el prestamo: " + e.getMessage());
            return false;
        }
    }

    public boolean restarLibro(int id_libro){
        String query = "UPDATE LIBROS SET NUM_COPIAS = NUM_COPIAS - 1 WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id_libro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la cantidad del libro: " + e.getMessage());
            return false;
        }
    }

    public List<Prestamo> obtenerPrestamos(int id_usuario) {
        List<Prestamo> prestamos = new ArrayList<Prestamo>();
        String query = "SELECT * FROM PRESTAMOS WHERE id_usuario = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id_usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String fecha_solicitud = String.valueOf(rs.getDate("FECHA_SOLICITUD"));
                    String fecha_devolucion = String.valueOf(rs.getDate("FECHA_DEVOLUCION"));
                    int estado = rs.getInt("ESTADO");
                    int id_user = rs.getInt("ID_USUARIO");
                    int id_libro = rs.getInt("ID_LIBRO");

                    Prestamo prestamo = new Prestamo(id,fecha_solicitud,fecha_devolucion,estado,id_user,id_libro);
                    prestamos.add(prestamo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return prestamos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean entregarPrestamo(int idPrestamo){
        String query = "UPDATE PRESTAMOS SET ESTADO = 1 WHERE ID = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, idPrestamo);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Algo salio mal nooo");
        }
        return false;
    }

    public boolean sumarLibro(int id_libro){
        String query = "UPDATE LIBROS SET NUM_COPIAS = NUM_COPIAS + 1 WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id_libro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la cantidad del libro: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM USUARIOS";
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                usuarios.add(new Usuario(
                    resultSet.getInt("ID"),
                    resultSet.getString("NOMBRE"),
                    resultSet.getString("CORREO"),
                    resultSet.getString("CONTRASEÑA")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public boolean eliminarLibro(int idLibro) {
        String query = "DELETE FROM Libros WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idLibro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el libro: " + e.getMessage());
            return false;
        }
    }

}

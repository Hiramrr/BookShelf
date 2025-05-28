package BaseDatosOracle;

import controllers.Categoria;
import controllers.Libro;
import controllers.Usuario;
import BaseDatosMongo.BaseDatosMongo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
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

    public boolean registroUsuario(int id, String nombre, String correo, String contraseña){
        String query = "INSERT INTO Usuarios Values (Usuario_t(?,?,?,?))";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, nombre);
            statement.setString(3, correo);
            statement.setString(4, contraseña);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e){
            System.out.println("Algo salio mal nooo");
        }
       return false;
    }

    public List<Libro> obtener_libros(){
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

    public ObservableList<Categoria> obtenerCategorias(){
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
}

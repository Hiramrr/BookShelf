package BaseDatosOracle;

import java.sql.*;

public class BaseDatosOracle {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
    private static final String USUARIO = "biblioteca";
    private static final String CONTRASEÑA = "biblioteca123";

    private static Connection con;


    public Connection conectar() {
        try {
            con = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión exitosa a Oracle");
        } catch (SQLException e) {
            System.err.println("Error al conectar con Oracle: " + e.getMessage());
        }
        return con;
    }

    public boolean consultarUsuario(String correo, String contraseña) {
        String query = "SELECT CORREO, CONTRASEÑA FROM USUARIOS WHERE CORREO = ? AND CONTRASEÑA = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, correo);
            statement.setString(2, contraseña);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage());
        }
        return false;
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
}

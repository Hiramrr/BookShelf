package BaseDatosMongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class BaseDatosMongo {

    private MongoDatabase database;
    private MongoClient client;

    public BaseDatosMongo() {
        try {
            this.client = MongoClients.create("mongodb://localhost:27017");
            this.database = client.getDatabase("BookShelf");
        } catch (Exception e) {
            System.err.println("Error al conectar con MongoDB: " + e.getMessage());
        }
    }

    public boolean guardarDireccionImagen(String coleccion, String url, int id) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            Document doc = new Document("url", url).append("id_libro", id);
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar imagen en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String obtenerUrlImagen(String coleccion, int idLibro) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            Document doc = collection.find(eq("id_libro", idLibro)).first();
            return doc != null ? doc.getString("url") : "images/libros/default.png";
        } catch (Exception e) {
            System.err.println("Error al obtener URL de imagen: " + e.getMessage());
            return "images/libros/default.png";
        }
    }

    public void cerrarConexion() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar la conexión con MongoDB: " + e.getMessage());
            }
        }
    }

}


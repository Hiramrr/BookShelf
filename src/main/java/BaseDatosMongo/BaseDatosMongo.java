package BaseDatosMongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            System.err.println("Error al guardar imagen" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarrDireccionImagen(String coleccion, String url, int id) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            collection.updateOne(eq("id_libro", id), new Document("$set", new Document("url", url)));
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar imagen" + e.getMessage());
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
            System.err.println("no tiene imagen xd" + e.getMessage());
            return "images/libros/default.png";
        }
    }

    public boolean guardarReseña(String coleccion, int id_usuario,int id_libro, String estrellas, String reseña) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            Document doc = new Document("Estrellas", estrellas).append("Reseña", reseña).append("id_usuario", id_usuario).append("id_libro", id_libro);
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar reseña" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Document> obtenerReseñas(String coleccion, int idLibro) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            List<Document> listaReseñas = collection.find(eq("id_libro", idLibro)).into(new ArrayList<>());
            return FXCollections.observableArrayList(listaReseñas);
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

   public ObservableList<Document> obtenerReseñasRecientes(String coleccion) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            List<Document> listaReseñas = collection.find().sort(descending("_id")).into(new ArrayList<>());
            return FXCollections.observableArrayList(listaReseñas);
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    public ObservableList<Document> obtenerReseñaUser(String coleccion, int id_user) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            List<Document> listaReseñas = collection.find(eq("id_usuario", id_user)).into(new ArrayList<>());
            return FXCollections.observableArrayList(listaReseñas);
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    public boolean guardarAboutMe(String coleccion, String aboutMe, String libro, int id_user, int id_libro) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            
            Document existente = collection.find(eq("id_user", id_user)).first();
            
            if (existente != null) {
                collection.updateOne(eq("id_user", id_user), new Document("$set", new Document("aboutMe", aboutMe).append("libro_favorito", libro).append("id_libro", id_libro)));
            } else {
                Document doc = new Document("aboutMe", aboutMe).append("libro_favorito", libro).append("id_user", id_user).append("id_libro", id_libro);
                collection.insertOne(doc);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar informacion noooo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Document obtenerAboutMe(String coleccion, int id_user) {
        try {
            MongoCollection<Document> collection = database.getCollection(coleccion);
            Document doc = collection.find(eq("id_user", id_user)).first();
            return doc;
        } catch (Exception e) {
            System.err.println("Error al obtener informacion del usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
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


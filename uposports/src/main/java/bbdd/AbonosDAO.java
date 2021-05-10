package bbdd;

import clases.Abono;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

public class AbonosDAO {
    
    private static DBCollection abonoInit() throws UnknownHostException{
                     // Conectar al servidor MongoDB
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // Conectar a la base de datos
            DB db = mongoClient.getDB("uposports");

            //Acceder coleccion "Abonos"*/
            DBCollection collection = db.getCollection("abonos");
            
            return collection;
    }
        
    //Método para añadir un documento nuevo a la colección AbonosDAO
    public static void insertarAbono(Abono abono) throws UnknownHostException {

        DBCollection collection = abonoInit();
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 3 atributos del nuevo documento Abono
        document.append("tipo", abono.getTipo());
        document.append("precio", abono.getCoste());
        document.append("duracion", abono.getDuracion());
        //Insertamos el documento en la colección AbonosDAO
        collection.insert(document);
        System.out.println("Documento Abono insertado: " + document + "\n");
    }

    //Método para mostrar todos los documentos de la colección AbonosDAO
    public static void mostrarAbonos(DBCollection collection) {
        DBCursor cursor = collection.find();// Obtenemos todos los documentos de la coleccion AbonosDAO
        DBObject elemento;
        //Recorrido de todos los elementos de la coleccion AbonosDAO
        System.out.println("Recorriendo la colección Abonos:");
        int i = 0;//Variable iteradora
        while (cursor.hasNext()) {//Mientras haya documentos
            i++;//Incrementamos la variable
            elemento = cursor.next();//Guardamos el documento en "elemento"
            System.out.println(String.format("Documento Abono_%d leido: %s", i, elemento));//Se muestra por pantalla el documento
        }
        System.out.println("Fin de la colección Abonos\n");

    }

    //Método para actualizar un campo de tipo String de la colección AbonosDAO
    public static void actualizarAbono(DBCollection collection, DBObject abono, String campo, String nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();//Instanciamos un nuevo documento
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));//Actualizamos el campo con el nuevoValor (ambos atributos se reciben por parámetro)
        collection.update(abono, newDocument);//Actualizamos el documento "abono" recibido por parámetro con el nuevo campo del documento creado
        System.out.println("Documento Abono actualizado correctamente\n");
    }

    //Método para actualizar un campo de tipo int de la colección AbonosDAO (método sobrecargado)
    public static void actualizarAbono(DBCollection collection, DBObject abono, String campo, int nuevoValor) {
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append(campo, nuevoValor));
        collection.update(abono, newDocument);
        System.out.println("Documento Abono actualizado correctamente\n");
    }

    //Método para eliminar un documento de la colección AbonosDAO
    public static void eliminarAbono(DBCollection collection, String tipo) {
        System.out.println(String.format("Buscando documento Abono tipo %s para eliminar...", tipo));
        collection.remove(buscarAbono(collection, tipo));//Elimina el documento que recibe del método buscarAbono, pasándole la colección y el tipo de Abono.
        System.out.println("Documento Abono eliminado\n");
    }

    //Método para buscar un documento abono en la colección AbonosDAO
    public static DBObject buscarAbono(DBCollection collection, String tipo) {
        BasicDBObject searchQuery = new BasicDBObject().append("tipo", tipo);//Creamos la query que será los documentos que contengan como atributo "tipo" el que recibe como parámetro el método
        DBCursor cursor = collection.find(searchQuery);//Los elementos que cumplan la condicion de searchQuery se introducen en cursor
        DBObject elemento = cursor.next();//Solamente debemos tener uno, ya que le pasamos el tipo que es nuestro ID.
        System.out.println(String.format("Documento Abono encontrado: %s", elemento));
        return elemento;//Devolvemos el documento que será útil para otras operaciones CRUD
    }
}
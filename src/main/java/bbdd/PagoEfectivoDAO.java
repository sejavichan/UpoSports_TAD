package bbdd;

import clases.Pago;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

public class PagoEfectivoDAO {

    private static DBCollection pagosInit() throws UnknownHostException {
        // Conectar al servidor MongoDB
        MongoClient mongoClient = new MongoClient("mongodb", 27017);

        // Conectar a la base de datos
        DB db = mongoClient.getDB("uposports");

        //Acceder coleccion "Abonos"*/
        DBCollection collection = db.getCollection("PagosEfectivo");

        return collection;
    }

    public static void insertarPago(Pago pago) throws UnknownHostException {

        DBCollection collection = pagosInit();
        BasicDBObject document = new BasicDBObject();//Instanciamos el nuevo documento
        //Insertamos los 2 atributos del nuevo documento PagoEfectivo
        document.append("fechaHora", pago.getFecha());
        document.append("cantidad", pago.getCantidad());
        //Insertamos el documento en la colección PagoEfectivo
        collection.insert(document);
        System.out.println("Documento Pago Efectivo insertado: " + document + "\n");
    }

}

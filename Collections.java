import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


public class Collections {
	final static String mongoDBURL = "None";
	final static String mongoDBName = "Transactions";
	static MongoClient client = getClient(mongoDBURL);
	static MongoDatabase db = client.getDatabase(mongoDBName);
	
	public static void main(String[] args) throws Exception {
	
	//create collections for each table in the schema
	db.createCollection("User");
	db.createCollection("Product");
	db.createCollection("Purchase");
	db.createCollection("Review");
	}
	
	private static MongoClient getClient(String mongoDBURL) {
		MongoClient client = null;
		if (mongoDBURL.equals("None"))
			client = new MongoClient();
		else
			client = new MongoClient(new MongoClientURI(mongoDBURL));
		return client;
	}
}
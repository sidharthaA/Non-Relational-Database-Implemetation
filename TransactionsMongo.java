import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.types.Decimal128;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

public class TransactionsMongo {
	final static String mongoDBURL = "None";
	final static String mongoDBName = "Transactions";
	static MongoClient client = getClient(mongoDBURL);
	static MongoDatabase db = client.getDatabase(mongoDBName);
	static MongoCollection<Document> user = db.getCollection("User");
	static MongoCollection<Document> product = db.getCollection("Product");
	static MongoCollection<Document> purchase = db.getCollection("Purchase");
	static MongoCollection<Document> review = db.getCollection("Review");
	static int countNegative = 0;
	
	
	public static void main(String[] args) throws Exception {
		
		CreateAccount("userA", "passwordA", "firstNameA", "lastNameA");
		CreateAccount("userB", "passwordB", "firstNameB", "lastNameB");
		CreateAccount("userC", "passwordC", "firstNameC", "lastNameC");
		AddProduct("prodA", "descriptionA", 111, 11);
		AddProduct("prodB", "descriptionB", 222, 12);
		AddProduct("prodC", "descriptionC", 333, 13);
		PostReview("userA", "passwordA", 1, 1, "reviewText1");
		PostReview("userA", "passwordA", 1, 3, "reviewText2");
		PostReview("userB", "passwordB", 2, 2, "reviewText2");
		GetProductAndReviews(2);
		GetAverageUserRating("userA");
		UpdateStockLevel(2, 7);
		db.getCollection("User").drop();
		db.getCollection("Product").drop();
		db.getCollection("Purchase").drop();
		db.getCollection("Review").drop();
		Populate(); 

	}

	private static MongoClient getClient(String mongoDBURL) {
		MongoClient client = null;
		if (mongoDBURL.equals("None"))
			client = new MongoClient();
		else
			client = new MongoClient(new MongoClientURI(mongoDBURL));
		return client;
	}
	
	public static void Populate() {
		
//        String query = "DELETE FROM user";
//        stmt.executeUpdate(query);
//        query = "DELETE FROM product";
//        stmt.executeUpdate(query);
//        query = "DELETE FROM review";
//        stmt.executeUpdate(query);
//        query = "DELETE FROM purchase";
//        stmt.executeUpdate(query);
	        
		//creating users
        for(int i = 1; i <=1000; i++) {
        	CreateAccount("user" + i, "pwd" + i, "firstName" + i, "lastName" + i);
        }
        
        // Creating products
        for(int i = 1; i <=10000; i++) {
        	Random randStock = new Random();
        	Random randPrice = new Random();
            int ubStock = 101;
            int ubPrice = 1001;
            int stock = randStock.nextInt(ubStock) + 1;
            int price = randPrice.nextInt(ubPrice) + 1;
        	AddProduct("prod" + i, "desc" + i, price, stock);
        }
        
        // Creating reviews
        for(int i = 1; i <=20000; i++) {
        	Random randUser = new Random();
        	Random randProdID = new Random();
        	Random randRating = new Random();
            int ubUser = 1001;
            int ubProdID = 10001;
            int ubRating = 10;
            int j = randUser.nextInt(ubUser) + 1;
            int prodID = randProdID.nextInt(ubProdID) + 1;
            int rating = randRating.nextInt(ubRating) + 1;
            try {
            PostReview("user" + j, "pwd" + j, prodID, rating, "reviewText" + i);
            }
            catch(Exception e) {
            	
            }
        }
        
        //Submit order
        for(int i = 1; i <=10000; i++) {
        	Random randUser = new Random();
        	Random randProdID = new Random();
	    	int ubUser = 1001;
	        int ubProdID = 10001;
        	int j = randUser.nextInt(ubUser) + 1;
        	Random randQuantity = new Random();
        	Random randMonth = new Random();
        	Random randDay = new Random();
        	int month = randMonth.nextInt(12) + 1;
        	int day = randDay.nextInt(28) + 1;
        	String date = 2020 + "-" + month + "-" + day;
        	HashMap<Integer, Integer> listOfProductsAndQuantities = new HashMap<Integer, Integer>();
        	for(int x = 1; x <= 10; x++) {
        		int prodID = randProdID.nextInt(ubProdID) + 1;
        		int quantity = randQuantity.nextInt(20) + 1;
        		listOfProductsAndQuantities.put(prodID, quantity);
        	}
	        SubmitOrder(date, "user" + j, "pwd" + j, listOfProductsAndQuantities);
        }
        System.out.println("populated!");
	}

	//CreateAccount
		public static void CreateAccount (String username, String password, String firstName, String lastName) {
//			System.out.println(CreateAccount);
			
			//Preparing a document
		      Document document = new Document();
		      document.append("User_Name", username);
		      document.append("Password", password);
		      document.append("First_Name", firstName);
		      document.append("Last_Name", lastName);
		      //Inserting the document into the collection
		      db.getCollection("User").insertOne(document);
		}
		
		//AddProduct
		public static void AddProduct(String name, String description, int price, int initialStock) {
			
			int count = (int) product.countDocuments();
			int prodID = count + 1;
			//Preparing a document
		      Document document = new Document();
		      document.append("Product_ID", prodID);
		      document.append("Name", name);
		      document.append("Description", description);
		      document.append("Price", price);
		      document.append("Count_Stock", initialStock);
		      //Inserting the document into the collection
		      db.getCollection("Product").insertOne(document);
		}
		
		//SubmitOrder
		public static void SubmitOrder(String date, String userName, String passWord, HashMap<Integer, Integer> listOfProductsAndQuantities) {
//			user.find({"password", "username": userName});
			
//			var query = user.find(d).projection(Projections.include("password")).iterator();
//			while(query.hasNext()) {
//				System.out.println(query.next());
//			}

			// check whether given user name exists and if password matches
			String password = "";
			BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("User_Name", userName);
			FindIterable<Document> iterDoc = user.find(whereQuery);
			for(Document d : iterDoc) {
				password = d.getString("Password");
		    }

			if (password.equals(passWord)) {
				// creating order ID
				int count = (int) purchase.countDocuments();
				int orderID = count + 1;	
				for (int productID : listOfProductsAndQuantities.keySet()) {
					int qty = 0;
					BasicDBObject whereQueryQty = new BasicDBObject();
					whereQueryQty.put("Product_ID", productID);
					FindIterable<Document> iterDocQty = product.find(whereQueryQty);
					for(Document d : iterDocQty) {
						qty = d.getInteger("Count_Stock");
				    }

    			    if(qty >= listOfProductsAndQuantities.get(productID)) {
    			    	//Preparing a document
    				    Document document = new Document();
    				    document.append("Order_ID", orderID);
    				    document.append("Produ_ID", productID);
    				    document.append("Quantity", qty);
    				    document.append("user_name", userName);
    				    document.append("Date_Order", date);
    				    //Inserting the document into the collection
    				    db.getCollection("Purchase").insertOne(document);
    			 
    					int totalCount = qty - listOfProductsAndQuantities.get(productID);
    					product.updateOne(Filters.eq("Product_ID", productID), Updates.set("Count_Stock", totalCount));
    			    	
    					int countt = 0;
    					BasicDBObject whereQueryCount = new BasicDBObject();
    					whereQueryCount.put("Product_ID", productID);
    					FindIterable<Document> iterDocCount = product.find(whereQueryCount);
    					for(Document d : iterDocCount) {
    						countt = d.getInteger("Count_Stock");
    				    }
    					if(countt < 0) {
	    			    	countNegative++;
	    			    }
					}
			    }
			}
		}		   			
			
		
//			var query = ("Password": {"User_Name": username);
//			var v = db.getCollection("user").find({query} }).iterator();
			
			
			
//			BasicDBObject whereQuery = new BasicDBObject();
//	        whereQuery.put("username", userName);
//	        var cursor = user.find(whereQuery);
//	        while(cursor.hasNext()) {
//	            System.out.println(cursor.next().get("password"));
//	        }
			
		
		//PostReview
		public static void PostReview(String userName, String passWord, int productID, int rating, String reviewText) {
			// check whether given user name exists and if password matches
			String password = "";
			BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("User_Name", userName);
			FindIterable<Document> iterDoc = user.find(whereQuery);
			for(Document d : iterDoc) {
				password = d.getString("Password");
		    }

			if (password.equals(passWord)) {
	    		// checking if user has already posted a review about the same product
				int prodID = 0;
				BasicDBObject whereQueryReview = new BasicDBObject();
				whereQueryReview.append("UserID", userName);
				whereQueryReview.append("Prod_ID", productID);
			    FindIterable<Document> iterDocReview = review.find(whereQueryReview);
				for(Document d : iterDocReview) {
					prodID = d.getInteger("Prod_ID");
			    }
				
				if (prodID == 0) {
					Random randMonth = new Random();
	            	Random randDay = new Random();
	            	int month = randMonth.nextInt(12) + 1;
	            	int day = randDay.nextInt(28) + 1;
	            	String date = 2020 + "-" + month + "-" + day;
	            	
	            	//Preparing a document
				    Document document = new Document();
				    document.append("UserID", userName);
				    document.append("Prod_ID", productID);
				    document.append("Rating", rating);
				    document.append("Review", reviewText);
				    document.append("Date_Review", date);
				    //Inserting the document into the collection
				    db.getCollection("Review").insertOne(document);
				}
	    	}
		}
		
		//UpdateStockLevel
		public static void UpdateStockLevel(int productID, int itemCountToAdd) throws Exception {
			
			int count = 0;
			BasicDBObject whereQueryQty = new BasicDBObject();
			whereQueryQty.put("Product_ID", productID);
			FindIterable<Document> iterDocQty = product.find(whereQueryQty);
			for(Document d : iterDocQty) {
				count = d.getInteger("Count_Stock");
		    }
			int totalCount = count + itemCountToAdd;
			product.updateOne(Filters.eq("Product_ID", productID), Updates.set("Count_Stock", totalCount));
		}
		
		//GetProductAndReviews
		public static void GetProductAndReviews(int productID) {
			String reviewText = "";
			String description = "";
			String name = "";
			String username = "";
			int rating = 0;
			int price = 0;
			int initialStock = 0;
			
			BasicDBObject whereQueryProd = new BasicDBObject();
			BasicDBObject whereQueryReview = new BasicDBObject();
			whereQueryProd.put("Product_ID", productID);
			whereQueryReview.put("Prod_ID", productID);
			FindIterable<Document> iterDocProd = product.find(whereQueryProd);
			for(Document d : iterDocProd) {
				price = d.getInteger("Price");
				name = d.getString("Name");
				description = d.getString("Description");
				initialStock = d.getInteger("Count_Stock");
			}
			FindIterable<Document> iterDocReview = review.find(whereQueryReview);
			for(Document d : iterDocReview) {
				rating = d.getInteger("Rating");
				username = d.getString("UserID");
				reviewText = d.getString("Review");
			}
			
//			System.out.println(price);
//			System.out.println(name);
//			System.out.println(description);
//			System.out.println(initialStock);
//			System.out.println(rating);
//			System.out.println(username);
//			System.out.println(reviewText);
//			System.out.println(productID);
				
		}
		
		//GetAverageUserRating
		public static void GetAverageUserRating(String username) {
			
			int RatingSum = 0;
			int count = 0;
			BasicDBObject whereQuery = new BasicDBObject();
	        whereQuery.put("UserID", username);
			FindIterable<Document> iterDoc = review.find(whereQuery);
			for(Document d : iterDoc) {
				RatingSum += d.getInteger("Rating");
				count++;
		    }
			float averageRating = RatingSum / count;
//			System.out.println(averageRating);
		}
	
}
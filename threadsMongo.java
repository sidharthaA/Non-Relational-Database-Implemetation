import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class threadsMongo extends Thread {
	
	public void run(){
		Random randProbability = new Random();
    	int probability = randProbability.nextInt(101);
    	
    	//CreateAccount
    	if (probability > 0 && probability <= 3) {
    		Random randUser = new Random();
        	int i = randUser.nextInt(10000) + 1001;
			try {
				TransactionsMongo.CreateAccount("user" + i, "pwd" + i, "firstName" + i, "lastName" + i);
			} catch (Exception e) {
				e.printStackTrace();
			} 
    	}
    	
    	// AddProduct
    	if (probability > 3 && probability <= 5) {
    		Random randStock = new Random();
        	Random randPrice = new Random();
        	Random randProdID = new Random();
            int stock = randStock.nextInt(101) + 1;
            int price = randPrice.nextInt(1001) + 1;  
            int i = randProdID.nextInt(10000) + 10001;
			try {
				TransactionsMongo.AddProduct("prod" + i, "desc" + i, price, stock);
			} catch (Exception e) {
				e.printStackTrace();
			} 
    	}
    	
    	//UpdateStockLevel
    	if (probability > 5 && probability <= 15) {
    		Random randCountToAdd = new Random();
        	Random randProdID = new Random();
            int countToAdd = randCountToAdd.nextInt(10) + 1;
            int prodID = randProdID.nextInt(10000) + 1;
            try {
				TransactionsMongo.UpdateStockLevel(prodID, countToAdd);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//GetProductAndReviews
    	if (probability > 15 && probability <= 80) {
    		Random randProdID = new Random();
    		int prodID = randProdID.nextInt(10000) + 1;
    		try {
				TransactionsMongo.GetProductAndReviews(prodID);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//GetAverageUserRating
    	if (probability > 80 && probability <= 85) {
    		Random randUser = new Random();
        	int i = randUser.nextInt(1001) + 1;
    		try {
				TransactionsMongo.GetAverageUserRating("user" + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//SubmitOrder
    	if (probability > 85 && probability <= 95) {
    		Random randUser = new Random();
        	Random randProdID = new Random();
        	Random randQuantity = new Random();
        	Random randMonth = new Random();
        	Random randDay = new Random();
	    	int ubUser = 1001;
	        int ubProdID = 10001;
        	int j = randUser.nextInt(ubUser) + 1;
        	int month = randMonth.nextInt(12) + 1;
        	int day = randDay.nextInt(28) + 1;
        	String date = 2020 + "-" + month + "-" + day;
        	HashMap<Integer, Integer> listOfProductsAndQuantities = new HashMap<Integer, Integer>();
        	for(int x = 1; x <= 10; x++) {
        		int prodID = randProdID.nextInt(ubProdID) + 1;
        		int quantity = randQuantity.nextInt(20) + 1;
        		listOfProductsAndQuantities.put(prodID, quantity);
        	}
    		try {
				TransactionsMongo.SubmitOrder(date, "user" + j, "pwd" + j, listOfProductsAndQuantities);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//PostReview
    	if (probability > 95 && probability <= 100) {
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
				TransactionsMongo.PostReview("user" + j, "pwd" + j, prodID, rating, "reviewText" + j);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}
	
	public static void main(String[] args) throws Exception{
		
		//populate function call
		TransactionsMongo.main(args);
		int counter = 0;
		//threads count 1
		long startTime1 = System.currentTimeMillis();
		System.out.println("thread1");
		
	    while (System.currentTimeMillis() < startTime1 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	t1.start();
	    	t1.join();
	    	counter++;
	    }
	    
	    FileWriter myWriter = new FileWriter("record.txt");
	      myWriter.write("1 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("1 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("thread1 ends");
	    
	    TransactionsMongo.Populate();
	  //threads count 2
	    counter = 0;
	    long startTime2 = System.currentTimeMillis();
	    System.out.println("thread2");
	    while (System.currentTimeMillis() < startTime2 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t1.join();
	    	t2.join();
	    	counter++;
	    }
	    
	    myWriter.write("2 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("2 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("thread2 ends");
	    
	    TransactionsMongo.Populate();
	  //threads count 3
	    counter = 0;
	    long startTime3 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime3 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	counter++;
	    }
	    
	    myWriter.write("3 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("3 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 4
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime4 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime4 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	counter++;
	    }
	    
	    myWriter.write("4 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("4 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 5
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime5 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime5 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	Thread t5 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	counter++;
	    }
	    
	    myWriter.write("5 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("5 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 6
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime6 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime6 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	Thread t5 = new threadsMongo();
	    	Thread t6 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	counter++;
	    }
	    
	    myWriter.write("6 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("6 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 7
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime7 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime7 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	Thread t5 = new threadsMongo();
	    	Thread t6 = new threadsMongo();
	    	Thread t7 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	counter++;
	    }
	    
	    myWriter.write("7 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("7 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 8
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime8 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime8 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	Thread t5 = new threadsMongo();
	    	Thread t6 = new threadsMongo();
	    	Thread t7 = new threadsMongo();
	    	Thread t8 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t8.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	t8.join();
	    	counter++;
	    }
	    
	  //threads count 9
	    myWriter.write("8 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("8 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime9 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime9 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	Thread t5 = new threadsMongo();
	    	Thread t6 = new threadsMongo();
	    	Thread t7 = new threadsMongo();
	    	Thread t8 = new threadsMongo();
	    	Thread t9 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t8.start();
	    	t9.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	t8.join();
	    	t9.join();
	    	counter++;
	    }
	    
	    myWriter.write("9 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("9 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 10
	    TransactionsMongo.Populate();
	    counter = 0;
	    long startTime10 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime10 + 300000) {
	    	Thread t1 = new threadsMongo();
	    	Thread t2 = new threadsMongo();
	    	Thread t3 = new threadsMongo();
	    	Thread t4 = new threadsMongo();
	    	Thread t5 = new threadsMongo();
	    	Thread t6 = new threadsMongo();
	    	Thread t7 = new threadsMongo();
	    	Thread t8 = new threadsMongo();
	    	Thread t9 = new threadsMongo();
	    	Thread t10 = new threadsMongo();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t8.start();
	    	t9.start();
	    	t10.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	t8.join();
	    	t9.join();
	    	t10.join();
	    	counter++;
	    }
	    
	    myWriter.write("10 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    System.out.println("10 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + TransactionsMongo.countNegative + "\n****************************************************************************\n");
	    myWriter.close();
	}
}

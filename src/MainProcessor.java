import javafx.application.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * README
 * - this program parses through amazon customer review data set to find 
 * ---- 1) What are the 10 most frequent words found across all the reviews? 
 * -------- the findings are as follows from ~1 Million Data: 
 * -------- These are the 10 most frequent words found across all products:

		[
		  great:  315478,
		  sound:  251156,
		  good:  221742,
		  works:  163690,
		  br:  162148,
		  one:  159942,
		  quality:  146545,
		  use:  136458,
		  product:  123704,
		  well:  122658,
		  ...211831 more 
		]

 * ---- 2) Which products received the most number of customer reviews?
 * These are the 10 most reviewed products:

		[ * just a sample
		  B003L1ZYYM:
		  product_title:  AmazonBasics High-Speed HDMI Cable - 6.5 Feet (2 Meters) Supports Ethernet, 3D, 4K and Audio Return,
		  size:  6654,
		]
		
 * - it builds a CustomerReview HashMap, using review_id as the key for the HashMap
 * - it builds a ProductId HashMap that uses the product_id to store two things: 
 * ---- 1) a list of all the customer review list 
 * ---- 2) a HashMap, using the string as the key, to count how many words appear
 * -------- this has been commented out for efficiency purposes.
 *  
 * RUNNING THE PROGRAM
 * - it is not recommended to run more than 1 Million data. 
 * - there are 4 files provided
 * --- extracted from https://s3.amazonaws.com/amazon-reviews-pds/tsv/index.txt
 * ---- sample.tsv (~ 50 data)
 * ---- amazon_review_electronics_small.tsv (~ 100K data)
 * ---- amazon_review_electronics_medium.tsv (~ 500K data)
 * ---- amazon_review_electronics_large.tsv (~ 1 Million data)
 *  
 * - screenshot of the run from 1 Million data has been attached. 
 * - most processing methods can be found in the Util.java file. 
 * 
 * ----------------------------------------------------------------------------------------------------
 * CustomerReview.jsava Comments
 * Site obtained from: https://s3.amazonaws.com/amazon-reviews-pds/tsv/index.txt
 * 
 * DATA COLUMNS:
 * marketplace       - 2 letter country code of the marketplace where the review was written.
 * customer_id       - Random identifier that can be used to aggregate reviews written by a single author.
 * review_id         - The unique ID of the review.
 * product_id        - The unique Product ID the review pertains to. In the multilingual dataset the reviews
 *                     for the same product in different countries can be grouped by the same product_id.
 * product_parent    - Random identifier that can be used to aggregate reviews for the same product.
 * product_title     - Title of the product.
 * product_category  - Broad product category that can be used to group reviews 
 *                     (also used to group the dataset into coherent parts).
 * star_rating       - The 1-5 star rating of the review.
 * helpful_votes     - Number of helpful votes.
 * total_votes       - Number of total votes the review received.
 * vine              - Review was written as part of the Vine program.
 * verified_purchase - The review is on a verified purchase.
 * review_headline   - The title of the review.
 * review_body       - The review text.
 * review_date       - The date the review was written.
 * 
 * @author kaungyang
 */



public class MainProcessor extends Application {

    private Stage primaryStage;
    private Text statusText, resultText;
    private Button uploadButton;
    
    private String[] dataColumns = null; 
    private static int numberOfDataColumns = 0; 
    private static boolean isFirstLine = true; 
    private static int totalReviews = 0; 
    
    
    private Map< String, ListMapNode > productIdMap = new HashMap<>(); 
    private Map< String, CustomerReview> customerReviewMap= new HashMap<>(); 
    private Map< String, Integer> uniqueWordCounts = new HashMap<>(); 
    
    
    private final static Font RESULT_FONT = Font.font("Lato", 24);
    private final static Font INPUT_FONT = Font.font("Lato", 20);
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        VBox primaryBox = new VBox();
        primaryBox.setAlignment(Pos.CENTER);
        primaryBox.setSpacing(20);
        primaryBox.setStyle("-fx-background-color: white");

        VBox uploadBox = new VBox();
        uploadBox.setAlignment(Pos.CENTER);
        uploadBox.setSpacing(20);
        Text uploadLabel = new Text("Upload an Amazon Customer Review Data Set that is Tab Seperated.");
        uploadLabel.setFont(INPUT_FONT);
        uploadButton = new Button("Upload data");
        uploadButton.setOnAction(this::processDataUpload);

        uploadBox.getChildren().add(uploadLabel);
        uploadBox.getChildren().add(uploadButton);
        primaryBox.getChildren().add(uploadBox);

        VBox resultsBox = new VBox();
        resultsBox.setAlignment(Pos.CENTER);
        resultsBox.setSpacing(20);
        statusText = new Text("");
        statusText.setVisible(false);
        statusText.setFont(RESULT_FONT);
        statusText.setFill(Color.RED);
        resultText = new Text("");
        resultText.setVisible(false);
        resultText.setFont(RESULT_FONT);
        resultsBox.getChildren().add(statusText);
        resultsBox.getChildren().add(resultText);
        primaryBox.getChildren().add(resultsBox);

        Scene scene = new Scene(primaryBox, 1000, 800, Color.TRANSPARENT);
        primaryStage.setTitle("Amazon Customer Reviews Data Upload");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void processDataUpload(ActionEvent event) {
        statusText.setVisible(false);
        resultText.setVisible(false);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TSV files (*.tsv)", "*.tsv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        parseFile(file);

    }
    
    public String[] getDataColumns() { return this.dataColumns; }
    
    /**
     * this method parses a single line using the delimiter provided.
     * it also parses the first line as a header. 
     * 
     * @param line the line to be parsed
     * @param delimiter the delimiter to use
     * @return String array of size 2, containing the CustomerReview's id and number of orders.
     * @throws ExpectedLessDataException 
     */
    private static String[] parseLine(String line, String delimiter) throws CustomExceptions.ExpectedLessDataColumnsException {
    	String[] data = null; 
		Scanner lineScan = new Scanner(line); 
    	lineScan.useDelimiter(delimiter); 
    	
    	if(isFirstLine) {
    		List<String> dataColumns = new ArrayList<String>();
    		while(lineScan.hasNext()) {
    			String s = lineScan.next(); 
    			dataColumns.add(s); 
    		}
    		
    		return dataColumns.toArray(new String[0]); 
    	}
    	
		int dataCount = 0;
		data = new String[numberOfDataColumns]; 
		
		while(lineScan.hasNext()) { 
			if( dataCount < numberOfDataColumns ) {
				String s = lineScan.next();
    			data[dataCount] = s; 
    			dataCount++;
			} else {				
				throw new CustomExceptions().new ExpectedLessDataColumnsException(); // more than two columns in the data  
			}  
		}
   
    	return data;
    }

    /**
     * 
     * @param file the file to read in. 
     */
    private void parseFile(File file) {
    	ArrayList<CustomerReview> customerReviewOrderList = new ArrayList<CustomerReview>(); 
    	String[] customerReviewData = null;
    	Scanner fileScan = null; 
    	try { 
    		
    		if(file == null) {
    			throw new FileNotFoundException(); 
    		}
    	
    		isFirstLine = true; 
    		
    		fileScan = new Scanner(file); 
    		while(fileScan.hasNext()) {
    			
    			if(isFirstLine) {
    				String headerLine = fileScan.nextLine();
    				dataColumns = parseLine(headerLine, "\t");
    				numberOfDataColumns = dataColumns.length; 
    				isFirstLine = false; 
    			} else {
    				String line = fileScan.nextLine(); 
            		customerReviewData = parseLine(line, "\t");
            		
            		// build customerReviewData object
            		CustomerReview newReview = Util.buildCustomerReview(customerReviewData);
            		if(newReview != null) {
            			totalReviews++; 
                		populateMaps(newReview); 
            		}
    			}
    		}    		
//    		Util.printProductIdMap(productIdMap, 10); 
    		System.out.println("There are: " + totalReviews + " reviews");
    		System.out.println("These are the 10 most frequent words found across all products:");
    		String uniqueWordCountString = Util.getStringCountMapString(uniqueWordCounts, 10);
    		System.out.println(uniqueWordCountString); 
    		
    		System.out.println("These are the 10 most reviewed products:"); 
    		String mostReviewedProducts = Util.getMostReviewedProducts(productIdMap, customerReviewMap, 10);
    		System.out.println(mostReviewedProducts); 
    		
    		
    	} catch(NumberFormatException ex) {
    		System.out.println(ex.getMessage()); 
    	} catch(CustomExceptions.ExpectedLessDataColumnsException ex) {
    		System.out.println(ex.getMessage()); 
    	} catch(FileNotFoundException ex) {
    		System.out.println(ex.getMessage()); 
     	} finally {
     		if(fileScan != null) {
     			fileScan.close();
     			System.out.println("Done. Goodbye!"); 
     		}
     	}
    }
    
    /**
     * 
     */
    private void populateMaps(CustomerReview cr) {
    	
    	String productId = cr.getProductId();
    	String reviewId = cr.getReviewId();
    	String reviewBody = cr.getReviewBody();
    	String[] wordArray = Util.getCleanWordArray(reviewBody);
    	cr.setCleanReviewBody(wordArray);
    	
    	if(!productIdMap.containsKey(productId)) { // new
    		List<String> newReviewIdList = new ArrayList<>();  
    		newReviewIdList.add(reviewId);
    		
    		Map<String, Integer> newStringCountMap = new HashMap<>();
    		/* this has been commented out to save memory while running */
    		// populateStringCount(newStringCountMap, wordArray); 
    		
    		/* 
    		 * ListMapNode contains a list of all the reviews in the first parameter 
    		 * and the string-count HashMap in the second 
    		 */
    		ListMapNode newNode = new ListMapNode(newReviewIdList, newStringCountMap); 

    		productIdMap.put(productId, newNode);
    		
    	} else {
    		ListMapNode tempNode = productIdMap.get(productId);
    		List<String> tempReviewIdList = tempNode.first();
    		if(tempReviewIdList == null) { tempReviewIdList = new ArrayList<>(); }
    		
			if(!tempReviewIdList.contains(reviewId)) {
				tempReviewIdList.add(reviewId);
			}
			
			// /* this has been commented out to save memory while running */
			// Map<String, Integer> tempStringCountMap = tempNode.second();
			// populateStringCount(tempStringCountMap, wordArray);     		
    	} 
    	
    	populateStringCount(uniqueWordCounts, wordArray); 
    	customerReviewMap.put(reviewId, cr);
    }
        
    /**
     * 
     * @param map
     * @param strArr
     */
    private void populateStringCount(Map<String, Integer> map, String[] strArr) {
    	for(String s: strArr) {
    		if(!map.containsKey(s)) { // if a new word
    			map.put(s, 1); 
    		} else { //if not a new word
    			int num = map.get(s);
    			map.put(s, num + 1); 
    		}		
    	}
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}

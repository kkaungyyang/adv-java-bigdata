import java.util.*; 

/**
 * This class is mainly for combining reusable functionalities.   
 * @author kaungyang
 * 
 */
public class Util {
	
    protected final static int MARKET_PLACE_INDEX = 0;
    protected final static int CUSTOMER_ID_INDEX = 1; 
    protected final static int REVIEW_ID_INDEX = 2; 
    protected final static int PRODUCT_ID_INDEX = 3;
    protected final static int PRODUCT_PARENT_INDEX = 4; 
    protected final static int PRODUCT_TITLE_INDEX = 5; 
    protected final static int PRODUCT_CATEGORY_INDEX = 6;
    protected final static int STAR_RATING_INDEX = 7; 
    protected final static int HELPFUL_VOTES_INDEX = 8; 
    protected final static int TOTAL_VOTES_INDEX = 9; 
    protected final static int VINE_INDEX = 10; 
    protected final static int VERIFIED_PURCHASE_INDEX = 11; 
    protected final static int REVIEW_HEADLINE_INDEX = 12; 
    protected final static int REVIEW_BODY_INDEX = 13; 
    protected final static int REVIEW_DATE_INDEX = 14;  
    
    // curated from: https://stackoverflow.com/questions/9953619/technique-to-remove-common-wordsand-their-plural-versions-from-a-string
    protected final static String[] STOP_WORDS = {
    		"all", "just", "being", "over", "both", "through", "yourselves", "its", "before", "herself", "had",
    		"should", "to", "only", "under", "ours", "has", "do", "them", "his", "very", "they", "not", "during",
    		"now", "him", "nor", "did", "this", "she", "each", "further", "where", "few", "because", "doing", "some",
    		"are", "our", "ourselves", "out", "what", "for", "while", "does", "above", "between", "t", "be", "we", "who",
    		"were", "here", "hers", "by", "on", "about", "of", "against", "s", "or", "own", "into", "yourself", "down",
    		"your", "from", "her", "their", "there", "been", "whom", "too", "themselves", "was", "until", "more", "himself",
    		"that", "but", "don", "with", "than", "those", "he", "me", "myself", "these", "up", "will", "below", "can",
    		"theirs", "my", "and", "then", "is", "am", "it", "an", "as", "itself", "at", "have", "in", "any", "if", "again",
    		"no", "when", "same", "how", "other", "which", "you", "after", "most", "such", "why", "a", "off", "i", "yours", "so",
    		"the", "having", "once"
    };
    
    /**
     * returns a string in [ str1, str2, ..., times more strings ] format 
     * @param list the list to concatenate to string with 
     * @param times the number of times wanted to be printed 
     * @return the nicely formatted string to be printed 
     */
	public static<T> String getListString(List<T> list, int times) {
		int size = list.size();
		StringBuilder sb = new StringBuilder(); 
		sb.append("["); 
		for(int i = 0; i < size && i < times; i ++) {
			sb.append(list.get(i)).append(", "); 
		}
		int left = size-times; 
		if(left < 0)
			left = 0; 
		
		sb.append("...").append(left).append(" more ]");
		
		return sb.toString(); 
	}
	
	/**
	 * this method returns the string representation of string-count hashmap. 
	 * used to print maps like string-count hash maps that contains the string as the key and count as the value. 
	 *    
	 * @param map the map that contains the strings and counts 
	 * @param numOfString the number of strings to print. 
	 * @return A String representation of the string-count hashmap for numOfString amount of times.  
	 */
	public static String getStringCountMapString(Map<String, Integer> map, int numOfString) {
		int size = map.size();
		StringBuilder sb = new StringBuilder(); 
		sb.append("\n\t\t[\n"); 
		
		Set<String> keys = map.keySet(); 
		Iterator<String> itr = keys.iterator(); 
		
		LinkedHashMap<String, Integer> sortedMap = sortHashMapByValues(map); 
		
		List<String> sortedKeySet = new ArrayList<>(sortedMap.keySet());
		
		Collections.reverse(sortedKeySet); 
		Iterator<String> sortedKeySetItr = sortedKeySet.iterator();
		int count = 0; 
		while(sortedKeySetItr.hasNext() && count < numOfString) {
			String tempKey = sortedKeySetItr.next();
			int tempNum = map.get(tempKey); 
			sb.append("\t\t  ").append(tempKey).append(":  ").append(tempNum).append(",\n"); 
			count++; 
		}
		
		int left = size-numOfString; 
		if(left < 0)
			left = 0; 
		sb.append("\t\t  ...").append(left).append(" more \n\t\t]\n");
		
		return sb.toString(); 
	}
	
	/**
	 * this method returns the String representation of the most reviewed products
	 * 
	 * @param map the map that contains the product, listMapNode
	 * @param reviewMap the hashmap that contains all the reviews using the review_id as the key 
	 * @param numOfProducts the number of products (number of times) to print, sorted, reversed
	 * @return
	 */
	public static String getMostReviewedProducts(Map<String, ListMapNode> map, Map<String, CustomerReview> reviewMap, int numOfProducts) {
		int size = map.size();
		StringBuilder sb = new StringBuilder(); 
		sb.append("\n\t\t[\n"); 
		int max = 0; 
		
		Map<String, Integer> numOfReviewsOnProductHashMap = new HashMap<>(); 
	
		Set<String> productIdKeys = map.keySet(); 
		Iterator<String> keyItr = productIdKeys.iterator(); 
		
		while(keyItr.hasNext()) {
			String key = keyItr.next(); 	
			ListMapNode lmn = map.get(key);
			
			List<String> reviewIdList = lmn.first(); 
			int reviewListSize = reviewIdList.size(); 
			if(key != null) {
				numOfReviewsOnProductHashMap.put(key, reviewListSize);
			}
			 
		}
		
		LinkedHashMap<String, Integer> sortedMap = sortHashMapByValues(numOfReviewsOnProductHashMap); 
		List<String> sortedKeySet = new ArrayList<>(sortedMap.keySet());
		Collections.reverse(sortedKeySet); 

		Iterator<String> sortedMapKeyItr = sortedKeySet.iterator(); 
		
		int count = 0; 
		while(sortedMapKeyItr.hasNext() && count < numOfProducts) {
			String tempKey = sortedMapKeyItr.next();
			int tempValue = sortedMap.get(tempKey); 
			String customerReviewId = map.get(tempKey).first().get(0);
			String productTitle = reviewMap.get(customerReviewId).getProductTitle(); 
			
			sb.append("\t\t  ").append(tempKey).append(":\n\t\t  product_title:  ");
			sb.append(productTitle).append(",\n\t\t  ").append("size:  ").append(tempValue).append(",\n\n"); 
			count++; 
		}
		
		int left = size-numOfProducts; 
		if(left < 0)
			left = 0; 
		sb.append("\t\t  ...").append(left).append(" more \n\t\t]\n");
		
		return sb.toString(); 
	}
	
	/**
	 * sorts the hash map provided by its value. 
	 * 
	 * @param map the hash map to be sorted 
	 * @return a LinkedHashMap<String, Integer>, a sortedMap of the hashMap
	 */
	public static LinkedHashMap<String, Integer> sortHashMapByValues(Map<String, Integer> map) {
	    List<String> mapKeys = new ArrayList<>(map.keySet());
	    List<Integer> mapValues = new ArrayList<>(map.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Integer val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            String key = keyIt.next();
	            
	            Integer comp1 = map.get(key);
	            Integer comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
	/**
	 * this method builds the customer review if all rows of the customer review is well-formed
	 * otherwise, it will return a null object. 
	 * 
	 * @param data the String array that contains the data for customer review
	 * @return the CustomerReview object if data is well-formed or null otherwise. 
	 */
    public static CustomerReview buildCustomerReview(String[] data) {
    
    	boolean containsNull = containsNullInArray(data); 
    	if(!containsNull) {
    		
    		boolean vine = false; 
        	boolean verifiedPurchase = false;         	
        	if(data[VINE_INDEX].equalsIgnoreCase("y")) { vine = true; }
        	if(data[VERIFIED_PURCHASE_INDEX].equalsIgnoreCase("y")) { verifiedPurchase = true; }
    		
    		CustomerReview newReview 
			= new CustomerReview.Builder()
			.marketPlace(data[MARKET_PLACE_INDEX])
			.customerId(data[CUSTOMER_ID_INDEX])
			.reviewId(data[REVIEW_ID_INDEX]) 
			.productId(data[PRODUCT_ID_INDEX])
			.productParent(data[PRODUCT_PARENT_INDEX])
			.productTitle(data[PRODUCT_TITLE_INDEX])
			.productCategory(data[PRODUCT_CATEGORY_INDEX])
			.starRating(Integer.parseInt(data[STAR_RATING_INDEX]))
			.helpfulVotes(Integer.parseInt(data[HELPFUL_VOTES_INDEX]))
			.totalVotes(Integer.parseInt(data[TOTAL_VOTES_INDEX]))
			.vine(vine)
			.verifiedPurchase(verifiedPurchase)
			.reviewHeadline(data[REVIEW_HEADLINE_INDEX])
			.reviewBody(data[REVIEW_BODY_INDEX])
			.reviewDate(data[REVIEW_DATE_INDEX])
			.build(); 
	
    		return newReview; 
    	}
    	
    	return null; 
    	
    	
//    	if(data[STAR_RATING_INDEX] == null)
//			data[STAR_RATING_INDEX] = "1"; 
//    	
//    	if(data[HELPFUL_VOTES_INDEX] == null)
//    		data[HELPFUL_VOTES_INDEX] = "1"; 
//    	
//    	if(data[TOTAL_VOTES_INDEX] == null)
//    		data[TOTAL_VOTES_INDEX] = "1"; 
//    	
//    	if(data[VINE_INDEX] == null)
//    		data[VINE_INDEX] = "N";
//    	
//    	if(data[VERIFIED_PURCHASE_INDEX] == null)
//    		data[VERIFIED_PURCHASE_INDEX] = "N";
//    	
//		if(data[VERIFIED_PURCHASE_INDEX] == null)
//			data[VERIFIED_PURCHASE_INDEX] = "N";
//    
//    	boolean vine = false; 
//    	boolean verifiedPurchase = false; 
//    	
//    	if(data[VINE_INDEX].equalsIgnoreCase("y")) {
//    		vine = true; 
//    	}
//    	
//    	if(data[VERIFIED_PURCHASE_INDEX].equalsIgnoreCase("y")) {
//    		verifiedPurchase = true; 
//    	}
//    	
    }
    
    /**
     * checks whether the string array contains null data 
     * @param data the string array that might contain some null data
     * @return true if data contains null values, and false otherwise. 
     */
    public static boolean containsNullInArray(String[] data) {
    	for(String s: data) {
    		if(s == null) {
    			return true; 
    		}
    	}
    	return false; 
	}
      
    /**
     * this function removes all the punctuation and stop words from the string
     * @param str the string to be cleaned 
     * @return the array of string words that are clean
     */
    public static String[] getCleanWordArray(String str) {
    	
    	if(str != null) {
    		String[] noPuncWords = str.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        	List<String> retWords = new ArrayList<>(); 
            
        	for(int i = 0; i < noPuncWords.length; i++) {
        		for(String sw: STOP_WORDS) {
        			if (noPuncWords[i].equalsIgnoreCase(sw)) {
        				noPuncWords[i] = null; 
        				break; 
        			}
        		}
        		
        		if(noPuncWords[i] != null) {
        			retWords.add(noPuncWords[i]); 
        		}
        	}
        	return retWords.toArray(new String[0]); 
    	}
    	return new String[] {}; 
    }
    
    /**
     * this function prints out the productIdMap. Uses product_id as the key and ListMapNode as a value.
     * the ListMapNode, described by <List, Map<String, Integer> contains the list of all review id's and
     * a HashMap that uses the string as the key and stores the number of occurence as its value.  
     *  
     * @param map the productIdMap  
     */
	public static void printProductIdMap(Map<String, ListMapNode> map, int numTimes) {
		//	private Map< String, ListMapNode > productIdMap = new HashMap<>();
		if(map != null) {
			Set<String> productIds = map.keySet(); 
			Iterator<String> itr = productIds.iterator();
			StringBuilder sb = new StringBuilder();
			int count = 0;
			while(itr.hasNext() && count < numTimes) {
				String productId = itr.next(); 
				ListMapNode node = map.get(productId);
				List<String> reviewIdList = node.first(); 
				Map<String, Integer> stringCountMap = node.second();

				String listStr = Util.getListString(reviewIdList, 10);
				String mapStr = Util.getStringCountMapString(stringCountMap, 10); 
					 
				sb.append("{\n"); 
				sb.append("\tproduct_id: ").append(productId).append("\n");
				sb.append("\treview_id List: ").append(listStr).append("\n"); 
				sb.append("\tstring counts: ").append(mapStr).append("\n");
				sb.append("}\n--------------------------\n");
				count ++; 
			}
			System.out.println(sb.toString());
			
		}
	}
    
    /**
     * this method is just for printing out an array nicely 
     * 
     * @param arr the array to loop through 
     * @param useFormat whether to use json-like format
     */
	public static <T> void printArray(T[] arr, boolean useFormat) {
		if(!useFormat) {
			for(T a: arr)
				System.out.println(a); 
		} else {
			System.out.println("{"); 
			for(T a: arr) {
				System.out.print("\t"); 
				System.out.println(a); 
			}
			System.out.println("}\n"); 
		}
	}
	
	/**
     * this method prints out key-pairs in json-like format if the two arrays passed in are the same size.
     *  
     * @param keyArr the key part of the array 
     * @param valueArr the value part of the pairs
     */
	public static <T> void printKeyValue(T[] keyArr, T[] valueArr) {
		if(keyArr.length == valueArr.length) {
			System.out.println("{"); 
			for(int i = 0; i < keyArr.length; i++) {
				System.out.print("\t"); 
				System.out.println(keyArr[i] + ":....." + valueArr[i]); 
			}
			System.out.println("}\n");
		} else {
			System.out.println("{ ERROR: Wrong Data Format }");
		}
	}
}

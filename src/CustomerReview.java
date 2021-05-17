/**
 * This class represents the Amazon Customer Reviews, obtained from the amazon datasets
 * 
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
 * 
 * @author kaungyang
 *
 */
public class CustomerReview implements Comparable<CustomerReview>{
	private String marketPlace; 
	private String customerId; 
	private String reviewId;
	private String productId;
	private String productParent; 
	private String productTitle; 
	private String productCategory; 
	private int starRating; 
	private int helpfulVotes; 
	private int totalVotes; 
	private boolean vine; 
	private boolean verifiedPurchase; 
	private String reviewHeadline; 
	private String reviewBody; 
	private String reviewDate;
	private String[] cleanReviewBody; 
	
	private CustomerReview(Builder builder) {
		this.marketPlace = builder.marketPlace; 
		this.customerId = builder.customerId; 
		this.reviewId = builder.reviewId; 
		this.productId = builder.productId; 
		this.productParent = builder.productParent; 
		this.productTitle = builder.productTitle; 
		this.productCategory = builder.productCategory; 
		this.starRating = builder.starRating; 
		this.helpfulVotes = builder.helpfulVotes; 
		this.totalVotes = builder.totalVotes; 
		this.vine = builder.vine; 
		this.verifiedPurchase = builder.verifiedPurchase; 
		this.reviewHeadline = builder.reviewHeadline; 
		this.reviewBody = builder.reviewBody; 
		this.reviewDate = builder.reviewDate; 
	}
	
	/* getters for CustomerReviews Class */ 
	public String getMarketPlace() { return this.marketPlace; }
	public String getCustomerId() { return this.customerId; }
	public String getReviewId() { return this.reviewId; }
	public String getProductId() { return this.productId; }
	public String getProductParent() { return this.productParent; }
	public String getProductTitle() { return this.productTitle; }
	public String getProductCategory() { return this.productCategory; }
	public int getStarRating() { return this.starRating; }
	public int getHelpfulVotes() { return this.helpfulVotes; }
	public int getTotalVotes() { return this.totalVotes; }
	public boolean getVine() { return this.vine; }
	public boolean getVerifiedPurchase() { return this.verifiedPurchase; }
	public String getReviewHeadline() { return this.reviewHeadline; }
	public String getReviewBody() { return this.reviewBody; }
	public String getReviewDate() { return this.reviewDate; }
	public String[] getCleanReviewBody() { return this.getCleanReviewBody(); }
	
	/* setters for CustomerReviews Class */  
	public void setMarketPlace(String var) { this.marketPlace = var; }
	public void setCustomerId(String var) { this.customerId = var; }
	public void setReviewId(String var) { this.reviewId = var; }
	public void setProductId(String var) { this.productId = var; }
	public void setProductParent(String var) { this.productParent = var; }
	public void setProductTitle(String var) { this.productTitle = var; }
	public void setProductCategory(String var) { this.productCategory = var; }
	public void setStarRating(int var) { if(var >= 1 && var <= 5) this.starRating = var;}
	public void setHelpfulVotes(int var) { if(var > 0) this.helpfulVotes = var; }
	public void setTotalVotes(int var) { if(var > 0) this.totalVotes = var; }
	public void setVine(boolean var) { this.vine = var; }
	public void setVerifiedPurchase(boolean var) { this.verifiedPurchase = var; }
	public void setReviewHeadline(String var) { this.reviewHeadline = var; }
	public void setReviewBody(String var) { this.reviewBody = var; }
	public void setReviewDate(String var) { this.reviewDate = var; }
	public void setCleanReviewBody(String[] var) { if(var != null && var.length > 0 ) this.cleanReviewBody = var;}
	/**
	 * CustomerReview toString method
	 * 
	 * Returns the string representation of CustomerReview. 
	 * Note that not all information is shown. 
	 */
	@Override
	public String toString() {	
		StringBuilder sb = new StringBuilder(); 
		sb.append("{\n"); 
		sb.append("\tproduct_title: ").append(this.getProductTitle()).append("\n"); 
		sb.append("\treview_id: ").append(this.getReviewId()).append("\n"); 
		sb.append("\tproductId: ").append(this.getProductId()).append("\n"); 
		sb.append("\treview_body: ").append(this.getReviewBody()).append("\n"); 
		sb.append("\tclean_review_body: ").append(this.getCleanReviewBody()).append("\n"); 
		sb.append("}"); 
		return sb.toString(); 
	}
	
	/**
	 *  CustomerReviews equals method
	 *  
	 *  If the review_id is the same, then they are the same reviews.
	 *  Otherwise, they are not equal. 
	 *  */  
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CustomerReview) {
			CustomerReview otherObj = (CustomerReview) obj;
			String otherReviewId = otherObj.getReviewId(); 
			return this.getReviewId().equalsIgnoreCase(otherReviewId); 
		} 
		return false;  
	} 	
	
	/**
	 * CustomerReview compareTo method
	 * 
	 * compares using review_id of two CustomerReviews, then    
	 * compares using product_id of two CustomerReviews. 
	 */
	@Override
	public int compareTo(CustomerReview review) {
		 if(this.getReviewId().compareToIgnoreCase(review.getReviewId()) != 0) {
			 return this.getReviewId().compareToIgnoreCase(review.getReviewId());
		 } else {
			 return this.getProductId().compareToIgnoreCase(review.getProductId());
		 } 
	}
	
	/**
	 * Builder class for CustomerReview
	 * One required parameter - reviewId.
	 * Although redundant, also provided a builder method reviewId()
	 * 
	 */
	public static class Builder {
		private String marketPlace = ""; 
		private String customerId = "";  
		private String reviewId = ""; 
		private String productId = ""; 
		private String productParent = "";  
		private String productTitle = "";  
		private String productCategory = "";  
		private int starRating = 0; 
		private int helpfulVotes = 0; 
		private int totalVotes = 0; 
		private boolean vine = false;  
		private boolean verifiedPurchase = false;  
		private String reviewHeadline = "";  
		private String reviewBody = "";  
		private String reviewDate = ""; 
		
		/* constructor of the builder to start off with */ 
		public Builder marketPlace(String var) { this.marketPlace = var; return this; }
		public Builder customerId(String var) { this.customerId = var; return this; }
		public Builder reviewId(String var) { this.reviewId = var; return this; }
		public Builder productId(String var) { this.productId = var; return this; }
		public Builder productParent(String var) { this.productParent = var; return this; }
		public Builder productTitle(String var) { this.productTitle = var; return this; }
		public Builder productCategory(String var) { this.productCategory = var;  return this; }
		public Builder starRating(int var) { this.starRating = var; return this; }
		public Builder helpfulVotes(int var) { this.helpfulVotes = var; return this; }
		public Builder totalVotes(int var) { this.totalVotes = var; return this; }
		public Builder vine(boolean var) { this.vine = var; return this; }
		public Builder verifiedPurchase(boolean var) { this.verifiedPurchase = var; return this; }
		public Builder reviewHeadline(String var) { this.reviewHeadline = var; return this; }
		public Builder reviewBody(String var) { this.reviewBody = var; return this; }
		public Builder reviewDate(String var) { this.reviewDate = var; return this; }
		public CustomerReview build() { return new CustomerReview(this); }
	}
}

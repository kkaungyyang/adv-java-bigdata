/**
 * Custom Exceptions that are found while reading amazon datasets
 * @author kaungyang
 *
 */

public class CustomExceptions {
	
	public class ExpectedLessDataColumnsException extends Exception {
		
		private static final String MESSAGE = "Expected Less Data"; 

		public ExpectedLessDataColumnsException() {
			super(MESSAGE); 
		}

		public ExpectedLessDataColumnsException(String message) {
			super(message); 
		}
	}

}

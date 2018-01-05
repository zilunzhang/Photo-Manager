package exception;

public class InvalidTagNameException extends Exception{

	private static final long serialVersionUID = -401813547616410624L;
	
	/**
	 * Deal with  exception that if tag name is invalid.
	 * 
	 * @param message
	 */
	public InvalidTagNameException(String message){
		super(message);
	}

}

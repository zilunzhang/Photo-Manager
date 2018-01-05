package exception;

public class InvalidPhotoPathException extends Exception{

	private static final long serialVersionUID = 5142896251865732065L;
	
	/**
	 * Deal with  exception that if  photo path is invalid.
	 * 
	 * @param message
	 */
	public InvalidPhotoPathException(String message){
		super(message);
	}
}

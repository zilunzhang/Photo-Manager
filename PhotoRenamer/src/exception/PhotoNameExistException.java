package exception;

public class PhotoNameExistException extends Exception{

	private static final long serialVersionUID = 4567102580542450952L;
	
	/**
	 * Deal with  exception that if  photo name already exists.
	 * 
	 * @param message
	 */
	public PhotoNameExistException(String message){
		super(message);
	}

}

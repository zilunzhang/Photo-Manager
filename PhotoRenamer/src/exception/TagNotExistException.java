package exception;

public class TagNotExistException extends Exception{


	private static final long serialVersionUID = 2632674133476694818L;
	
	/**
	 * Deal with  exception that if  tag does not exist.
	 * 
	 * @param message
	 */
	public TagNotExistException(String message){
		super(message);
	}

}

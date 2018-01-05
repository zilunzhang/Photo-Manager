package exception;

public class DuplicateTagException extends Exception {


	private static final long serialVersionUID = 4062012172154975952L;
	
	/**
	 * Deal with  exception that if tag already exists (Duplicate).
	 * 
	 * @param message
	 */
	public DuplicateTagException(String message){
		super(message);
	}

}

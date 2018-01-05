package exception;

public class EmptyPhotoNameException extends Exception{

	private static final long serialVersionUID = -1280733033395597339L;
	
	/**
	 * Deal with  exception that if  photo name is empty.
	 * 
	 * @param message
	 */
	public EmptyPhotoNameException(String message){
		super(message);
	}
}

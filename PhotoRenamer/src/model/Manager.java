package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract generic class Manager provides necessary
 * functionalities that a manager should have.
 */
public abstract class Manager<T> {
	
	/** The ser file path */
	private String filePath;
	
	/**
	 * Create a Manager object.
	 * 
	 * @param filePath 
	 * 					the ser file path
	 */
	public Manager(String filePath){
		this.filePath = filePath;
	}
	
	
	// Reference from Week 8 in lecture code
	@SuppressWarnings("unchecked")
	/**
	 * Read data and store
	 * @param data
	 * 				The map to store data read from ser file
	 * @return Map<String, T>
	 * 			a map of data that has all the ser file data
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	protected Map<String, T> readFromFile(Map<String, T> data) throws ClassNotFoundException, IOException {
		
		InputStream file = new FileInputStream(filePath);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream(buffer);

		//deserialize the Map
		data = (HashMap<String, T>) input.readObject();
		input.close();
		return data;
	}
	
	
	
	
	// Reference: week 8 Paul's lecture
	/**
	 * 
	 * @param data
	 * 				The data that needs to be wrote to file
	 * @throws IOException
	 */
	public void saveToFile(Map<String, T> data) throws IOException{
		OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(data);
        output.close();
	}
}

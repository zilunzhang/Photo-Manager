package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import exception.InvalidPhotoPathException;

/** PhotoManager class provides several operations including tagging and 
 * renaming on photo(s) that it manages. It keeps track of all photo info
 * and writes it to ser file when called for.  
 */
public class PhotoManager extends Manager<Photo>{
	// Implementation similar to week 8 Paul's lecture code: class StudentManager
	// Citation: http://www.cdf.toronto.edu/~csc207h/fall/lectures.shtml
	
	/** Logger instance for logging use  */
	private static final Logger logger = 
			Logger.getLogger(PhotoManager.class.getName());

	
	/**
	 * A mapping from the photo's path to photo
	 */
	private Map<String, Photo> photos;
	
	/** This manager's ser file path */
	private String filePath;
	
	
	
	/**
	 * Create a photoManager that keeps track of a bunch of photos 
	 * and writes and reads Photo object from given ser file path
	 * (It will produce a ser file if no file found in given path)
	 * 
	 * @param filePath
	 * 					file's path.
	 * 
	 * @throws ClassNotFoundException
	 * 
	 * @throws IOException
	 */
	public PhotoManager(String filePath) throws ClassNotFoundException, IOException{
		super(filePath);
		this.filePath = filePath;
		photos = new HashMap<String, Photo>();
		
		// Initialize logger
		logger.setLevel(Level.FINER);
		FileHandler fh = new FileHandler("Photo.log", true);
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		
		File file = new File(filePath);
		if(file.exists()){
			readFromFile();
		}else{
			file.createNewFile();
		}
	}
	
	
	// Reference: Week 8 lecture code
	@SuppressWarnings("unchecked")
	/**
	 * Reads photo object from ser file at given filePath.
	 * 
	 * @param filePath
	 * 					file's path.
	 * 
	 * @throws ClassNotFoundException
	 */
	private void readFromFile() throws ClassNotFoundException{
		try {
			photos = super.readFromFile(photos);
	    } 
	    catch (IOException e) {
	    	logger.log(Level.SEVERE, "Cannot read Photo obeject from file.", e);
	    }
	}
	
	
	
	
	
	// Reference: week 8 Paul's lecture
	/**
	 * Write photo object to file at the given filePath.
	 * 
	 * @param filePath
	 * 					file's path.
	 * 
	 * @throws IOException
	 */
	public void saveToFile() throws IOException{
		super.saveToFile(photos);
		logger.log(Level.FINE, "Write tag object to file");
	}
	
	
	
	
	
	/**
	 * Close all handler that it's logger have
	 */
	public void closeHandler(){
		for (Handler f: logger.getHandlers())
			f.close();
	} 
	
	
	
	/**
	 * Add a new photo to be kept track of.
	 * If the photo doesn't contain in photos before.
	 * 
	 * @param photo
	 * 				photo to add.
	 */				
	public void addPhoto(Photo photo){
		if(!photos.containsKey(photo.getPath()))
			photos.put(photo.getPath(), photo);
	}
	
	
	
	
	
	/**
	 * Find the photo that is being track of at the given path.
	 * Throw InvalidPhotoPathException when photo's path is invalid.
	 * 
	 * @param path
	 * 				photo's path.
	 * 
	 * @return	Photo
	 * 				at the path.
	 * 
	 * @throws InvalidPhotoPathException
	 * 										when photo's path is invalid.
	 */
	public Photo findPhoto(String path) throws InvalidPhotoPathException{
		if(isPathValid(path)){
			return photos.get(path);			
		}else{
			return null;
		}
	}
	
	
	
	
	
	/**
	 * Get all Photos that this Manager is managing.
	 * 
	 * @return Collection <Photo>
	 * 							the collection form of photo in photos
	 */
	public Collection<Photo> getPhotos(){
		return photos.values();
	}
	
	
	
	
	
	/**
	 * Remove photo at the given path so that it will no longer be tracked.
	 * Throw InvalidPhotoPathException when photo's path is invalid.
	 * 
	 * @param path
	 * 				photo's path.
	 * 
	 * @throws InvalidPhotoPathException 
	 * 				if photo's path is invalid.
	 */
	public void removePhoto(String path) throws InvalidPhotoPathException{
		if(photos.containsKey(path)){
			photos.remove(path);
		}
	}
	
	
	
	
	/**
	 * Set the name of photo at given path to this given name. 
	 * Throw InvalidPhotoPathException if the given path is invalid.
	 * 
	 * @param path
	 * 				photo's path.
	 * 
	 * @param name
	 * 				photo's name.
	 * 
	 * @throws InvalidPhotoPathException
	 * 				if photo's path is invalid.
	 * 	
	 * @throws IOException
	 */
	// Taken from StackOverflow and reformed to fit our code
	// http://stackoverflow.com/questions/1158777/rename-a-file-using-java
	private void setPhotoName(String path, String name) 
			throws InvalidPhotoPathException, IOException{
		if(isPathValid(path)){
			// Do the renaming step
			Photo photo = photos.get(path);
			String oldName = photo.getName();
			Path source = Paths.get(photo.getPath());
			Path target = source.resolveSibling(name);
			Files.move(source, target);
			
			// Photo has been renamed on OS level
			// Need to update this curr photo info
			String newPath = target.toString();
			photo.updateInfo(name, newPath);
			photos.remove(path);
			photos.put(newPath, photo);
			
			//log info
			logger.log(Level.INFO, "Rename Photo from:" + oldName + " to:" + name);
			saveToFile();
		}
	}
	
	
	
	
	
	/**
	 * Remove tag from the photo at given path.
	 * Note that this will reset this Photo's name
	 * Throw InvalidPhotoPathException when photo's path is invalid.
	 * 
	 * @param path
	 * 				photo's path.
	 * 
	 * @param tag
	 * 				the tag to remove.
	 * 
	 * @throws InvalidPhotoPathException 
	 * 				if photo's path is invalid.
	 * 
	 * @throws IOException
	 */
	public void removeTag(String path, Tag tag) 
			throws InvalidPhotoPathException, IOException{
		if(isPathValid(path)){
			Photo photo = photos.get(path);
			photo.removeTag(tag);
			tag.removePhoto(photo);
			String newName = photo.getName().replaceAll(tag.toString(), "");
			setPhotoName(path, newName);
		}
	}
	
	
	
	
	
	/**
	 * Add tag to the photo at given path.
	 * Note that this will reset photo's name.
	 * Throw InvalidPhotoPathException when photo's path is invalid.
	 * 
	 * @param path
	 * 				photo's path.
	 * 
	 * @param tag
	 * 				the tag to add.
	 * 
	 * @throws InvalidPhotoPathException
	 * 				if photo's path is invalid.
	 * 
	 * @throws IOException
	 */
	public void addTag(String path, Tag tag) 
			throws InvalidPhotoPathException, IOException{
		if(isPathValid(path)){
			Photo photo = photos.get(path);
			if(!photo.getTags().contains(tag)){
				photo.addTag(tag);
				String newName = tag.toString() + photo.getName();
				setPhotoName(path, newName);
			}
		}
	}
	
	
	
	/**
	 * Find the photo that has most many tags.
	 * 
	 * @return Photo
	 * 					photo that has most many tags.
	 */
	public Photo findMostTaggedPhoto(){
		Photo result = null;
		for(Photo p: photos.values()){
			if(result == null)
				result = p;
			else{
				if(p.getTags().size() >= result.getTags().size())
					result = p;
			}
		}
		return result;
	}
	
	
	
	/**
	 * Remove the given tag from all photos.
	 * Note that this will reset those photos' name.
	 * Throw InvalidPhotoPathException when photo's path is invalid.
	 * 
	 * @param tag
	 * 				the tag to remove from all.
	 * 
	 * @throws InvalidPhotoPathException
	 * 				if photo's path is invalid.
	 * 
	 * @throws IOException
	 */
	public void removeTagForAll(Tag tag) 
			throws InvalidPhotoPathException, IOException{
		for(Photo p: tag.getPhotos()){
			this.removeTag(p.getPath(), tag);
		}
	}
	
	/**
	 * Revert photo's name to a this given old name.
	 * Throw InvalidPhotoPathException when photo's path is invalid.
	 * Set photo's name.
	 * Set photo's tags.
	 * @param path
	 * 				photo's path.
	 * 
	 * @param oldName
	 * 				photo's oldName to revert.
	 * 
	 * @throws InvalidPhotoPathException
	 * 				if photo's path is invalid.
	 * 
	 * @throws IOException
	 */
	public void revertToPreName(String path, String oldName) 
			throws InvalidPhotoPathException, IOException{
		Photo p = findPhoto(path);
		ArrayList<String> newPreNames = new ArrayList<>();
		for(String preName: p.getPreviousName()){
			if(!preName.equals(oldName)){
				newPreNames.add(preName);
			}else{
				break;
			}
		}
		p.setPreviousName(newPreNames);
		setPhotoName(path, oldName);
	}
	
	
	
	
	
	@Override
    /**
     * Return a String representation of name for all photo 
     * in a line by line form.
     * 
     * @return String
     * 					a String representation of this photo.
     */
	public String toString(){
		String result = "";
		for(Photo p : photos.values()){
			result = p.toString() + "\n";
		}
		return result;
	}
	
	/**
	 * Check if the given path is valid, i.e. path not empty and the
	 * photo of this path is being tracked.
	 * 
	 * @param path 
	 * 				a photo's path
	 * @return boolean
	 * 				True if path is valid, otherwise false.
	 * 
	 * @throws InvalidPhotoPathException
	 * 				if photo's path is invalid
	 */
	private boolean isPathValid(String path) throws InvalidPhotoPathException{
		boolean result = true;
		if(!photos.containsKey(path)){
			result = false;
			logger.log(Level.SEVERE, "Path doesn't match, cannot find such photo");
			throw new InvalidPhotoPathException("Path does not matxh!");
		}
		return result;
	}
	
}

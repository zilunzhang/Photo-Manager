package model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import exception.DuplicateTagException;
import exception.InvalidTagNameException;
import exception.TagNotExistException;

/** TagManager class provides several operations including 
 * adding and removing a photo from tag(s) and that it manages. 
 * It keeps track of all tag info and writes it to ser file 
 * when called for. 
 */
public class TagManager extends Manager<Tag>{
	// Implementation referenced from week 8 Paul's lecture code: class StudentManager
	// Citation: http://www.cdf.toronto.edu/~csc207h/fall/lectures.shtml
	
	/**
	 * A mapping from tag names to tag.
	 */
	private Map<String, Tag> tags;

	
	/** Logger instance for logging use */
	private static final Logger logger = 
			Logger.getLogger(TagManager.class.getName());
	
	/** This manager's ser file path*/
	private String filePath;
	

	/**
	 * Create a TagManager that keeps track of a bunch of tags and 
	 * writes and reads tag object from given ser file path.
	 * (It will produce a ser file if no file found in given path)
	 * 
	 * @param filePath
	 * 					file's path
	 * 
	 * @throws ClassNotFoundException
	 * 								
	 * @throws IOException	
	 * 						
	 */
	public TagManager(String filePath) 
			throws ClassNotFoundException, IOException{
		super(filePath);
		this.filePath = filePath;
		tags = new HashMap<String, Tag>();
		logger.setLevel(Level.FINER);
		FileHandler fh = new FileHandler("Tag.log", true);
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		
		File file = new File(filePath);
		if(file.exists()){
			readFromFile();
		}else{
			file.createNewFile();
		}
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	/**
	 * Reads photo object from ser file at given filePath.
	 * 
	 * @param filePath
	 * 					file's path
	 * 
	 * @throws ClassNotFoundException
	 * 								
	 */
	private void readFromFile() throws ClassNotFoundException {
		try {
			tags = super.readFromFile(tags);
	    } 
	    catch (IOException e) {
	    	logger.log(Level.SEVERE, "Cannot read Tag obeject from file.", e);
	    }
		
	}
	
	
	
	
	/**
	 * Write tag object to file at the given filePath.
	 * log what we do.
	 * 
	 * @param filePath
	 * 					file's path
	 * 
	 * @throws IOException
	 */
	public void saveToFile() throws IOException{
		super.saveToFile(tags);
        logger.log(Level.FINE, "Write Tag obeject to file");
	}
	
	
	
	
	/**
	 * Close all the Handler that this class logger have.
	 */
	public void closeHandler(){
		for (Handler f: logger.getHandlers())
			f.close();
	} 
	
	
	
	
	
	/**
	 * Find tag represented by the given name.
	 * 
	 * @param name
	 * 				tag's name.
	 * 
	 * @return Tag
	 * 				the tag in tags before with corresponding name.
	 */
	public Tag findTag(String name){
		return tags.get(name);
	}
	
	
	
	
	
	/**
	 * Get all tags that this manager is managing.
	 * 
	 * @return Collection <Tag>
	 * 							the collection of tags
	 */
	public Collection<Tag> getTags(){
		return tags.values();
	}
	
	
	
	
	
	/**
	 * Create a new tag based on given name.
	 * Throw DuplicateTagException when new tag's name already exist in tags.
	 * Throw InvalidTagNameException when new tag's name is invalid.
	 * 
	 * @param name
	 * 				new tag's name for creating new tag.
	 * 
	 * @return Tag
	 * 				new tag to be created.
	 * 
	 * @throws DuplicateTagException
	 * 				if new tag's name already exist in tags
	 * 
	 * @throws InvalidTagNameException
	 * 				if new tag's name is invalid.
	 */
	public Tag createTag(String name) 
			throws DuplicateTagException, InvalidTagNameException{
		if(isNameValid(name) && !tags.containsKey(name)){
			Tag newTag = new Tag(name);
			tags.put(name, newTag);
			logger.log(Level.INFO, "Create a new tag:" + name);
			return newTag;
		}else{
			// Name valid test not throwing exception and 
			// tags contains this name, must be duplicate tag
			logger.log(Level.INFO, "Create a duplicate tag.");
			throw new DuplicateTagException("Tag name already exist!");
		}
	}
	
	
	
	
	
	/**
	 * Add photo that contains given tag name to the corresponding tag. 
	 * Throw InvalidTagNameException when tag's name is invalid.
	 * Throw TagNotExistException when tag does not exist.
	 * 
	 * @param name
	 * 				name of tag.
	 * 
	 * @param photo
	 * 				photo to add.
	 * 
	 * @throws InvalidTagNameException
	 * 				if new tag's name is invalid.
	 * 
	 * @throws TagNotExistException
	 * 				if tag does not exist.
	 * @throws DuplicateTagException 
	 * 				if this photo is already tagged by such tag
	 */
	public void addPhoto(String name, Photo photo) 
			throws InvalidTagNameException, TagNotExistException, DuplicateTagException{
		if(isNameValid(name) && isTagExist(name)){
			Tag tag = tags.get(name);
			if(tag.getPhotos().contains(photo)){
				throw new DuplicateTagException("This photo is already tagged!");
			}
			tag.addPhoto(photo);
		}
	}
	
	
	
	
	
	/**
	 * Remove photo from the tag's tagging list.
	 * Throw InvalidTagNameException when tag's name is invalid.
	 * Throw TagNotExistException when tag does not exist.
	 * 
	 * @param name
	 * 				name of tag.
	 * 
	 * @param photo
	 * 				photo to remove.
	 * 
	 * @throws InvalidTagNameException
	 * 				if new tag's name is invalid.
	 * 
	 * @throws TagNotExistException
	 *				if tag does not exist.
	 */
	public void removePhoto(String name, Photo photo)
			throws InvalidTagNameException, TagNotExistException{
		if(isNameValid(name) && isTagExist(name)){
			Tag tag = tags.get(name);
			tag.removePhoto(photo);
		}
	}
	

	
	/**
	 * Remove the tag from management, no longer keep track of this tag.
	 * Throw InvalidTagNameException when tag's name is invalid.
	 * Throw TagNotExistException when tag does not exist.
	 * 
	 * @param name
	 * 				name of tag.
	 * 
	 * @throws InvalidTagNameException
	 * 				if new tag's name is invalid.
	 * @throws TagNotExistException
	 * 				if tag does not exist.
	 */
	public void deleteTag(String name) 
			throws InvalidTagNameException, TagNotExistException{
		if(isNameValid(name) && isTagExist(name)){			
			tags.remove(name);
			logger.log(Level.SEVERE, "Delete a tag:" + name);
		}
	}
	
	
	
	/**
	 * Find the mostly tagged Tag
	 * 
	 * @return Tag
	 * 				that is mostly tagged.
	 */
	public Tag findMostCommonTag(){
		Tag result = null;
		for(Tag t: tags.values()){
			if (result == null)
				result = t;
			else{
				if(t.getPhotos().size() >= result.getPhotos().size())
					result = t;
			}
		}
		return result;
	}
	
	/**
	 * Remove all tag from the given photo
	 * @param photo
	 * 				the photo that need to remove tag
	 */
	public void removeAllTag(Photo photo){
		for(Tag t: photo.getTags()){
			t.removePhoto(photo);
		}
	}
	
	
	/**
	 * Add all tag based on given photo's tag list
	 * 
	 * @param photo
	 * 				the photo that need to add tag
	 */
	public void addAllTag(Photo photo){
		for(Tag t: photo.getTags()){
			t.addPhoto(photo);
		}
	}
	
	
	/**
	 * Check if the given name is valid, i.e. name not empty and the
	 * tag of this name is being tracked
	 * @param name
	 * 				a tag's name
	 * @return boolean
	 * 				true if given name is valid, false otherwise.
	 * @throws InvalidTagNameException
	 * 				if tag name is invalid
	 */
	public boolean isNameValid(String name) throws InvalidTagNameException{
		boolean result = true;
		if(name == null | name.isEmpty()){
			result = false;
			logger.log(Level.INFO, "Operating an empty tag.");
			throw new InvalidTagNameException("Tag name is empty!");
		}
		return result;
	}
	
	/**
	 * Check if the given tag exists, i.e. being track of.
	 * @param name 
	 * 				a tag's name
	 * @return boolean
	 * 				true if the tag exist, false otherwise
	 * 
	 * @throws TagNotExistException
	 * 				if such tag does not exist
	 */
	public boolean isTagExist(String name) throws TagNotExistException{
		boolean result = true;
		if(!tags.containsKey(name)){
			result = false;
			logger.log(Level.INFO, "Operating an non-existing tag");
			throw new TagNotExistException("Such tag does not exist!");
		}
		return result;
	}
}

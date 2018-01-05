package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import exception.DuplicateTagException;
import exception.InvalidPhotoPathException;
import exception.InvalidTagNameException;
import exception.TagNotExistException;
import util.Thumbnail;

/**
 * A class that deals with all the Manager,
 * behaving like a mediator as it provides functionality 
 * that requires all(or maybe some) Managers' joint work.
 */
public class Mediator {
	
	// Use Mediator Design Pattern and Singleton Design Pattern
	// Mediator design pattern implement not exactly same as template 
	// uml or crc, namely I skip the abstract mediator class and 
	// use this mediator as the only concrete general mediator since the 
	// whole project structure is relatively simple enough such that 
	// one concrete mediator all there requires.
	
	/** A TagManager to mediate */
	private TagManager tManager;
	
	/** A PhotoManager to mediate */
	private PhotoManager pManager;
	
	/** Singleton instance*/
	private static Mediator instance = null;
	
	/**
	 * Create a Mediator object based on given 
	 * tagManager, photoManager.
	 * 
	 * @param tManager 
	 * 				the tag manager that is going to be dealt with.
	 * @param pManager
	 * 				the photo manager that is going to be dealt with.
	 */
	private Mediator(TagManager tManager, PhotoManager pManager){
		this.tManager = tManager;
		this.pManager = pManager;
	}
	
	/**
	 * Get the singleton instance of Mediator.
	 * 
	 * @param tManager 
	 * 				the tag manager that is going to be dealt with.
	 * @param pManager
	 * 				the photo manager that is going to be dealt with.
	 * 
	 * @return Mediator
	 * 				a instance of mediator(the single one)  
	 */
	public static Mediator getInstance(TagManager tManager, PhotoManager pManager) {
		if(instance == null)
			instance = new Mediator(tManager, pManager);
		return instance;
	}
	
	/**
	 * Add a new tag to the given photo.
	 * Note that this will create a new tag and reset the photo name.
	 * 
	 * @param photo
	 * 				the photo that need to add tag
	 * @param tagName
	 * 				the tag that add to given photo
	 * @throws DuplicateTagException
	 * 				if given photo has been tagged by such tag
	 * @throws InvalidTagNameException
	 * 				if tag name is invalid
	 * @throws TagNotExistException
	 * 				if such tag doesn't exist
	 * @throws InvalidPhotoPathException
	 * 				if photo path is invalid
	 * @throws IOException
	 */
	public void addNewTagToPhoto(Photo photo, String tagName) 
			throws DuplicateTagException, InvalidTagNameException, 
			TagNotExistException, InvalidPhotoPathException, IOException{
		
		String path = photo.getPath();
		Tag tag = tManager.createTag(tagName);
		pManager.addTag(path, tag);
		tManager.addPhoto(tagName, photo);
	}
	
	/**
	 * Add a existing tag to the given photo.
	 * Note that this will reset the photo name.
	 * @param photo
	 * 				the photo that needs to add tag
	 * @param tagName
	 * 				name of the tag that is going to be added to this photo
	 * @throws InvalidPhotoPathException
	 * 				if photo path is invalid
	 * @throws InvalidTagNameException
	 * 				if tag name is invalid
	 * @throws TagNotExistException
	 * 				if such tag does not exist
	 * @throws DuplicateTagException
	 * 				if this photo has been tagged by such tag
	 * @throws IOException
	 */
	public void addExistingTagToPhoto(Photo photo, String tagName) 
			throws InvalidPhotoPathException, IOException, InvalidTagNameException, 
			TagNotExistException, DuplicateTagException{
		
		String path = photo.getPath();
		Tag tag = tManager.findTag(tagName);
		pManager.addTag(path, tag);
		tManager.addPhoto(tagName, photo);
	}
	
	/**
	 * Remove the given tag from given photo.
	 * Note that this will reset the photo name.
	 * 
	 * @param photo 
	 * 				the photo that needs to remove tag
	 * @param tagName 
	 * 				the tag name that is going to be removed
	 * @throws InvalidPhotoPathException
	 * 				if photo path is invalid
	 * @throws InvalidTagNameException
	 * 				if tag name is invalid
	 * @throws TagNotExistException
	 * 				if such tag does not exist
	 * @throws IOException
	 */
	public void removeTagFromPhoto(Photo photo, String tagName) 
			throws InvalidPhotoPathException, IOException, 
			InvalidTagNameException, TagNotExistException{
		
		String path = photo.getPath();
		Tag tag = tManager.findTag(tagName);
		pManager.removeTag(path, tag);
		tManager.removePhoto(tagName, photo);
	}
	
	
	/**
	 * Delete the given tag so that it will no longer be tracked.
	 * Note that this will rename all photos that had been tagged
	 * by this tag.
	 *  
	 * @param tagName
	 * 				name of the tag that needs to be delete
	 * @throws InvalidPhotoPathException
	 * 				if photo path is invalid
	 * @throws InvalidTagNameException
	 * 				if tag name is invalid
	 * @throws TagNotExistException
	 * 				if such tag does not exsist
	 * @throws IOException
	 */
	public void deleteTag(String tagName) 
			throws InvalidPhotoPathException, IOException, 
			InvalidTagNameException, TagNotExistException{
		
		Tag tag = tManager.findTag(tagName);
		pManager.removeTagForAll(tag);
		tManager.deleteTag(tagName);
	}
	
	/**
	 * Revert the given photo's name to given old name.
	 * @param photo 
	 * 					the photo to revert
	 * @param oldName 
	 * 					the photo's old name
	 * @throws InvalidTagNameException 
	 * 					if tag name is invalid
	 * @throws TagNotExistException
	 * 					if tag not exist
	 * @throws DuplicateTagException
	 * 					if tag already existed or photo has been tagged
	 * @throws InvalidPhotoPathException
	 * 					if photo path is invalid
	 * @throws IOException
	 */
	public void revertToPreName(Photo photo, String oldName) 
			throws InvalidTagNameException, TagNotExistException, 
			DuplicateTagException, InvalidPhotoPathException, IOException {
		
		if(oldName.equals(photo.getName()))
			return;
		
		for(Tag t: photo.getTags()){
			t.removePhoto(photo);
		}
		
		photo.setTags(new ArrayList<Tag>());
		String[] arrayName = oldName.split(" ");
		for(String s: arrayName){
			if(s.startsWith(Tag.prefix)){
				String tagName = s.substring(1);
				if(tManager.isTagExist(tagName)){
					tManager.findTag(tagName).addPhoto(photo);
					photo.addTag(tManager.findTag(tagName));
				}else{
					Tag newTag = tManager.createTag(tagName);
					tManager.addPhoto(tagName, photo);
					photo.addTag(newTag);
				}	
			}
		}
		pManager.revertToPreName(photo.getPath(), oldName);
	}
	
	/**
	 * Get the renaming log in a string list. 
	 * 
	 * @return List<String>
	 * 			list of string that contains logging history. 
	 * @throws IOException
	 */
	public List<String> getNamingLogList() throws IOException{
		List<String> lines = Files.readAllLines(Paths.get("Photo.log"));
		return lines;
	}
	public Collection<String> getPreviousNames(Photo photo){
		return photo.getPreviousName();
	}
	
	
	/**
	 * Get all photo's thumbnail grouping as a list.
	 * 
	 * @return list of Thumbnail
	 */
	public List<Thumbnail> getThumbList(){
		List<Thumbnail> result = new ArrayList<Thumbnail>();
		for(Photo p: pManager.getPhotos()){
			result.add(p.getThumb());
		}
		return result;
	}
	
	/**
	 * Get all the tags that are currently being tracked of
	 * 
	 * @return Collection<Tag>
	 * 				the collection of tags
	 */
	public Collection<Tag> getAllTags(){
		return tManager.getTags();
	}
	
	/**
	 * Create a tag, not tagging to any photo currently.
	 * 
	 * @param name
	 * 			name of the tag to be created
	 * @throws DuplicateTagException
	 * 			if same tag already exists 
	 * @throws InvalidTagNameException
	 * 			if tag name is invalid
	 */
	public Tag getCreateTag(String name) throws DuplicateTagException, InvalidTagNameException{
		return tManager.createTag(name);
		
	}
	
	public Tag findTag(String name) {
		return tManager.findTag(name);
	}
	
	
	/**
	 * Close all managers that this mediator is dealing with
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException{
		pManager.saveToFile();
		tManager.saveToFile();
		pManager.closeHandler();
		tManager.closeHandler();
	}
	
}

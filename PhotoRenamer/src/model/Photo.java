package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import util.Thumbnail;

// import directory_explorer.buildFileTree;
	
/** Photo class represent a image file and keep
*  track of necessary file information. 
*/
public class Photo implements Serializable{


	private static final long serialVersionUID = 6955723612371190680L;
	
	/** The Collection of photo's previous names, which represent by collection of strings. */
	private Collection<String> previousName;
	
	/** The set of Tags that photo has.*/
	private ArrayList<Tag> tags;
	
	/** Photo's filename*/
	private String name;
	
	/** Photo's absolute path*/
	private String path;
	
	/** Photo's file */
	private File file;
	
	/** Photo's corresponding Thumbnail object */
	private Thumbnail thumb;
	
	
	
	/**
	 * Create a Photo object based on given information.
	 *
	 * @param name
	 *            Photo's filename.
	 * @param path
	 *            Photo's absolute path.
	 * @param file
	 *            This Photo's file.
	 * @throws IOException 
	 */
	public Photo(String name, String path, File file) throws IOException{
		this.name = name;
		this.path = path;
		this.file = file;
		thumb = new Thumbnail(file, this);
		previousName = new ArrayList<String>();
		tags = new ArrayList<Tag>();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	/**
	 * hashCode.
	 * 
	 * @return integer
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((previousName == null) ? 0 : previousName.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	/**
	 * If equal.
	 * 
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (previousName == null) {
			if (other.previousName != null)
				return false;
		} else if (!previousName.equals(other.previousName))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} 
		return true;
	}






	/**
	 * Return the path of the photo.
	 * 
	 * @return path
	 * 				the path of the photo.
	 */
	public String getPath() {
		return path;
	}



	
	
	/**
	 * Set the path of the Photo.
	 *
	 * @param path
	 *            	the path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	
	


	/**
	 * Return this photo's previousNames 
	 * (As Collection Of Strings).
	 * 
	 * @return the previousNames.
	 */
	public Collection<String> getPreviousName() {
		return previousName;
	}
	
	

	/**
	 * Set this photo's previous name
	 *  
	 * @param previousName the previousName to set
	 */
	public void setPreviousName(Collection<String> previousName) {
		this.previousName = previousName;
	}


	/**
	 * Return photo's file.
	 * 
	 * @return  file
	 * 				  this photo's file.  
	 */
	public File getFile() {
		return file;
	}

	
	
	
	
	/**
	 * Set the file of this photo.
	 * 
	 * @param file 
	 * 				the file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	
	
	
	
	/**
	 * Get the name of this photo.
	 * 
	 * @return name
	 * 				the photo's name.
	 */
	public String getName() {
		return name;
	}
	
	
	
	

	/**
	 * Set the name of this photo.
	 * 
	 * @param name 
	 * 				the name to set.
	 */
	public void setName(String newName) {
		previousName.add(this.name);
		this.name = newName;
	}
	
	
	
	
	
    /**
     * Return a String representation of the photo of this
     * form:
     * 
     * "super's toString, name"
     *
     * @return String 
     * 					a String representation of this photo.
     */
	@Override
	public String toString(){
		return super.toString() + ", " + this.name;
	}
	
	
	
	

	/**
	 * Return the set of this photo's Tags. 
	 * 
	 * @return tags
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	
	
	

	/**
	 * Set the tags of this photo.
	 * 
	 * @param tags 
	 * 				the tags to set
	 */
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	
	

	/**
	 * Get this photo's thumbnail
	 * 
	 * @return the thumb
	 */
	public Thumbnail getThumb() {
		return thumb;
	}


	/**
	 * Set the thumbnail of this photo
	 * 
	 * @param thumb the thumb to set
	 */
	public void setThumb(Thumbnail thumb) {
		this.thumb = thumb;
	}


	public BufferedImage getThumbImage() throws IOException{
		return thumb.readThumbImage();
	}
	
	
	/**
	 * Remove the tag from this photo,
	 * if this photo has the tag.
	 * 
	 * @param tags 
	 * 				the tag to remove.
	 */
	public void removeTag(Tag tag){
		if(tags.contains(tag)){
			tags.remove(tag);
		}
	}
	
	
	/**
	 * Add the tag to this photo,
	 * if this photo doesn't have the tag before.
	 * 
	 * @param tags 
	 * 				the tag to add.
	 */
	public void addTag(Tag tag){
		tags.add(tag);
	}

	
	/**
	 * Update all info of this photo, 
	 * including photo's name, path and file info.
	 * 
	 * @param name
	 * 				the new name to update.
	 * 
	 * @param path
	 * 				the new path to update.
	 */
	public void updateInfo(String name, String path){
		setFile(new File(path));
		setName(name);
		setPath(path);
	}
	
}

package model;

import java.io.Serializable;
import java.util.ArrayList;


/** Tag class represents a photo's tag */
public class Tag implements Serializable{

	private static final long serialVersionUID = 3430529772963736249L;
	
	/**
	 * The prefix "@" for each tag
	 */
	public static final String prefix = "@";
	
	/** The name of tag */
	private String name;
	
	/** The list of photos that share this tag */
	private ArrayList<Photo> photos;
	

	
	

	/**
	 * Create a Tag based on the given name.
	 *
	 * @param name
	 *            tag's name.
	 */
	public Tag(String name){
		this.name = name;
		photos = new ArrayList<Photo>();
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((photos == null) ? 0 : photos.hashCode());
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
		Tag other = (Tag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (photos == null) {
			if (other.photos != null)
				return false;
		} 
		return true;
	}

	



	/**
	 * Return name of the tag.
	 * 
	 * @return name
	 * 				the name of the tag.
	 */
	public String getName() {
		return name;
	}

	
	
	
	
	/**
	 * Set the name of the tag.
	 * 
	 * @param name 
	 * 				the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
	/**
	 * Return a list of photos that contain the tag.
	 * 
	 * @return the photos
	 * 						the photos contain the tag.
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	
	
	
	
	/**
	 * Add photo to the this tag's list of photos
	 * 
	 * @param photo
	 * 				the photo to add.
	 */
	public void addPhoto(Photo photo){
			photos.add(photo);
		}
	
	
	
	
	
	/**
	 * Remove photo from this tag's photo list,
	 * i.e. photo is not tagged by this tag anymore.
	 * 
	 * @param photo
	 * 				the photo to remove.
	 */
	public void removePhoto(Photo photo){
		if(photos.contains(photo)){
			photos.remove(photo);
		}
	}
	
	
	
	
	
    /**
     * Return a String representation of the tag of this
     * form:
     * "@TagName "
     *
     * @return a String representation of this tag.
     */
	@Override
	public String toString(){
		return Tag.prefix + name + " ";
	}
	
	
}

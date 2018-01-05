package model;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;


/** PhotoLoader class helps load all photos 
 *  everywhere under the directory into a PhotoManager 
 */
public class PhotoLoader {
	
	
	/**
	 * Load all photo files under given selected directory into photo manager. 
	 * 
	 * @param manager
	 *            	the PhotoManager we are building.
	 * @param file
	 *            	the directory where we load photos from.
	 * @throws IOException 
	 */
	public static void loadPhotos(PhotoManager pManager, File file) throws IOException{
		// Idea taken from A1-part2-buildTree method
		File[] fileList = file.listFiles();
		for (File f: fileList){
			if(f.isDirectory())
				loadPhotos(pManager, f);
			else{
				// Identify image file, taken from StackOverflow
				// Citation: http://stackoverflow.com/questions/9643228/test-if-file-is-an-image
				String mimeType = new MimetypesFileTypeMap().getContentType(f); 
				String type = mimeType.split("/")[0];
		        if(type.equals("image")){
		        	Photo p = new Photo(f.getName(), f.getAbsolutePath(), f);
		        	pManager.addPhoto(p);
		        }
			}
		}
	}
}

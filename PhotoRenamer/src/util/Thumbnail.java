package util;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import model.Photo;

/**
 * Create a thumbnail (small) image from a full image. The thumbnail will be
 * written to a specified directory. A future modification could be to save the
 * thumbnail in memory.
 * 
 */

public class Thumbnail implements Serializable{

	/** Auto-generated ser ID */
	private static final long serialVersionUID = -6264726831222761443L;
	
	/** this thumbnail's file path */
	private String path;
	
	/** this thumbnail's corresponding photo */
	private Photo photo;
	
	/**
	 * Create a thumbnail object
	 * 
	 * @param f
	 * 			the photo file that needs a thumbnail
	 * @param photo
	 * 			this thumbnail's corresponding photo object
	 * @throws IOException
	 */
	public Thumbnail(File f, Photo photo) throws IOException{
		this.photo = photo;
		writeThumb(f);
	}
		
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Thumbnail other = (Thumbnail) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		return true;
	}


	/**
	 * Get this thumbnail's path
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	
	
	/**
	 * Set this thumbnail's path
	 * 
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Get this thumbnail's corresponding photo
	 * 
	 * @return the photo
	 */
	public Photo getPhoto() {
		return photo;
	}


	/**
	 * Set this thumbnail's corresponding photo
	 * 
	 * @param photo the photo to set
	 */
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	
	/**
	 * Read thumbnail file into memory
	 * 
	 * @return result
	 * 			the BufferedImage of thumbnail file
	 * @throws IOException
	 */
	public BufferedImage readThumbImage() throws IOException{
		BufferedImage result = ImageIO.read(new File(this.path));
		return result;
	}
	
		
		
	//Taken from StackOverflow
	//Url: http://stackoverflow.com/questions/30731045/generate-thumbnail-in-java
	/**
	 * 
	 * @param in
	 * 			the image file's BufferedImage
	 * @param w
	 * 			width of the thumbnail to be created
	 * @param h
	 * 			height of the thumbnail to be crated
	 * @return bi
	 * 			the created thumbnail's BufferedImage
	 */
	private BufferedImage createThumb(BufferedImage in, int w, int h) {
		// scale w, h to keep aspect constant
		double outputAspect = 1.0*w/h;
		double inputAspect = 1.0*in.getWidth()/in.getHeight();
		if (outputAspect < inputAspect) {
			// width is limiting factor; adjust height to keep aspect
		    h = (int)(w/inputAspect);
		} else {
		    // height is limiting factor; adjust width to keep aspect
		    w = (int)(h*inputAspect);
		}
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(in, 0, 0, w, h, null);
		g2.dispose();
		return bi;
	}
	
	
	
	//Taken from StackOverflow
	//Url: http://stackoverflow.com/questions/30731045/generate-thumbnail-in-java
	/**
	 * Write the image file f's thumbnail to hard drive.
	 * 
	 * @param f
	 * 			the image file that need a thumbnail
	 * 
	 * @throws IOException
	 */
	public void writeThumb(File f) throws IOException{
		String in = f.getAbsolutePath();
		String name = f.getName();
		String extension = in.substring(in.lastIndexOf(".")+1);
		name = name.replaceAll(extension, "");
		new File("thumbnails").mkdir();
        BufferedImage bi = createThumb(
        		ImageIO.read(Files.newInputStream(Paths.get(in))), 128, 128);


        Path out = Paths.get("thumbnails/" + name + "_thumb." + extension);

        setPath(out.toString());
        ImageIO.write(bi, extension, Files.newOutputStream(out));
		}
	}

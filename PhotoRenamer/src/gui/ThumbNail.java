package gui;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import model.Mediator;
import model.Photo;
import gui.MainWindow;

public class ThumbNail extends JLabel implements MouseListener {
	
	private Photo photo;
	private MainWindow mainWindow;
	private Mediator mediator;

	/**
	 * The ThumbNail
	 *  
	 * @param photo
	 * 			photo that pass in to make ThumbNail
	 * 
	 * @param mainWindow
	 * 			main window that pass in to display 
	 *  
	 * @param mediator
	 * 			mediator that pass in.
	 * 
	 * @throws IOException
	 */
	public ThumbNail(Photo photo, MainWindow mainWindow, Mediator mediator) throws IOException {

		super(new ImageIcon(photo.getThumbImage()));
		this.photo = photo; 
		this.mainWindow = mainWindow; 
		this.addMouseListener(this);
		this.mediator = mediator;
	}
	
	
	
	/**
	 * Get photo that pass in
	 * 
	 * @return photo
	 * 				the photo pass in
	 */
	public Photo getPhoto(){
		return this.photo;
	}
	
	
	
	/**
	 * Get the Thumbnail image.
	 * 
	 * @return Thumbnail	
	 * 				Thumbnail image
	 * 
	 * @throws IOException
	 */
	public BufferedImage getThumbnail() throws IOException{
		return photo.getThumbImage();
	}
	
	
	
	/**
	 * Get photo's past names, in the form of single string.
	 * 
	 * @return pastNames
	 * 				photo's pastnames in single string form.
	 */
	public String getPastNames(){
		String pastNames = "";
		for (String pastName: this.photo.getPreviousName()){
			pastNames = pastNames + pastName + "/n";
		}
		return pastNames;
	}
	
	
	
	/**
	 * Add mouse event to thumbnails. (click)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
			if (arg0.getSource().getClass().equals(this.getClass())){
				ThumbNail source = (ThumbNail) arg0.getSource();
				System.out.println(source.getPhoto().getPath());
				
				mainWindow.getImageUsingLabel().setIcon(new ImageIcon(source.getPhoto().getPath()));
				mainWindow.getImageUsingLabel().setVisible(true);
				
				mainWindow.getTitle().setText(photo.getName());
				mainWindow.getTitle().setVisible(true);
				
				mainWindow.setPastNamesList(mediator,photo);
				
				mainWindow.setCurrPhoto(source.getPhoto());
				}
	}
	

	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
			}
	

	@Override
	public void mouseExited(MouseEvent e) {

	}

}

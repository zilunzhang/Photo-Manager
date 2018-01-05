package photo_renamer;

import java.awt.EventQueue;

import gui.MainWindow;

/**
 * PhotoRenamer class is the entry for the whole 
 * PhotoRenamer application.
 */
public class PhotoRenamer {
	
	/**
	 * main entry for driving PhotoRenamer application.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		}

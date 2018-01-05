package gui;

import java.io.File;

import javax.swing.JFileChooser;

public class DirectoryReader {
	 
	/**
	 * Return the selected file.
	 * 
	 * @return file
	 * 				file that selected.
	 */
	public static File display(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		return file;
	}
	
}

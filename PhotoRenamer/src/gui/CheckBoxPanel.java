package gui;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import model.Mediator;
import model.Photo;


// Reference from lecture code "CheckBoxPanel"
public class CheckBoxPanel extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private MainWindow mainWindow;

	private List<JCheckBox> Tags = new ArrayList<>();
	
	public CheckBoxPanel(MainWindow mainWindow) throws IOException {
		this.mainWindow = mainWindow;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		// TODO Auto-generated method stub
		Component superRes = super.add(comp);
		Tags.add((JCheckBox) comp);
		((JCheckBox) comp).addActionListener(this);
		return superRes;
	}

	public List<String> getSelected() {
		// Iterate over Tags and return the selected ones.
		ArrayList<String> selectedNames = new ArrayList<String>();
		for(JCheckBox tag: Tags){
			if (tag.isSelected()){
				selectedNames.add(tag.getName());
			}
		
		}
		return 	selectedNames;
	}
	
	public String getName(ActionEvent e){
		JCheckBox comp = (JCheckBox) e.getSource();
		if (comp.isSelected()){
			return comp.getName();
			}
		return "Please select a Tag";
	}

	public void actionPerformed(ActionEvent e) {
		JCheckBox comp = (JCheckBox) e.getSource();
		if (comp.isSelected())
			System.out.println("Wow I selected a " + comp.getText() + " Tag!");
		mainWindow.setCurrTagName(comp.getText());
		
	}
}

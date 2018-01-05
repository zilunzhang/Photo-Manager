package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exception.DuplicateTagException;
import exception.InvalidPhotoPathException;
import exception.InvalidTagNameException;
import exception.TagNotExistException;
import model.Mediator;
import model.Photo;
import model.PhotoLoader;
import model.PhotoManager;
import model.Tag;
import model.TagManager;
import util.Thumbnail;


public class MainWindow {

	private static final int blockWidth = 300;
	
	// Util
	
	public JFrame mainFrame;
	private MainWindow mainWindow;
	private File selectedFile;
	private List<JLabel> thumbNailList;
	private Photo currPhoto;
	private String currTagName;
	
	// LeftBlock
	
	private JPanel leftBlockInit;
	private JPanel leftBlock;
	// Photo past name panel components 
	private JScrollPane pastNamescrollPanel;
	private DefaultListModel<String> listModel;
	private String currSelectedName;
	private JList<String> pastNameList;
	// Button panel component
	private JPanel leftBlockButtonPanel;
	private JButton revertButton;
	private JButton logButton;
	
	// Middle block
	
	private JPanel middleBlockInit;
	private JPanel middleBlock;
	// Title Panel
	private JPanel middleBlockTitlePanel;
	private JLabel title;
	// Preview panel
	private JScrollPane previewScroll;
	protected ImageIcon usingImage; // preview image part
	private JLabel imageUsingLabel; // preview image part
	
	// Right block
	
	private JPanel rightBlockInit;
	private JPanel rightBlock;
	// Add tag Panel
	private JTextArea tagToAdd;
	private JButton commitButton;
	private JPanel buttonPanel;
	//Taglistblock
	private JPanel tagsBlock;
	private JScrollPane tagScrollPanel;
	private JPanel addTagBlock;
	private CheckBoxPanel tagsList;
	private JPanel addDeletebuttonPanel;
	private JButton addToPhoto;
	private JButton removeFromPhoto;
	
	//Downblock
	
	private JScrollPane downBlockInit;
	private JScrollPane downBlockScrollPane;
	private JPanel downBlockContent;
	
	//Mediater
	
	private Mediator mediator;
	private PhotoManager pManager;
	private TagManager tManager;

	/**
	 * Create the application.
	 * 
	 * @throws IOException 
	 */
	public MainWindow() throws IOException {
		initializeMainWindow();
		
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException 
	 */
	public void initializeMainWindow() throws IOException {
		// Init main frame
		mainFrame = new JFrame();
		mainWindow = this;
		mainFrame.setTitle("PhotoRenamer");
		mainFrame.setBounds(30, 30, 1500, 1000);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Init left block
		leftBlockInit = initializeLeftBlock();
		leftBlockInit.setVisible(true);
		mainFrame.getContentPane().add(leftBlockInit, BorderLayout.WEST);
		
		// Init middle block
		middleBlockInit = initializeMiddleBlock();
		middleBlockInit.setVisible(true);
		mainFrame.getContentPane().add(middleBlockInit, BorderLayout.CENTER);
		
		// Init right block
		rightBlockInit = initializeRightBlock();
		rightBlockInit.setVisible(true);
		mainFrame.getContentPane().add(rightBlockInit, BorderLayout.EAST);
		
		// Init down block
		downBlockInit = initializeDownBlock();
		downBlockInit.setVisible(true);
		thumbNailList = new ArrayList<JLabel>();
		mainFrame.getContentPane().add(downBlockInit, BorderLayout.SOUTH);
		
		mainFrame.pack();
		
		// Init menu bar
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu openMenu = new JMenu("Open");
		openMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuBar.add(openMenu);
		
		// Implement Load Photo button
		JMenuItem openMenuItem = new JMenuItem("Load Photos");
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			// Add action preform to this menu item
			public void actionPerformed(ActionEvent arg0) {
				selectedFile = DirectoryReader.display();
				try {
					// Init tag manager and photo manager
					tManager = new TagManager("Tag.ser");
					pManager = new PhotoManager("Photo.ser");
					
					// Load photos into photo managers 
					PhotoLoader.loadPhotos(pManager, selectedFile);
					
					// Make mediator
					mediator = Mediator.getInstance(tManager, pManager);
					
					// Get thumbnail image list
					List<Thumbnail> thumbList= mediator.getThumbList();
					setThumbs(thumbList);
					setAllTags(mediator.getAllTags());
					
					// Add action to log button
					logButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
				
						JFrame photoLog = new JFrame("Photo's Renaming Log" );
						photoLog.setSize( 1500, 1000 );
						photoLog.setVisible( true );
						JTextArea displayArea = new JTextArea();
						String content = null;
						try {
						content = getPreviousNameLog(mediator.getNamingLogList());
						} catch (IOException e1) {
						e1.printStackTrace();
						}
						displayArea.setText(content);
						photoLog.add(displayArea);
						}});
					

					// Add action to revert button
					revertButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							mediator.revertToPreName(currPhoto, currSelectedName);
							setPastNamesList(mediator, currPhoto);
							getTitle().setText(currPhoto.getName());
							mainFrame.repaint();
						} catch (DuplicateTagException | IOException e1) {
							e1.printStackTrace();
						}
						catch(InvalidPhotoPathException e2){
							// Reference from Oracle
							// http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
							JOptionPane.showMessageDialog(mainFrame,
								    "Cannot Find Photo. Check if You Renamed Photo through Opration System",
								    "Inane warning",
								    JOptionPane.WARNING_MESSAGE);
						}
						catch(InvalidTagNameException e3){
						}
						catch(TagNotExistException e4){}
					}});
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		openMenu.add(openMenuItem);
		
		// Implement Exit button
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.setVisible(false);
				mainFrame.dispose();
			}
		});
		openMenu.add(exitMenuItem);
		
		// Implement Help button
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuBar.add(helpMenu);
		
		JMenuItem mntmContactAuthor = new JMenuItem("Contact Author");
		mntmContactAuthor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					JFrame newFrame = new JFrame("Photo's Log");
					newFrame.setVisible(true);
					newFrame.setSize(300,300);
					JPanel contentPanel = new JPanel();
					JTextArea content = new JTextArea();
					content.setText("zhan1381: zilun.zhang@mail.utoronto.ca"+"\n"+"linjiah6 jiahuang.lin@mail.utoronto.ca");
					contentPanel.add(content);
					newFrame.add(contentPanel, BorderLayout.CENTER);
					newFrame.pack();
					}
		});
		helpMenu.add(mntmContactAuthor);
		mainFrame.getContentPane().setLayout(null);
		
		// Save file and exit 
		// Reference from stackoverflow: 
		// http://stackoverflow.com/questions/9093448/do-something-when-the-close-button-is-clicked-on-a-jframe
			mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(mainFrame, 
		            "Do you want to save the changes?", "Saving changes?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	try {
						mediator.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        	System.exit(0);
		        } else {
			        try {
						mediator.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        	System.exit(0);
			        }
		    }
		});
		
	}
	
	/**
	 * initialize Left Block
	 * 
	 * @return
	 */
	public JPanel initializeLeftBlock(){
		leftBlock = new JPanel();
		leftBlock.setBorder(BorderFactory.createTitledBorder("Photo's Past Names"));
		
		// Use JList to display previous names
		listModel = new DefaultListModel<String>();
		pastNameList = new JList<String>(listModel);
		pastNameList.addListSelectionListener(
	                            new ListSelectionListener(){
	                            	@Override
									public void valueChanged(ListSelectionEvent e){
	                            		currSelectedName = pastNameList.getSelectedValue();
	                            		System.out.println("Selected past name:");
	                            		System.out.println(currSelectedName);
	                            		System.out.println(currPhoto.getTags());
	                            	}
	                            });
		revertButton = new JButton();
		revertButton.setText("Revert To This Name");
		revertButton.setVisible(true);
		logButton = new JButton();
		logButton.setText("Check Photo's Log");
		logButton.setVisible(true);

		leftBlockButtonPanel = new JPanel();
		leftBlockButtonPanel.add(revertButton);
		leftBlockButtonPanel.add(logButton);
		leftBlockButtonPanel.setLayout(new BoxLayout(leftBlockButtonPanel, BoxLayout.Y_AXIS));
		leftBlockButtonPanel.setMaximumSize(new Dimension(400, 100));
		
		pastNamescrollPanel = new JScrollPane(pastNameList, 
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		leftBlock.setPreferredSize(new Dimension( blockWidth, (mainFrame.getSize().height)*1/2));
		leftBlock.add(pastNamescrollPanel);
		leftBlock.add(leftBlockButtonPanel);
		leftBlock.setLayout(new BoxLayout(leftBlock, BoxLayout.Y_AXIS));
		leftBlock.setVisible(true);
		return leftBlock;	
	}
	
	/**
	 * Initialize Middle Block
	 * 
	 * @return
	 */
	public JPanel initializeMiddleBlock(){
		
		middleBlock = new JPanel();
		title = new JLabel();
		if(title.getName() == null){
			title.setText("Photo's Pastname Here");
		}
		title.setSize(75,75);
		title.setVisible(true);
		middleBlockTitlePanel = new JPanel();
		middleBlockTitlePanel.add(title, BorderLayout.CENTER);
		
		// Image preview panel
		imageUsingLabel = new JLabel();
		imageUsingLabel.setVisible(true);
		previewScroll = new JScrollPane(imageUsingLabel, 
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		previewScroll.setPreferredSize(new Dimension(middleBlock.getWidth(), 700));
		middleBlock.setPreferredSize(new Dimension( 800, (mainFrame.getSize().height)*3/5));
		middleBlock.add(middleBlockTitlePanel);
		middleBlock.add(previewScroll);
		middleBlock.setLayout(new BoxLayout(middleBlock, BoxLayout.Y_AXIS));
		middleBlock.setVisible(true);
		return middleBlock;
	}
	
	/**
	 * Initialize Right Block
	 * 
	 * @return
	 */
	public JPanel initializeRightBlock(){
			
			// Right block part
			rightBlock = new JPanel();
			
			// Add tag part
			addTagBlock = new JPanel();
			
			// Add tag part-set title and size
			addTagBlock.setBorder(BorderFactory.createTitledBorder("Add tag to this photo"));
			addTagBlock.setSize(new Dimension(20, 20));
			
			// Add tag part-add a button
			commitButton = new JButton("Commit!");
			commitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						mediator.addNewTagToPhoto(currPhoto, tagToAdd.getText());
						Tag newTag = mediator.findTag(tagToAdd.getText());
						JCheckBox newTagCheckBox = new JCheckBox(newTag.getName());
						newTagCheckBox.setVisible(true);
						tagsList.add(newTagCheckBox);

						tagsList.setVisible(true);
						tagsBlock.doLayout();
						mainFrame.repaint();
						
						setPastNamesList(mediator,currPhoto);
						title.setText(currPhoto.getName());						
						tagToAdd.setText(null);
					} catch (DuplicateTagException | IOException e1) {
						// Reference from Oracle
						// http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
						JOptionPane.showMessageDialog(mainFrame,
							    "This Tag Already Exists",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
					}
					catch(InvalidPhotoPathException e2){
						// Reference from Oracle
						// http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
						JOptionPane.showMessageDialog(mainFrame,
							    "Cannot Find Photo. Check if You Renamed Photo through Opration System",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
					}
					catch(InvalidTagNameException e3){
						// Reference from Oracle
						// http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
						JOptionPane.showMessageDialog(mainFrame,
							    "Please Type a Non-Empty Name",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
					}
					catch(TagNotExistException e4){}
				}});

			// Make a buttonPanel
			buttonPanel = new JPanel();
			buttonPanel.add(commitButton, BorderLayout.CENTER);
	
			// Add tag part-add JTextArea
			tagToAdd = new JTextArea();
			
			// Check if text is valid
			tagToAdd.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					if(!(tagToAdd.getText().equals(""))) {
						commitButton.setEnabled(true);
					}
					else {
						commitButton.setEnabled(false);
					}
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					if(!(tagToAdd.getText().equals(""))) {
						commitButton.setEnabled(true);
					}
					else {
						commitButton.setEnabled(false);
					}
				}
				});
			
			// Set JTextArea
			tagToAdd.setLineWrap(true);
			tagToAdd.setWrapStyleWord(true);
			
			// Add comp to addTagBlock
			addTagBlock.add(tagToAdd);
			addTagBlock.add(buttonPanel);
			addTagBlock.setMaximumSize(new Dimension(400, 100));			
			
			// Make a new tagsBlock
			tagsBlock = new JPanel();
			
			// make a CheckBoxPanel
			try {
				tagsList = new CheckBoxPanel(mainWindow);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			tagsList.setLayout(new GridLayout(10, 3) );

			// Add tagsList to tagScrollPanel.
			tagScrollPanel = new JScrollPane(tagsList, 
				    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			// Set title for tagScrollPanel.
			tagScrollPanel.setBorder(BorderFactory.createTitledBorder("Tag's List (One Per Time, need to uncheck)"));
//			tagsList.setBorder(BorderFactory.createTitledBorder("Tag's List (One Per Time)"));
			
			// Add tagScrollPanel to tagsBlock.
			tagsBlock.add(tagScrollPanel);
//			tagsBlock.add(tagsList);
			
			// Make button Panel.
			addDeletebuttonPanel = new JPanel();
			
			// Make add button.
			addToPhoto = new JButton("Add This Tag To Photo");
			
			addToPhoto.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						mediator.addExistingTagToPhoto(currPhoto, currTagName);
						getTitle().setText(currPhoto.getName());
						setPastNamesList(mediator,currPhoto);
						mainFrame.repaint();
					} 
					catch (DuplicateTagException | IOException e1) {
						// Reference from Oracle
						// http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
						JOptionPane.showMessageDialog(mainFrame,
							    "This Tag Already Exists",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
					}catch(InvalidPhotoPathException e2){
						// Reference from Oracle
						// http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
						JOptionPane.showMessageDialog(mainFrame,
							    "Cannot Find Photo. Check if You Renamed Photo through Opration System",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
					}catch(InvalidTagNameException|TagNotExistException e3){
					}
				}
				}
			);		
			
			// make remove button.
			removeFromPhoto = new JButton("Remove This Tag From Photo");
			
			removeFromPhoto.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						mediator.removeTagFromPhoto(currPhoto, currTagName);
						getTitle().setText(currPhoto.getName());
						setPastNamesList(mediator,currPhoto);
						mainFrame.repaint();
					} catch (IOException e1) {
					}
					catch(InvalidPhotoPathException e2){	
					}
					catch(InvalidTagNameException|TagNotExistException e2){
					}
				}});

			// add those two buttons to button Panel
			addDeletebuttonPanel.add(addToPhoto);
			addDeletebuttonPanel.add(removeFromPhoto);
			addDeletebuttonPanel.setLayout(new BoxLayout(addDeletebuttonPanel, BoxLayout.Y_AXIS));
			
			// add button Panel to tagsBlock.
			tagsBlock.add(addDeletebuttonPanel);
			tagsBlock.setLayout(new BoxLayout(tagsBlock, BoxLayout.Y_AXIS));
			
			rightBlock.setPreferredSize(new Dimension( blockWidth, (mainFrame.getSize().height)*1/2));
			rightBlock.add(addTagBlock);
			rightBlock.add(tagsBlock);
			rightBlock.setLayout(new BoxLayout(rightBlock, BoxLayout.Y_AXIS));
			rightBlock.setVisible(true);
			return rightBlock;
		}
		
		/**
		 * Initialize Down Block
		 * 
		 * @return
		 * 
		 * @throws IOException
		 */
	public JScrollPane initializeDownBlock() throws IOException{
		
		downBlockContent = new JPanel();
		downBlockContent.setLayout(new FlowLayout());
			
		downBlockScrollPane = new JScrollPane(downBlockContent, 
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		downBlockScrollPane.setPreferredSize(new Dimension( 700, 300));
		downBlockScrollPane.setBorder(BorderFactory.createTitledBorder("thumbnail"));

		return downBlockScrollPane;
	}
	
	
	
	
	/**
	 * Set Thumbs to thumbnails' list in Down Block
	 * 
	 * @param thumbList
	 * 					the thumbnails to set
	 * @throws IOException
	 */
	public void setThumbs(List<Thumbnail> thumbList) throws IOException{
		
		for(Thumbnail thumb:thumbList){
		ThumbNail thumbNailFromPhoto = new ThumbNail(thumb.getPhoto(),  mainWindow, mediator);
		downBlockContent.add(thumbNailFromPhoto);
		thumbNailList.add(thumbNailFromPhoto);
		
		downBlockContent.setVisible(true);
		downBlockContent.doLayout();
		downBlockScrollPane.doLayout();
		mainFrame.repaint();
	}	
	}
	
	
	
	/**
	 * Set the current displaying photo. 
	 * Recognize by mouse clicking thumbnail.(Please see Class ThumbNail) 
	 * 
	 * @param photo
	 */
	public void setCurrPhoto(Photo photo){
		this.currPhoto = photo;
	}
	
	
	
	/**
	 * Set the current selecting tag. 
	 * Recognize by mouse clicking checkbox.
	 * 
	 * @param photo
	 */
	public void setCurrTagName(String currTagName){
		this.currTagName = currTagName;
	}
	
	
	
	/**
	 * Set the current displaying photo's past name list 
	 * Showing on LeftBlock. 
	 * 
	 * @param mediator
	 * 				the mediator we are using. 
	 * @param photo
	 * 				the current photo that displaying.
	 */
	public void setPastNamesList(Mediator mediator, Photo photo){
			listModel.clear();
		for (String pastName: mediator.getPreviousNames(photo)){
			listModel.addElement(pastName);
		};	
			pastNamescrollPanel.doLayout();
			leftBlock.doLayout();
			mainFrame.repaint();
		}
	
	
	
	/**
	 * Make all existing Tags in this directory's photos to be JCheckBox 
	 * and put them into JCheckBox Panel. 
	 * 
	 * @param allTags
	 * 				all tags need to be convert.				
	 */
	public void setAllTags(Collection<Tag> allTags){

		for(Tag tag : allTags){
			JCheckBox tagCheckBox = new JCheckBox(tag.getName());
			tagCheckBox.setVisible(true);
			tagsList.add(tagCheckBox);}

		tagsList.setVisible(true);
		tagsBlock.doLayout();
		mainFrame.repaint();
		
	}
	
	
	
	/**
	 * Get Photo's Previous Name Log.
	 * 
	 * @param logList
	 * 				photo's previous name log.
	 * 
	 * @return logList 
	 * 					in single string form.
	 */
	public String getPreviousNameLog(List<String> logList){
		
		String content = "";
			for (String log : logList){
			content += log + "\n";
			}
			return content;
	}
	
	/**
	 * Return the preview photo JLabel we are using.
	 * 
	 * @return imageUsingLabel
	 * 					 	contain the preview photo.
	 */
	public JLabel getImageUsingLabel(){
		return this.imageUsingLabel;
	}
	
	
	
	/**
	 * Return the title on the preview photo.
	 * 
	 * @return title
	 * 				title we are using
	 */
	public JLabel getTitle(){
		return this.title;
	}
	
	
	
	/**
	 * Return the past name list in the form of 
	 * JList<String>
	 * 
	 * @return pastNameList
	 * 					JList form of pastNameList
	 */
	public JList<String> getPastNameList(){
		return this.pastNameList;
	}
	
	}

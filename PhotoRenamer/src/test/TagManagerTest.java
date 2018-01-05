/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exception.DuplicateTagException;
import exception.InvalidTagNameException;
import exception.TagNotExistException;
import model.Photo;
import model.Tag;
import model.TagManager;

/**
 *This class tests TagManager class
 *
 */
public class TagManagerTest {
	
	private TagManager tManager;
	
	private Photo testPhoto;
	
	private static File testFile = new File("PhotosForTest/Cat.jpeg");
	
	private static File testDir = new File("PhotosForTest");
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		assertTrue(testDir.exists());
		assertTrue(!(testDir.listFiles().length == 0));
		assertTrue(testFile.exists());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File currDir = new File(System.getProperty("user.dir"));
		for(File f : currDir.listFiles()){
			if(f.getName().startsWith("Tag.log") || f.getName().endsWith(".ser")){
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				if(!f.delete())
					System.out.println("Delete unsuccessful");
			}
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tManager = new TagManager("TestTagManager.ser");
		testPhoto = new Photo("Cat.jpeg", "PhotoForTest/Cat.jpeg", testFile);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		tManager.closeHandler();
		tManager.saveToFile();
		File currDir = new File(System.getProperty("user.dir"));
		for(File f : currDir.listFiles()){
			if(f.getName().startsWith("Tag.log") || f.getName().endsWith(".ser")){
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
//				f.setWritable(true);
				if(!f.delete())
					System.out.println("Delete unsuccessful");
			}
		}
	}
	
	
	/**
	 * Test createTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 */
	@Test
	public void testCreateUniqueTag() 
			throws DuplicateTagException, InvalidTagNameException {

		assertEquals(0, tManager.getTags().size());
		tManager.createTag("Obama");
		tManager.createTag("James");
		assertEquals(2, tManager.getTags().size());
		assertNotNull(tManager.findTag("Obama"));
		assertNotNull(tManager.findTag("James"));
	}
	
	
	/**
	 * Test crateTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 */
	@Test(expected=DuplicateTagException.class)
	public void testCreateDuplicateTag() 
			throws DuplicateTagException, InvalidTagNameException {
		tManager.createTag("Obama");
		tManager.createTag("Obama");
	}
	
	/**
	 * Test isNameValid() method
	 * 
	 * @throws InvalidTagNameException
	 */
	@Test(expected=InvalidTagNameException.class)
	public void testEmptyIsNameValid() throws InvalidTagNameException {
		tManager.isNameValid("");
	}
	
	/**
	 * 
	 * @throws InvalidTagNameException
	 */
	@Test
	public void testValidIsNameValid() throws InvalidTagNameException {
		assertTrue(tManager.isNameValid("Obama"));
	}
	
	/**
	 * Test isTagExiste() method
	 * @throws TagNotExistException
	 */
	@Test(expected=TagNotExistException.class)
	public void testNonExistedIsTagExist() throws TagNotExistException {
		tManager.isTagExist(" ");
	}
	
	/**
	 * Test isTagExist() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws TagNotExistException
	 */
	@Test
	public void testExistedIsTagExist() 
			throws DuplicateTagException, InvalidTagNameException, TagNotExistException {
		tManager.createTag("Obama");
		assertTrue(tManager.isTagExist("Obama"));
	}
	
	/**
	 * Test findTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 */
	@Test
	public void testFindTag() throws DuplicateTagException, InvalidTagNameException {
		Tag t = tManager.createTag("Curry");
		assertEquals(t, tManager.findTag("Curry"));
	}
	
	/**
	 * Test getTags() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 */
	@Test
	public void testGetTags() throws DuplicateTagException, InvalidTagNameException {
		Tag t = tManager.createTag("Curry");
		assertEquals(1, tManager.getTags().size());
		assertTrue(tManager.getTags().contains(t));
	}
	
	/**
	 * Test addPhoto() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws TagNotExistException
	 */
	@Test
	public void testAddPhoto() 
			throws DuplicateTagException, InvalidTagNameException, TagNotExistException {
		Tag t = tManager.createTag("Cat");
		tManager.addPhoto("Cat", testPhoto);
		assertEquals(1, t.getPhotos().size());
		assertTrue(t.getPhotos().contains(testPhoto));
	}
	
	
	/**
	 * Test addDuplicatePhoto() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws TagNotExistException
	 */
	@Test(expected=DuplicateTagException.class)
	public void testAddDuplicatePhoto() 
			throws DuplicateTagException, InvalidTagNameException, TagNotExistException {
		Tag t = tManager.createTag("Cat");
		tManager.addPhoto("Cat", testPhoto);
		tManager.addPhoto("Cat", testPhoto);
	}
	
	/**
	 * Test removePhoto method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws TagNotExistException
	 */
	@Test
	public void testRemovePhoto() 
			throws DuplicateTagException, InvalidTagNameException, TagNotExistException {
		Tag t = tManager.createTag("Cat");
		tManager.addPhoto("Cat", testPhoto);
		assertEquals(1, t.getPhotos().size());
		assertTrue(t.getPhotos().contains(testPhoto));
		tManager.removePhoto("Cat", testPhoto);
		assertTrue(t.getPhotos().isEmpty());
	}
	
	
	/**
	 * Test deleteTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws TagNotExistException
	 */
	@Test(expected=TagNotExistException.class)
	public void testDeleteTag() throws DuplicateTagException, InvalidTagNameException, TagNotExistException {
		Tag t = tManager.createTag("Cat");
		tManager.deleteTag("Cat");
		tManager.isTagExist("Cat");
	}
	
	/**
	 * Test removeAllTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws TagNotExistException
	 */
	@Test
	public void testRemoveAllTag()
			throws DuplicateTagException, InvalidTagNameException, TagNotExistException {
		Tag t1 = tManager.createTag("Cat1");
		Tag t2 = tManager.createTag("Cat2");
		testPhoto.addTag(t1);
		testPhoto.addTag(t2);
		tManager.addPhoto("Cat1", testPhoto);
		tManager.addPhoto("Cat2", testPhoto);
		tManager.removeAllTag(testPhoto);
		assertEquals(0, t1.getPhotos().size());
		assertEquals(0, t2.getPhotos().size());
	}
	
	
	/**
	 * Test addAllTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 */
	@Test
	public void testAddAllTag() throws DuplicateTagException, InvalidTagNameException {
		Tag t1 = tManager.createTag("Cat1");
		Tag t2 = tManager.createTag("Cat2");
		testPhoto.addTag(t2);
		testPhoto.addTag(t1);
		tManager.addAllTag(testPhoto);
		assertEquals(1, t1.getPhotos().size());
		assertTrue(t1.getPhotos().contains(testPhoto));
		assertEquals(1, t2.getPhotos().size());
		assertTrue(t2.getPhotos().contains(testPhoto));
	}
	
	/**
	 * Test findMostCommonTag() method
	 * 
	 * @throws DuplicateTagException
	 * @throws InvalidTagNameException
	 * @throws IOException
	 */
	@Test
	public void testFindMostCommonTag()
			throws DuplicateTagException, InvalidTagNameException, IOException {
		Tag t1 = tManager.createTag("Cat1");
		Tag t2 = tManager.createTag("Cat2");
		Photo testPhoto2 = new Photo("Cats.jpg", "PhotosForTest/Cats.jpg", new File("PhotosForTest/Cats.jpg"));
		testPhoto.addTag(t1);
		testPhoto.addTag(t2);
		testPhoto2.addTag(t1);
		tManager.addAllTag(testPhoto);
		tManager.addAllTag(testPhoto2);
		assertEquals(t1, tManager.findMostCommonTag());
	}
}

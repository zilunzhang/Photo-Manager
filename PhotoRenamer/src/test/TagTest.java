/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Photo;
import model.Tag;

/**
 * This class tests Tag class
 *
 */
public class TagTest {

	protected Tag t;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		t = new Tag("Curry");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetName() {
//		fail("Not yet implemented");
		assertEquals("Curry", t.getName());
	}
	
	@Test
	public void testSetName() {
		t.setName("KD");
		assertEquals("KD", t.getName());
	}
	
	@Test
	public void testGetPhotos() {
		assertEquals(0, t.getPhotos().size());
	}
	
	@Test
	public void testAddPhoto() throws IOException {
		assertEquals(0, t.getPhotos().size());
		String path = "PhotosForTest/pexels-photo-202737.jpeg";
		Photo p1 = new Photo("Cat", path, new File(path));
		t.addPhoto(p1);
		assertEquals(1, t.getPhotos().size());
		assertTrue(t.getPhotos().contains(p1));
	}
	
	@Test
	public void testRemovePhoto() throws IOException {
		assertEquals(0, t.getPhotos().size());
		String path = "PhotosForTest/pexels-photo-202737.jpeg";
		Photo p1 = new Photo("Cat", path, new File(path));
		t.addPhoto(p1);
		assertEquals(1, t.getPhotos().size());
		t.removePhoto(p1);
		assertEquals(0, t.getPhotos().size());
	}
	
	@Test
	public void testToString() {
		assertEquals("@Curry ", t.toString());
	} 

}

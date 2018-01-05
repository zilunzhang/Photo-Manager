/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Photo;
import model.Tag;

/**
 * This class tests Photo class
 *
 */
public class PhotoTest {
	
	private String path = "PhotosForTest/pexels-photo-202737.jpeg";
	private String name = "pexels-photo-202737.jpeg";
	private File f = new File(path);
	private Photo p;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		assertTrue(new File("PhotosForTest/pexels-photo-202737.jpeg").exists());
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
		p = new Photo(name, path, f);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testGetPath() {
		assertEquals(this.path, p.getPath());
	}
	
	
	@Test
	public void testSetPath() {
		p.setPath(" ");
		assertEquals(" ", p.getPath());
	}
	
	
	@Test
	public void testGetName() {
		assertEquals("pexels-photo-202737.jpeg", p.getName());
	}
	
	
	@Test
	public void testSetName() {
		p.setName(" ");
		assertEquals(" ", p.getName());
	}
	
	
	@Test
	public void testGetPreviousName() {
		p.setName("KT");
		p.setName("KD");
		assertEquals(2, p.getPreviousName().size());
		assertTrue(p.getPreviousName().contains("pexels-photo-202737.jpeg"));
		assertTrue(p.getPreviousName().contains("KT"));
	}
	
	
	@Test
	public void testAddTag() {
		Tag t = new Tag("Cat");
		p.addTag(t);
		assertEquals(1, p.getTags().size());
		assertTrue(p.getTags().contains(t));
	}
	
	
	@Test
	public void testRemoveTag() {
		Tag t = new Tag("Cat");
		p.addTag(t);
		assertEquals(1, p.getTags().size());
		p.removeTag(t);
		assertEquals(0, p.getTags().size());
	}
	
	
	
	@Test
	public void testGetTags() {
		Tag t = new Tag("Cat");
		List<Tag> lst = new ArrayList<>();
		lst.add(t);
		p.addTag(t);
		assertEquals(lst, p.getTags());
	}
	
	
	@Test
	public void testSetTags() {
		assertTrue(p.getTags().isEmpty());
		Tag t = new Tag("Cat");
		ArrayList<Tag> lst = new ArrayList<>();
		lst.add(t);
		p.setTags(lst);
		assertEquals(lst, p.getTags());
	}
	
	@Test
	public void testUpdateInfo() {
		p.updateInfo(" ", "  ");
		assertEquals(" ", p.getName());
		assertEquals("  ", p.getPath());
		assertEquals(new File("  "), p.getFile());
	}
	
	

}

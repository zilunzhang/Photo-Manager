/**
 * 
 */
package test;

import static org.junit.Assert.assertTrue;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Photo;
import model.PhotoManager;

/**
 *This class tests PhotoManager class
 *
 */
public class PhotoManagerTest {

	private PhotoManager pManager;
	
	private Photo p1, p2;
	
	private static File testDir = new File("PhotosForTest");
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		assertTrue(testDir.exists());
		assertTrue(!(testDir.listFiles().length == 0));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File currDir = new File(System.getProperty("user.dir"));
		for(File f : currDir.listFiles()){
			if(f.getName().startsWith("Photo.log") || f.getName().endsWith(".ser")){
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
		pManager = new PhotoManager("TestPhotoManager.ser");
		p1 = new Photo("Cats.jpg", "PhotosForTest/Cats.jpg", new File("PhotosForTest/Cats.jpg"));
		p2 = new Photo("Man.jpg", "PhotosForTest/Man.jpg", new File("PhotosForTest/Man.jpg"));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		pManager.closeHandler();
		pManager.saveToFile();
		File currDir = new File(System.getProperty("user.dir"));
		for(File f : currDir.listFiles()){
			if(f.getName().startsWith("Photo.log") || f.getName().endsWith(".ser")){
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				if(!f.delete())
					System.out.println("Delete unsuccessful");
			}
		}
	}

	@Test
	public void test() {
//		fail("Not yet implemented");
	}

}

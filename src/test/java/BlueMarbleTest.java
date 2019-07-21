import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.curiousworks.BlueMarble;
import org.junit.Test;

public class BlueMarbleTest {

	@Test
	public void testGetTodaysImage() throws Exception {
//		assertNotNull(BlueMarble.getMostRecentImage()); 
// on date 21th July 2019, there are no photos after 27th June 2019.
	}

	@Test
	public void testExceptionIsThrownWhenDateIsInvalid() throws Exception {
		try {
			String aLongTimeAgo = "1978-06-09";
			BlueMarble blueMarble = new BlueMarble();
			blueMarble.setDate(aLongTimeAgo);
			blueMarble.getImage();
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetImageOnDate() throws IOException {
		BlueMarble blueMarble = new BlueMarble();
		blueMarble.setDate("2019-05-23");
		InputStream image = blueMarble.getImage();
		assertTrue(getFirstLineOfFile(image).contains("PNG"));
	}

	private String getFirstLineOfFile(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		return reader.readLine();
	}

	@Test
	public void testCaption() throws Exception {
		BlueMarble blueMarble = new BlueMarble();
		blueMarble.setDate("2019-05-23");
		blueMarble.getImage();
		assertTrue(blueMarble.getCaption().contains("NASA"));
	}

	@Test
	public void testEnhanced() throws Exception {
		BlueMarble blueMarble = new BlueMarble();
		blueMarble.setDate("2018-09-23"); // there are no enhanced images in the database after September 2018
		blueMarble.setEnhanced(true);     // not the same status on July 2019
		InputStream image = blueMarble.getImage();
		assertTrue(getFirstLineOfFile(image).contains("PNG"));
	}

}

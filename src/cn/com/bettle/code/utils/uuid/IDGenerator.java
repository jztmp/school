package cn.com.bettle.code.utils.uuid;


/**
 * ID????
 * @author Leason
 *
 *
 */
public class IDGenerator {
	/**
	 * uuid.hex?瀹渚?
	 */
	private static final UUIDHexGenerator hexGenerator = new UUIDHexGenerator();
	/**
	 * ??涓?釜uuid.hex?涓婚?
	 * @return
	 */
	public static String generateUUIDHex(){
		return hexGenerator.generate();
	}
}

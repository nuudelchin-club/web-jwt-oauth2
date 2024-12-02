package nuudelchin.club.web.common;

import java.util.Base64;

public class ImageUtility {

	public static String encodeToBase64(byte[] imageBytes) {
		
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

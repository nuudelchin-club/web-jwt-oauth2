package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import nuudelchin.club.web.common.ImageUtility;
import nuudelchin.club.web.dto.UserPostDTO;

@Entity
@Setter
@Getter
public class UserPostEntity {
	
	private String username;
	private String fullname;
	private String email;    
    private String content;
    private String updatedAt;
    @Lob
    private byte[] picture;
    
    public UserPostDTO convertToUserPostDTO() {
    	
    	String pictureSrc = picture != null 
	            ? "data:image/png;base64," + ImageUtility.encodeToBase64(picture)
	            : null;
    	
    	UserPostDTO dto = new UserPostDTO();
    	dto.setUsername(username);
    	dto.setFullname(fullname);
    	dto.setEmail(email);
    	dto.setContent(content);
    	dto.setUpdatedAt(updatedAt);
    	dto.setPicture(pictureSrc);
		
		return dto;
    }
}

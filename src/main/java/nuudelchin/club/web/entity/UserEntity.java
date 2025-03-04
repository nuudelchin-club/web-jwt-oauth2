package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import nuudelchin.club.web.common.ImageUtility;
import nuudelchin.club.web.dto.UserDTO;

@Entity
@Getter
@Setter
public class UserEntity {

    private String username;    
    private String fullname;
    private String email;
    private String role;    
    @Lob
    private byte[] picture;
    
    public UserDTO convertToUserDTO() {
    	
    	String pictureSrc = picture != null 
	            ? "data:image/png;base64," + ImageUtility.encodeToBase64(picture)
	            : null;
    	
    	UserDTO dto = new UserDTO();
    	dto.setUsername(username);
    	dto.setFullname(fullname);
    	dto.setEmail(email);
    	dto.setRole(role);
    	dto.setPicture(pictureSrc);
		
		return dto;
    }
}

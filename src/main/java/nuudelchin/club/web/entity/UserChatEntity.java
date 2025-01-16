package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import nuudelchin.club.web.common.ImageUtility;
import nuudelchin.club.web.dto.UserChatDTO;

@Entity
@Setter
@Getter
public class UserChatEntity {

	private String fullname;
    @Lob
    private byte[] picture;
    
    private String chatId;
    private String senderId;
    private String recipientId;
    
    public UserChatDTO convertToUserChatDTO() {
    	
    	String pictureSrc = picture != null 
	            ? "data:image/png;base64," + ImageUtility.encodeToBase64(picture)
	            : null;
    	
    	UserChatDTO dto = new UserChatDTO();    	
    	dto.setFullname(fullname);
    	dto.setPicture(pictureSrc);
    	dto.setChatId(chatId);
    	dto.setSenderId(senderId);
    	dto.setRecipientId(recipientId);
		
		return dto;
    }
}

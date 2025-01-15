package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import nuudelchin.club.web.dto.MessageDTO;

@Entity
@Setter
@Getter
public class MessageEntity {
	
	private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    
    public MessageDTO convertToMessageDTO() {
    	
    	MessageDTO dto = new MessageDTO();
    	dto.setSenderId(senderId);
    	dto.setContent(content);
		
		return dto;
    }
}

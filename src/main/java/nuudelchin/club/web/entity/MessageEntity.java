package nuudelchin.club.web.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MessageEntity {
	
	private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
}

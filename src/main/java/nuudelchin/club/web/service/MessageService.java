package nuudelchin.club.web.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.MessageEntity;
import nuudelchin.club.web.repository.MessageRepository;

@Service
public class MessageService {

	private final SimpMessagingTemplate messagingTemplate;
	private final MessageRepository messageRepository;
	private final ChatService chatService;

    public MessageService(
    		SimpMessagingTemplate messagingTemplate, 
    		MessageRepository messageRepository, 
    		ChatService chatService) {

    	this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.chatService = chatService;
    }
    
    public void send(MessageEntity entity) {
    	
    	String senderId = entity.getSenderId();
    	String recipientId = entity.getRecipientId();
    	String chatId = chatService.getChatId(senderId, recipientId);
    	entity.setChatId(chatId);

    	int result = messageRepository.save(entity);
    	if(result == 1) {
    		
    		messagingTemplate.convertAndSendToUser(
        		entity.getRecipientId(), "/queue/messages",
        		entity
	        );
    	} else {
    		
    		messagingTemplate.convertAndSendToUser(
    				entity.getSenderId(), "/queue/messages",
    				"Your message was not sent!"
	        );	
    	}
    }
    
    public List<MessageEntity> get(ChatEntity entity) {
    	
    	String senderId = entity.getSenderId();
    	String recipientId = entity.getRecipientId();
    	String chatId = chatService.getChatId(senderId, recipientId);  
    	
    	return messageRepository.findByChatId(chatId);
	}
}

package nuudelchin.club.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.MessageEntity;
import nuudelchin.club.web.repository.MessageRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final ChatService chatService;

    public MessageService(MessageRepository messageRepository, ChatService chatService) {

        this.messageRepository = messageRepository;
        this.chatService = chatService;
    }
    
    public MessageEntity save(MessageEntity entity) {
    	
    	String chatId = chatService.getChatId(entity.getSenderId(), entity.getRecipientId());
    	entity.setChatId(chatId);
    	int result = messageRepository.save(entity);
    	if(result == 1) {
    		return entity;
    	}
    	return null;
    }
    
    public List<MessageEntity> get(ChatEntity entity) {
    	
    	String chatId = chatService.getChatId(entity.getSenderId(), entity.getRecipientId());    	
		return messageRepository.findByChatId(chatId);
	}
}

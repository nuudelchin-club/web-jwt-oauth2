package nuudelchin.club.web.service;

import org.springframework.stereotype.Service;

import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.repository.ChatRepository;

@Service
public class ChatService {

	private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {

        this.chatRepository = chatRepository;
    }
    
    public String getChatId(String senderId, String recipientId) {
    	
    	ChatEntity entity = chatRepository.findBySenderIdAndRecipientId(senderId, recipientId);
    	
    	if(entity != null) {    		
    		return entity.getChatId();
    	}
    	
    	return createChatId(senderId, recipientId);
    }
    
    public String createChatId(String senderId, String recipientId) {
    	
    	String chatId = String.format("%s_%s", senderId, recipientId);
    	
    	ChatEntity senderRecipient = ChatEntity
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

    	ChatEntity recipientSender = ChatEntity
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
    	
    	chatRepository.save(senderRecipient);
    	chatRepository.save(recipientSender);
    	
    	return chatId;
    }
}

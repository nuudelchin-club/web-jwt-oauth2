package nuudelchin.club.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import nuudelchin.club.web.dto.UserChatDTO;
import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.UserChatEntity;
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
    
	public List<UserChatDTO> get(String username) {
		
		List<UserChatDTO> dtoList = new ArrayList<>();
		
		List<UserChatEntity> entityList = chatRepository.selectBySenderId(username);
		
		int i, isize = entityList.size();
		for(i = 0; i < isize; i++) {
			
			UserChatEntity entity = entityList.get(i);
			dtoList.add(entity.convertToUserChatDTO());
		}
		
		return dtoList;
	}
}

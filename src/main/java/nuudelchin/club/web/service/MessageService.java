package nuudelchin.club.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.dto.MessageDTO;
import nuudelchin.club.web.dto.UserDTO;
import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.MessageEntity;
import nuudelchin.club.web.repository.MessageRepository;

@Service
public class MessageService {

	private final SimpMessagingTemplate messagingTemplate;
	private final MessageRepository messageRepository;
	private final ChatService chatService;
	private final UserService userService;

    public MessageService(
    		SimpMessagingTemplate messagingTemplate, 
    		MessageRepository messageRepository, 
    		ChatService chatService,
    		UserService userService) {

    	this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.userService = userService;
    }
    
    public void send(MessageEntity entity) {
    	
    	String senderId = entity.getSenderId();
    	String recipientId = entity.getRecipientId();
    	String chatId = chatService.getChatId(senderId, recipientId);
    	entity.setChatId(chatId);

    	int result = messageRepository.save(entity);
    	if(result == 1) {
    		
    		MessageDTO dto = entity.convertToMessageDTO();    		
    		UserDTO sender = userService.findByUsername(dto.getSenderId());
    		
    		dto.setFullname(sender.getFullname());
    		dto.setPicture(sender.getPicture());
    		
    		messagingTemplate.convertAndSendToUser(
        		entity.getRecipientId(), "/queue/messages",
        		dto
	        );
    	} else {
    		
    		messagingTemplate.convertAndSendToUser(
    				entity.getSenderId(), "/queue/messages",
    				"Your message was not sent!"
	        );	
    	}
    }
    
    public List<MessageDTO> get(ChatEntity entity) {
    	
    	String senderId = entity.getSenderId();
    	String recipientId = entity.getRecipientId();
    	String chatId = chatService.getChatId(senderId, recipientId);  
    	
    	List<MessageEntity> entityList = messageRepository.findByChatId(chatId);
    	
    	List<MessageDTO> dtoList = new ArrayList<MessageDTO>(); 
    	
    	int i, isize = entityList.size();
    	for(i = 0; i < isize; i++) {
    		
//    		MessageDTO dto = entityList.get(i).convertToMessageDTO();    		
//    		UserDTO sender = userService.findByUsername(dto.getSenderId());
//    		
//    		dto.setFullname(sender.getFullname());
//    		dto.setPicture(sender.getPicture());
    		
    		MessageDTO dto = new MessageDTO();
    		dto.setContent(entityList.get(i).getContent());
    		
    		dtoList.add(dto);
    	}
    	
    	return dtoList;
	}
}

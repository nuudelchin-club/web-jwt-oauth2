package nuudelchin.club.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.MessageEntity;
import nuudelchin.club.web.entity.NotifyEntity;
import nuudelchin.club.web.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController {
	
	private final SimpMessagingTemplate messagingTemplate;
	private final MessageService messageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {

        this.messagingTemplate = messagingTemplate;
		this.messageService = messageService;
    }

	@MessageMapping("/send")
    public void send(@Payload MessageEntity entity) {
		MessageEntity savedMsg = messageService.save(entity);
		
		if(savedMsg != null) {
			messagingTemplate.convertAndSendToUser(
        		entity.getRecipientId(), "/queue/messages",
                new NotifyEntity(
                    savedMsg.getSenderId(),
                    savedMsg.getRecipientId(),
                    savedMsg.getContent()
                )
	        );	
		} else {
			messagingTemplate.convertAndSendToUser(
				entity.getSenderId(), "/queue/messages",
	            "Your message was not sent!"
	        );
		}
    }
	
	@PostMapping("/get")
	public Object get(@RequestBody ChatEntity entity) {
		return messageService.get(entity);
	}
}

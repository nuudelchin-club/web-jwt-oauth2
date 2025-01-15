package nuudelchin.club.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuudelchin.club.web.entity.ChatEntity;
import nuudelchin.club.web.entity.MessageEntity;
import nuudelchin.club.web.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController {
	
	private final MessageService messageService;

    public MessageController(MessageService messageService) {

		this.messageService = messageService;
    }

	@MessageMapping("/send")
    public void send(@Payload MessageEntity entity) {
		messageService.send(entity);
    }
	
	@PostMapping("/get")
	public Object get(@RequestBody ChatEntity entity) {
		return messageService.get(entity);
	}
}

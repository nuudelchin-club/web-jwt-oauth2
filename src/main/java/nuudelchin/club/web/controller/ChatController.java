package nuudelchin.club.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuudelchin.club.web.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	private final ChatService chatService;

    public ChatController(ChatService chatService) {

		this.chatService = chatService;
    }
	
	@PostMapping("/get")
	public Object get(@RequestBody String username) {
		return chatService.get(username);
	}
}

package nuudelchin.club.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuudelchin.club.web.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		
		this.userService = userService;
	}

	@GetMapping("getLoggedIn")
	public Object getLoggedIn() {		
		return userService.getLoggedIn();
	}
	
	@PostMapping("/findByUsername")
	public Object findByUsername(@RequestBody String username) {
		return userService.findByUsername(username);
	}
}

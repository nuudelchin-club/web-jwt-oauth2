package nuudelchin.club.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		
		this.userService = userService;
	}

	@GetMapping("/user")
	@ResponseBody
	public Object userAPI() {
		
		System.out.println("userAPI");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		
		UserEntity entity = userService.findByUsername(username);
		
		System.out.println(entity);
		
		return entity;
	}
}

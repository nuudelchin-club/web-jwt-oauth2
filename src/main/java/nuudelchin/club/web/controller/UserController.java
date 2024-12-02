package nuudelchin.club.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nuudelchin.club.web.common.ImageUtility;
import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		
		this.userService = userService;
	}

	@GetMapping("/my")
	@ResponseBody
	public Object myAPI() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		
		UserEntity userEntity = userService.findByUsername(username);
		
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		if(userEntity != null) {
			
			String fullname = userEntity.getFullname();
			String role = userEntity.getRole();
			String email = userEntity.getEmail();
			byte[] pictureBytes = userEntity.getPicture();
			
			String picture = pictureBytes != null 
		            ? "data:image/png;base64," + ImageUtility.encodeToBase64(pictureBytes)
		            : "/images/default-profile.png";
			
			result.put("fullname", fullname);
			result.put("role", role);
			result.put("email", email);
			result.put("picture", picture);
		}
				
		return result;
	}
}

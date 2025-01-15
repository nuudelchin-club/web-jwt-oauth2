package nuudelchin.club.web.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.common.ImageUtility;
import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserEntity getLoggedIn() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		String username = authentication.getName();		
		return findByUsername(username);
    }
    
	public UserEntity findByUsername(String username) {
	
		UserEntity entity = userRepository.findByUsername(username);
		
		if(entity != null) {
			
			byte[] pictureBytes = entity.getPicture();
			
			String pictureStr = pictureBytes != null 
		            ? "data:image/png;base64," + ImageUtility.encodeToBase64(pictureBytes)
		            : null;
			
			entity.setPictureSrc(pictureStr);
			
		}
		
		return entity;
	}
}

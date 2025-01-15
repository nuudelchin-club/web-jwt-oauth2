package nuudelchin.club.web.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.dto.UserDTO;
import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserDTO getLoggedIn() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		String username = authentication.getName();		
		return findByUsername(username);
    }
    
	public UserDTO findByUsername(String username) {
	
		UserEntity entity = userRepository.findByUsername(username);		
		if(entity != null) {			
			return entity.convertToUserDTO();			
		}		
		return null;
	}
}

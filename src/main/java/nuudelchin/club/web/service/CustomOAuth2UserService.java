package nuudelchin.club.web.service;

import java.io.IOException;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.common.DownloadUtility;
import nuudelchin.club.web.dto.CustomOAuth2User;
import nuudelchin.club.web.dto.FacebookResponse;
import nuudelchin.club.web.dto.GoogleReponse;
import nuudelchin.club.web.dto.NaverResponse;
import nuudelchin.club.web.dto.OAuth2Response;
import nuudelchin.club.web.dto.UserDTO;
import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
	private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleReponse(oAuth2User.getAttributes());        
        }
        else if (registrationId.equals("facebook")) {

            oAuth2Response = new FacebookResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }
        
        String username = oAuth2Response.getUsername(); 
        String fullname = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();
        String pictureUrl = oAuth2Response.getPictureURL();        
        String role = "ROLE_USER";
        ///
        byte[] pictureBytes = null;      	
    	try {
    		if(pictureUrl != null) {
    			pictureBytes = DownloadUtility.downloadImage(pictureUrl);	
    		}			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        UserEntity userEntity = userRepository.findByUsername(username);
        
        if(userEntity == null) {
        	
        	userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setFullname(fullname);
            userEntity.setEmail(email);
            userEntity.setPicture(pictureBytes);
            userEntity.setRole(role);
            
            userRepository.save(userEntity);	
        }
        else {
        	
        	userEntity.setUsername(username);
        	userEntity.setFullname(fullname);
        	userEntity.setEmail(email);
            userEntity.setPicture(pictureBytes);
            
            userRepository.update(userEntity);
        }
        ///
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setFullname(fullname);
        userDTO.setEmail(email);
        userDTO.setPictureUrl(pictureUrl);
        userDTO.setRole(role);
        
        return new CustomOAuth2User(userDTO);
    }
}

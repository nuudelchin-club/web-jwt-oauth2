package nuudelchin.club.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.dto.UserPostDTO;
import nuudelchin.club.web.entity.PostEntity;
import nuudelchin.club.web.entity.UserPostEntity;
import nuudelchin.club.web.repository.PostRepository;

@Service
public class PostService {

	private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {

        this.postRepository = postRepository;
    }

	public List<UserPostDTO> get() {
	
		List<UserPostDTO> dtoList = new ArrayList<>();
		
		List<UserPostEntity> entityList = postRepository.get();
		
		int i, isize = entityList.size();
		for(i = 0; i < isize; i++) {
			
			UserPostEntity entity = entityList.get(i);
			dtoList.add(entity.convertToUserPostDTO());
		}
		
		return dtoList;
	}	
	
	public int save(String content) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		String username = authentication.getName();
		
		PostEntity entity = new PostEntity();
		entity.setContent(content);
		entity.setUsername(username);
		
		return postRepository.save(entity);
	}	
}

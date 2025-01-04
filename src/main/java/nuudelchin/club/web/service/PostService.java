package nuudelchin.club.web.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.entity.PostEntity;
import nuudelchin.club.web.repository.PostRepository;

@Service
public class PostService {

	private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {

        this.postRepository = postRepository;
    }

	public List<PostEntity> getAllList() {
	
		return postRepository.getAllList();
	}	
	
	public int save(String content) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		String username = authentication.getName();
		
		PostEntity postEntity = new PostEntity();
		postEntity.setContent(content);
		postEntity.setUsername(username);
		
		return postRepository.save(postEntity);
	}	
}

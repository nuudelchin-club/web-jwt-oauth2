package nuudelchin.club.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import nuudelchin.club.web.entity.PostEntity;
import nuudelchin.club.web.service.PostService;

@Controller
@ResponseBody
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		
		this.postService = postService;
	}
	
	@GetMapping("/getAllList")
	public Object getAllList() {
		
		System.out.println("getAllList");
		
		List<PostEntity> result = postService.getAllList();
		
		System.out.println(result);
		
		return result;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestBody String content) {
	
		this.postService.save(content);
		
		System.out.println("Received Post: " + content);

        return ResponseEntity.ok("Post received successfully!");
	}
}

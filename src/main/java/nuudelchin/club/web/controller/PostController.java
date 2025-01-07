package nuudelchin.club.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import nuudelchin.club.web.service.PostService;

@Controller
@ResponseBody
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		
		this.postService = postService;
	}
	
	@GetMapping("/getPostList")
	public Object getPostList() {
		
		System.out.println("getPostList");
		
		return postService.getPostList();
	}

	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestBody String content) {
	
		this.postService.save(content);
		
		System.out.println("Received Post: " + content);

        return ResponseEntity.ok("Post received successfully!");
	}
}

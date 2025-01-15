package nuudelchin.club.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuudelchin.club.web.service.PostService;

@RestController
@RequestMapping("/post")
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		
		this.postService = postService;
	}
	
	@GetMapping("/get")
	public Object get() {
		return postService.get();
	}

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody String content) {	
		int result = this.postService.save(content);		
		if(result == 1) {
			return new ResponseEntity<>("OK", HttpStatus.OK);	
		}
		return new ResponseEntity<>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

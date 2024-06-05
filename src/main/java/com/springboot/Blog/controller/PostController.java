package com.springboot.Blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.Blog.payload.PostDto;
import com.springboot.Blog.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/posts")
public class PostController {
	
	private PostService postService;

	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
		return new ResponseEntity<>(postService.createPost(postDto),HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<PostDto> getAllPosts() {
		return postService.getAllPosts();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable long id){
		return ResponseEntity.ok(postService.getPostById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable long id){
		PostDto postResponse  = postService.updatePost(postDto,id);
		
		return new ResponseEntity<>(postResponse,HttpStatus.OK);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable long id){
		postService.deletePostById(id);
		return new ResponseEntity<>("Post entity delete successfully: ",HttpStatus.OK);
	}
	
	@GetMapping("/category/{id}")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Long categoryId){
		List<PostDto> postDtos = postService.getPostByCategory(categoryId);
		return ResponseEntity.ok(postDtos);
	}
	
}

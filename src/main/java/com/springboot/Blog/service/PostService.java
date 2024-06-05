package com.springboot.Blog.service;

import java.util.List;

import com.springboot.Blog.payload.PostDto;

public interface PostService {
	PostDto createPost(PostDto postDto);
	
	List<PostDto> getAllPosts();

	PostDto getPostById(long id);

	PostDto updatePost(PostDto postDto, long id);

	void deletePostById(long id);
	
	List<PostDto> getPostByCategory(Long categoryId);
}

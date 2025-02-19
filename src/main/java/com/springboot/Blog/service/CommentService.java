package com.springboot.Blog.service;

import java.util.List;

import com.springboot.Blog.payload.CommentDto;

public interface CommentService {
	CommentDto createComment(long postId, CommentDto commentDto);
	List<CommentDto> getCommentsByPostId(long postId);
	
	CommentDto getCommentById(Long postId,Long commentId);

	CommentDto updateComment(Long postId,long commentId,CommentDto commentRequest);

	void deleteComment(Long postId,Long commentId);
	
}

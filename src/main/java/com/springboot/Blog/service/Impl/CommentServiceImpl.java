package com.springboot.Blog.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.Blog.entity.Comment;
import com.springboot.Blog.entity.Post;
import com.springboot.Blog.exception.BlogApiException;
import com.springboot.Blog.exception.ResourceNotFoundException;
import com.springboot.Blog.payload.CommentDto;
import com.springboot.Blog.repository.CommentRepository;
import com.springboot.Blog.repository.PostRepository;
import com.springboot.Blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	private CommentRepository commentRepository;
	
	private PostRepository postRepository;
	
	private ModelMapper mapper;
	
	public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper) {
		super();
		this.commentRepository = commentRepository;
		this.postRepository=postRepository;
		this.mapper=mapper;
	}



	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		Comment comment  = mapToEntity(commentDto);
		Post post  = postRepository.findById(postId).orElseThrow(
				()-> new ResourceNotFoundException("Post","id",postId));
		comment.setPost(post);
		Comment newComment = commentRepository.save(comment);
		return mapToDto(newComment);
	}
	
	private CommentDto mapToDto(Comment comment) {
		CommentDto commentDto = mapper.map(comment, CommentDto.class);
		
//		CommentDto commentDto = new CommentDto();
//		commentDto.setId(comment.getId());
//		commentDto.setName(comment.getName());
//		commentDto.setEmail(comment.getEmail());
//		commentDto.setBody(comment.getBody());
		return commentDto;
		
	}
	
	private Comment mapToEntity(CommentDto commentDto) {
//		Comment comment = new Comment();
//		comment.setBody(commentDto.getBody());
//		comment.setId(commentDto.getId());
//		comment.setName(commentDto.getName());
//		comment.setEmail(commentDto.getEmail());
//		
		Comment comment = mapper.map(commentDto, Comment.class);
		return comment;
	}



	@Override
	public List<CommentDto> getCommentsByPostId(long postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);
		return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
	}



	@Override
	public CommentDto getCommentById(Long postId, Long commentId) {
		Post post = postRepository.findById(postId).orElseThrow(
				()-> new ResourceNotFoundException("Post","id",postId));
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				()-> new ResourceNotFoundException("Comment","id",commentId));
		
		if(!comment.getpost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
		}
		
		return mapToDto(comment);
		
		
	}



	@Override
	public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {
		Post post = postRepository.findById(postId).orElseThrow(
				()-> new ResourceNotFoundException("Post","id",postId));
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				()-> new ResourceNotFoundException("Comment","id",commentId));
		
		if(!comment.getpost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
		}
		
		
		comment.setBody(commentRequest.getBody());
		comment.setEmail(commentRequest.getEmail());
		comment.setName(commentRequest.getName());
		
		Comment updatedComment = commentRepository.save(comment);
		return mapToDto(updatedComment);
	}



	@Override
	public void deleteComment(Long postId, Long commentId) {
		Post post = postRepository.findById(postId).orElseThrow(
				()-> new ResourceNotFoundException("Post","id",postId));
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				()-> new ResourceNotFoundException("Comment","id",commentId));
		
		if(!comment.getpost().getId().equals(post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
		}
		
		commentRepository.delete(comment);
		
	}
	
	

}

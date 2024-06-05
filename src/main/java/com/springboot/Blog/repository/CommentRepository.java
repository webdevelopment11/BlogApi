package com.springboot.Blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.Blog.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment,Long>{
	List<Comment> findByPostId(long postId);
}

package com.springboot.Blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.Blog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}

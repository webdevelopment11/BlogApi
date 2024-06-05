package com.springboot.Blog.service;

import java.util.List;

import com.springboot.Blog.payload.CategoryDto;

public interface CategoryService {

	CategoryDto addCategory(CategoryDto categoryDto);
	
	CategoryDto getcategory(Long categoryId);
	
	List<CategoryDto> getAllCategories();
	
	CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
	
	void deleteCategory(Long categoryId);
}

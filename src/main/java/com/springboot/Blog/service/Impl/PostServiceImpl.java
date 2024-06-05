package com.springboot.Blog.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.Blog.entity.Category;
import com.springboot.Blog.entity.Post;
import com.springboot.Blog.exception.ResourceNotFoundException;
import com.springboot.Blog.payload.PostDto;
import com.springboot.Blog.repository.CategoryRepository;
import com.springboot.Blog.repository.PostRepository;
import com.springboot.Blog.service.PostService;
@Service
public class PostServiceImpl implements PostService{

	private PostRepository postRepository;
	private ModelMapper mapper;
	
	private CategoryRepository categoryRepository;
	
	@Autowired
	public PostServiceImpl(PostRepository postRepository,ModelMapper mapper,
							CategoryRepository categoryRepository) {
		super();
		this.postRepository = postRepository;
		this.mapper=mapper;
		this.categoryRepository = categoryRepository;
	}





	@Override
	public PostDto createPost(PostDto postDto) {
		
		Category category = categoryRepository.findById(postDto.getCategoryId())
		.orElseThrow(()-> new ResourceNotFoundException("Category","id",postDto.getCategoryId()));
		
		
		
		Post post = mapToEntity(postDto);
		post.setCategory(category);
		Post newPost = postRepository.save(post);
		PostDto postResponse =  mapToDto(newPost);
		return postResponse;
	}

	@Override
	public PostDto getPostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
		return mapToDto(post);
	}



	@Override
	public List<PostDto> getAllPosts() {
		
		 // create Pageable instance
        

        List<Post> posts = postRepository.findAll();

      

        return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
	}
	
	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
		
		Category category = categoryRepository.findById(postDto.getCategoryId())
			.orElseThrow(()-> new ResourceNotFoundException("category","id",postDto.getCategoryId()));
		
		post.setContent(postDto.getContent());
		post.setDescription(postDto.getDescription());
		post.setTitle(postDto.getTitle());
		post.setCategory(category);
		Post updatePost = postRepository.save(post);
		PostDto postResponse = mapToDto(updatePost);
		return postResponse;
		
	}
	
	@Override
	public void deletePostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
		postRepository.delete(post);
		
	}
	
	private PostDto mapToDto(Post post) {
		
		PostDto postDto = mapper.map(post, PostDto.class);
		
		
//		PostDto postDto = new PostDto();
		
//		postDto.setId(post.getId());
//		postDto.setContent(post.getContent());
//		postDto.setDescription(post.getDescription());
//		postDto.setTitle(post.getTitle());

		
		
		return postDto;
	}

	
	private Post mapToEntity(PostDto postDto) {
		
		Post post = mapper.map(postDto,Post.class);
		
//		Post post = new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
		return post;
	}





	@Override
	public List<PostDto> getPostByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category","id",categoryId));
		
		List<Post> posts = postRepository.findByCategoryId(categoryId);
		
		return posts.stream().map((post)-> mapToDto(post)).collect(Collectors.toList());
	}





	





	





	
}

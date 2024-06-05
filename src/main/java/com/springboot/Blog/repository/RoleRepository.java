package com.springboot.Blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.Blog.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	Optional<Role> findByName(String name);
	

}

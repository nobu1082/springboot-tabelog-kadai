package com.example.nobukuni2023.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nobukuni2023.entity.Role;


public interface RoleRepository extends JpaRepository<Role,Integer>{
	public Role findByName(String name);
	public Optional<Role> findById(Integer id);
}

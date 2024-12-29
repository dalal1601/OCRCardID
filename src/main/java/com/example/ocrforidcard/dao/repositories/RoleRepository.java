package com.example.ocrforidcard.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ocrforidcard.dao.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);


}

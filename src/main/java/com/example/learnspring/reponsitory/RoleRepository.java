package com.example.learnspring.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learnspring.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}

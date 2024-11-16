package com.example.learnspring.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learnspring.Entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}

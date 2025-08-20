package com.example.springbootwebapp.repository;

import com.example.springbootwebapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

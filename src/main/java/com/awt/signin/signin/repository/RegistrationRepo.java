package com.awt.signin.signin.repository;

import com.awt.signin.signin.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegistrationRepo extends JpaRepository<Users, Long> {


    Users findByEmail(String email);
}

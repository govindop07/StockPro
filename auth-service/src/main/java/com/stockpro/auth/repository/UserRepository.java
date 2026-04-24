package com.stockpro.auth.repository;

import com.stockpro.auth.entity.Role;
import com.stockpro.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Optional<User> findByUserId(Long userId);

    boolean existsByEmail(String email);

    List<User> findAllByRole(Role role);

    List<User> findByDepartment(String department);

    List<User> findByIsActive(Boolean isActive);

    void deleteByUserId(Long userId);
}

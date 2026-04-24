package com.stockpro.auth.service;

import com.stockpro.auth.dto.*;

import java.util.List;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    void logout(String token);
    LoginResponse refreshToken(String token);
    UserResponse getUserById(Long userId);
    UserResponse getCurrentUserProfile(String email);
    UserResponse updateProfile(String email, ProfileUpdateRequest request);
    void changePassword(String email, PasswordChangeRequest request);
    void deactivateUser(Long userId);
    List<UserResponse> getAllUsers();
}

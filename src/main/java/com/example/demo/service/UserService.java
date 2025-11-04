package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional(readOnly = true)
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        User user = userRepository.findByUserIdAndPassword(
            loginRequest.getUserId().toUpperCase(), 
            loginRequest.getPassword().toUpperCase()
        ).orElseThrow(() -> new RuntimeException("Wrong Password. Try again..."));
        
        return new LoginResponseDTO(
            user.getUserId(),
            user.getFirstName(),
            user.getLastName(),
            user.getUserType(),
            "Authentication successful"
        );
    }
    
    @Transactional(readOnly = true)
    public PageResponseDTO<UserDTO> getAllUsers(int page, int size, String startUserId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        
        Page<User> userPage;
        if (startUserId != null && !startUserId.trim().isEmpty()) {
            userPage = userRepository.findUsersStartingFrom(startUserId, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        
        List<UserDTO> userDTOs = userPage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return new PageResponseDTO<>(
            userDTOs,
            userPage.getNumber(),
            userPage.getSize(),
            userPage.getTotalElements(),
            userPage.getTotalPages(),
            userPage.isFirst(),
            userPage.isLast(),
            userPage.hasNext(),
            userPage.hasPrevious()
        );
    }
    
    @Transactional(readOnly = true)
    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User ID NOT found..."));
        return convertToDTO(user);
    }
    
    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByUserId(userCreateDTO.getUserId())) {
            throw new RuntimeException("User ID already exist...");
        }
        
        User user = new User(
            userCreateDTO.getUserId().toUpperCase(),
            userCreateDTO.getFirstName(),
            userCreateDTO.getLastName(),
            userCreateDTO.getPassword().toUpperCase(),
            userCreateDTO.getUserType().toUpperCase()
        );
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    @Transactional
    public UserDTO updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User ID NOT found..."));
        
        boolean modified = false;
        
        if (!user.getFirstName().equals(userUpdateDTO.getFirstName())) {
            user.setFirstName(userUpdateDTO.getFirstName());
            modified = true;
        }
        
        if (!user.getLastName().equals(userUpdateDTO.getLastName())) {
            user.setLastName(userUpdateDTO.getLastName());
            modified = true;
        }
        
        if (!user.getPassword().equals(userUpdateDTO.getPassword().toUpperCase())) {
            user.setPassword(userUpdateDTO.getPassword().toUpperCase());
            modified = true;
        }
        
        if (!user.getUserType().equals(userUpdateDTO.getUserType().toUpperCase())) {
            user.setUserType(userUpdateDTO.getUserType().toUpperCase());
            modified = true;
        }
        
        if (!modified) {
            throw new RuntimeException("Please modify to update...");
        }
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User ID NOT found...");
        }
        userRepository.deleteById(userId);
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPassword(user.getPassword());
        dto.setUserType(user.getUserType());
        dto.setCreatedAt(user.getCreatedAt().format(DATE_TIME_FORMATTER));
        dto.setUpdatedAt(user.getUpdatedAt().format(DATE_TIME_FORMATTER));
        return dto;
    }
}

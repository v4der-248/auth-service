package org.example.services;

import java.util.Objects;
import java.util.UUID;
import java.util.HashSet;

import org.example.entities.UserInfo;
import org.example.models.UserInfoDto;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserInfo user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found with name: " + username);
        }

        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signUpUser(UserInfoDto userInfoDto){
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }

        String userId = UUID.randomUUID().toString();

        UserInfo newUser = UserInfo.builder()
                .userId(userId)
                .username(userInfoDto.getUsername())
                .password(userInfoDto.getPassword())
                .roles(new HashSet<>())
                .build();
        
        if(Objects.nonNull(newUser)){
            userRepository.save(newUser);
            return true;
        }

        return false;        
    }
}

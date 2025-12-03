package org.example.services;

import org.example.repositories.RefreshTokenRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        UserInfo extractedUserInfo = userRepository.findByUsername(username);

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(extractedUserInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
                
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
}

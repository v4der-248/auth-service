package org.example.controllers;

import org.example.entities.RefreshToken;
import org.example.models.UserInfoDto;
import org.example.responses.JwtResponseDTO;
import org.example.services.JwtService;
import org.example.services.RefreshTokenService;
import org.example.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/auth/v1/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfoDto){
        try {
            Boolean isSignedUp = userDetailsService.signUpUser(userInfoDto);
            if(Boolean.FALSE.equals(isSignedUp)){
                return new ResponseEntity<>("User Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());

            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDTO.builder()
                    .accessToken(jwtToken)
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

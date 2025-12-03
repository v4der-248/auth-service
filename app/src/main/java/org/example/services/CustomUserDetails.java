package org.example.services;

import org.example.entities.UserInfo;
import org.example.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {
    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;
    
    public CustomUserDetails(UserInfo userInfo){
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();

        List<GrantedAuthority> authList = new ArrayList<>();

        for(UserRole role : userInfo.getRoles()){
            authList.add(new SimpleGrantedAuthority(role.getRoleName().toUpperCase()));
        }

        this.authorities = authList;

    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

	@Override
	public String getPassword(){
        return password;
    }

	@Override
	public String getUsername(){
        return username;
    }

	@Override
	public boolean isAccountNonExpired(){
        return true;
    }

	@Override
	public boolean isAccountNonLocked(){
        return true;
    }

	@Override
	public boolean isCredentialsNonExpired(){
        return true;
    }

	@Override
	public boolean isEnabled(){
        return true;
    }
}
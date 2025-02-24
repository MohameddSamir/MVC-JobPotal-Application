package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.Users;
import com.jobportal.jobportal.repository.UsersRepository;
import com.jobportal.jobportal.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository){
        this.usersRepository=usersRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users= usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("couldn't find the user"));
        return new CustomUserDetails(users);
    }
}

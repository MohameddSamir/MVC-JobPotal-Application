package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.entity.RecruiterProfile;
import com.jobportal.jobportal.entity.Users;
import com.jobportal.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;
    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder,
                        RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService){
        this.usersRepository=usersRepository;
        this.passwordEncoder=passwordEncoder;
        this.recruiterProfileService=recruiterProfileService;
        this.jobSeekerProfileService=jobSeekerProfileService;
    }

    public Users getUserByEmail(String email){
        return usersRepository.findByEmail(email).orElse(null);
    }
    public void save(Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        usersRepository.save(users);
        if(users.getUserType().getUserTypeId() == 1){
            recruiterProfileService.save(new RecruiterProfile(users));
        }else {
            jobSeekerProfileService.save(new JobSeekerProfile(users));
        }
    }
    public Users getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return getUserByEmail(authentication.getName());
    }
    public Object getCurrentUserProfile(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users users= getCurrentUser();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                return recruiterProfileService.getCurrentRecruiterProfile(users.getId());
            }else {
                return jobSeekerProfileService.getCurrentJobSeekerProfile(users.getId());
            }
        }
        return null;
    }

}

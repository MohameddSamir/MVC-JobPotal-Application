package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.RecruiterProfile;
import com.jobportal.jobportal.repository.RecruiterProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository){
        this.recruiterProfileRepository=recruiterProfileRepository;
    }

    public void save(RecruiterProfile recruiterProfile){
        recruiterProfileRepository.save(recruiterProfile);
    }
    public RecruiterProfile getCurrentRecruiterProfile(int id){
        return recruiterProfileRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("couldn't find the user"));
    }
}

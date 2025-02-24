package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository){
        this.jobSeekerProfileRepository=jobSeekerProfileRepository;
    }

    public void save(JobSeekerProfile jobSeekerProfile){
        jobSeekerProfileRepository.save(jobSeekerProfile);
    }
    public JobSeekerProfile getCurrentJobSeekerProfile(int id){
        return jobSeekerProfileRepository.findById(id).orElse(null);
    }
}

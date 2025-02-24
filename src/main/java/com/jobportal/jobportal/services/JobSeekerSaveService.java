package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.JobPostActivity;
import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.entity.JobSeekerSave;
import com.jobportal.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {

    private final JobSeekerSaveRepository jobSeekerSaveRepository;
    @Autowired
    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository){
        this.jobSeekerSaveRepository=jobSeekerSaveRepository;
    }

    public void save(JobSeekerSave jobSeekerSave){
        jobSeekerSaveRepository.save(jobSeekerSave);
    }
    public List<JobSeekerSave> getCandidateJobs(JobSeekerProfile jobSeekerProfile){
        return jobSeekerSaveRepository.findByUserId(jobSeekerProfile);
    }
    public List<JobSeekerSave> getJobCandidates(JobPostActivity jobPostActivity){
        return jobSeekerSaveRepository.findByJob(jobPostActivity);
    }
}

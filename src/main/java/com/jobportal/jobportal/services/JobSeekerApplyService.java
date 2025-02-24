package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.JobPostActivity;
import com.jobportal.jobportal.entity.JobSeekerApply;
import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;
    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository){
        this.jobSeekerApplyRepository=jobSeekerApplyRepository;
    }

    public void save(JobSeekerApply jobSeekerApply){
        jobSeekerApply.setApplyDate(new Date());
        jobSeekerApplyRepository.save(jobSeekerApply);
    }
    public List<JobSeekerApply> getJobCandidates(JobPostActivity jobPostActivity){
        return jobSeekerApplyRepository.findByJob(jobPostActivity);
    }
    public List<JobSeekerApply> getCandidateJobs(JobSeekerProfile jobSeekerProfile){
        return jobSeekerApplyRepository.findByUserId(jobSeekerProfile);
    }
}

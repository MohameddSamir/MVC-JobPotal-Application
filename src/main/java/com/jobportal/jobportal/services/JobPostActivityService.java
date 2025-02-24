package com.jobportal.jobportal.services;

import com.jobportal.jobportal.entity.*;
import com.jobportal.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;
    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository){
        this.jobPostActivityRepository=jobPostActivityRepository;
    }

    public void save(JobPostActivity jobPostActivity){
        jobPostActivityRepository.save(jobPostActivity);
    }
    public List<JobPostActivity> getAll(){
        return jobPostActivityRepository.findAll();
    }
    public JobPostActivity getJobPostActivityById(int id){
        return jobPostActivityRepository.findById(id).orElse(null);
    }
    public List<RecruiterJobSDto> getRecruiterJobs(int recruiter){
        List<IRecruiterJobs> iRecruiterJobs= jobPostActivityRepository.getRecruiterJobs(recruiter);
        List<RecruiterJobSDto> recruiterJobSDtoList= new ArrayList<>();
        for (IRecruiterJobs recruiterJobs: iRecruiterJobs){
            recruiterJobSDtoList.add(new RecruiterJobSDto(recruiterJobs.getTotalCandidates(),recruiterJobs.getJobPostId(),
                    recruiterJobs.getJobTitle(),
                    new JobLocation(recruiterJobs.getLocationId(),recruiterJobs.getCountry(),recruiterJobs.getCity(),recruiterJobs.getState()),
                    new JobCompany(recruiterJobs.getCompanyId(),recruiterJobs.getName(),""))
            );
        }
        return recruiterJobSDtoList;
    }
    public List<JobPostActivity> search(String job, String location, List<String>type, List<String> remote,
                                        LocalDate searchDate){
        return Objects.isNull(searchDate) ? jobPostActivityRepository.searchWithoutDate(job, location, type, remote) :
                jobPostActivityRepository.search(job, location, type, remote, searchDate);
    }
}

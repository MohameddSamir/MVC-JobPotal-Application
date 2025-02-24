package com.jobportal.jobportal.repository;

import com.jobportal.jobportal.entity.JobPostActivity;
import com.jobportal.jobportal.entity.JobSeekerApply;
import com.jobportal.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer> {

    List<JobSeekerApply> findByJob(JobPostActivity jobPostActivity);

    List<JobSeekerApply> findByUserId(JobSeekerProfile jobSeekerProfile);

}

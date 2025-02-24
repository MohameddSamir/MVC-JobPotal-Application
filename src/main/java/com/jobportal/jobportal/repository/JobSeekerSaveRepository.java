package com.jobportal.jobportal.repository;

import com.jobportal.jobportal.entity.JobPostActivity;
import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave,Integer> {

    List<JobSeekerSave> findByUserId(JobSeekerProfile jobSeekerProfile);
    List<JobSeekerSave> findByJob(JobPostActivity jobPostActivity);
}

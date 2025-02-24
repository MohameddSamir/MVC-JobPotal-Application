package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.JobPostActivity;
import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.entity.JobSeekerSave;
import com.jobportal.jobportal.services.JobPostActivityService;
import com.jobportal.jobportal.services.JobSeekerProfileService;
import com.jobportal.jobportal.services.JobSeekerSaveService;
import com.jobportal.jobportal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class JobSeekerSaveController {

    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final UsersService usersService;
    @Autowired
    public JobSeekerSaveController(JobSeekerSaveService jobSeekerSaveService, JobPostActivityService jobPostActivityService,
                                   JobSeekerProfileService jobSeekerProfileService, UsersService usersService){
        this.jobSeekerSaveService=jobSeekerSaveService;
        this.jobPostActivityService=jobPostActivityService;
        this.jobSeekerProfileService=jobSeekerProfileService;
        this.usersService=usersService;
    }

    @GetMapping("/saved-jobs/")
    public String savedJobs(Model model){
        JobSeekerProfile jobSeekerProfile= jobSeekerProfileService.getCurrentJobSeekerProfile(usersService.getCurrentUser().getId());
        if(jobSeekerProfile != null){
            List<JobSeekerSave> jobSeekerSaveList= jobSeekerSaveService.getCandidateJobs(jobSeekerProfile);
            List<JobPostActivity> jobPostActivityList= new ArrayList<>();
            for (JobSeekerSave jobSeekerSave:jobSeekerSaveList){
                jobPostActivityList.add(jobSeekerSave.getJob());
            }
            model.addAttribute("jobPost", jobPostActivityList);
            model.addAttribute("user", jobSeekerProfile);
        }else {
            throw new RuntimeException("Some thing wrong");
        }
        return "saved-jobs";
    }
    @PostMapping("/job-details/save/{id}")
    public String saveJob(@PathVariable("id") int id){
        JobPostActivity jobPostActivity= jobPostActivityService.getJobPostActivityById(id);
        JobSeekerProfile jobSeekerProfile= jobSeekerProfileService.getCurrentJobSeekerProfile(usersService.getCurrentUser().getId());
        if(jobPostActivity != null && jobSeekerProfile != null){
            JobSeekerSave jobSeekerSave= new JobSeekerSave();
            jobSeekerSave.setJob(jobPostActivity);
            jobSeekerSave.setUserId(jobSeekerProfile);
            jobSeekerSaveService.save(jobSeekerSave);
        }else {
            throw new RuntimeException("Some thing wrong");
        }
        return "redirect:/dashboard/";
    }
}

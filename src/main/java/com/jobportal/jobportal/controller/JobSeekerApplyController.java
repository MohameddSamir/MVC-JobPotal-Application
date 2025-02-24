package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.*;
import com.jobportal.jobportal.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class JobSeekerApplyController {

    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobSeekerSaveService jobSeekerSaveService;
    @Autowired
    public JobSeekerApplyController(JobSeekerApplyService jobSeekerApplyService, JobPostActivityService jobPostActivityService,
                                    UsersService usersService, JobSeekerProfileService jobSeekerProfileService,
                                    JobSeekerSaveService jobSeekerSaveService){
        this.jobSeekerApplyService=jobSeekerApplyService;
        this.jobPostActivityService=jobPostActivityService;
        this.usersService=usersService;
        this.jobSeekerProfileService=jobSeekerProfileService;
        this.jobSeekerSaveService=jobSeekerSaveService;
    }

    @GetMapping("/job-details-apply/{id}")
    public String applyDetails(@PathVariable("id") int id, Model model){
        JobPostActivity jobPostActivity= jobPostActivityService.getJobPostActivityById(id);
        Object user= usersService.getCurrentUserProfile();
        if(jobPostActivity != null){
            List<JobSeekerApply> jobSeekerApplyList= jobSeekerApplyService.getJobCandidates(jobPostActivity);
            List<JobSeekerSave> jobSeekerSaveList= jobSeekerSaveService.getJobCandidates(jobPostActivity);
            model.addAttribute("jobDetails", jobPostActivity);
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                model.addAttribute("applyList", jobSeekerApplyList);
            }else {
                JobSeekerProfile jobSeekerProfile= jobSeekerProfileService.getCurrentJobSeekerProfile(((JobSeekerProfile)user).getUserAccountId());
                if(jobSeekerProfile != null) {
                    boolean applied = false;
                    boolean saved=false;
                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                        if (jobSeekerApply.getUserId().getUserAccountId() == jobSeekerProfile.getUserAccountId()) {
                            applied = true;
                            break;
                        }
                    }
                    for (JobSeekerSave jobSeekerSave : jobSeekerSaveList){
                        if(jobSeekerSave.getUserId().getUserAccountId() == jobSeekerProfile.getUserAccountId()){
                            saved=true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied",applied);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }
        model.addAttribute("user", user);
        return "job-details";
    }
    @PostMapping("/job-details/apply/{id}")
    public String applyJob(@PathVariable("id") int id){
        JobPostActivity jobPostActivity= jobPostActivityService.getJobPostActivityById(id);
        JobSeekerProfile jobSeekerProfile= jobSeekerProfileService.getCurrentJobSeekerProfile(usersService.getCurrentUser().getId());
        if(jobPostActivity != null && jobSeekerProfile != null) {
            JobSeekerApply jobSeekerApply= new JobSeekerApply();
            jobSeekerApply.setJob(jobPostActivity);
            jobSeekerApply.setUserId(jobSeekerProfile);
            jobSeekerApplyService.save(jobSeekerApply);
        }else {
            throw new RuntimeException("Some thing wrong");
        }
        return "redirect:/dashboard/";
    }
}

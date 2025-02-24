package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.*;
import com.jobportal.jobportal.services.JobPostActivityService;
import com.jobportal.jobportal.services.JobSeekerApplyService;
import com.jobportal.jobportal.services.JobSeekerSaveService;
import com.jobportal.jobportal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/dashboard")
public class JobPostActivityController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    @Autowired
    public JobPostActivityController(JobPostActivityService jobPostActivityService, UsersService usersService,
                                     JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService){
        this.usersService=usersService;
        this.jobPostActivityService=jobPostActivityService;
        this.jobSeekerApplyService=jobSeekerApplyService;
        this.jobSeekerSaveService=jobSeekerSaveService;
    }
    @GetMapping("/")
    public String dashboard(@RequestParam(value = "partTime", required = false) String partTime,
                            @RequestParam(value = "fullTime", required = false) String fullTime,
                            @RequestParam(value = "freelance", required = false) String freelance,
                            @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                            @RequestParam(value = "officeOnly", required = false) String officeOnly,
                            @RequestParam(value = "partialRemote", required = false) String partialRemote,
                            @RequestParam(value = "today", required = false) boolean today,
                            @RequestParam(value = "days7", required = false) boolean days7,
                            @RequestParam(value = "days30", required = false) boolean days30,
                            @RequestParam(value = "job", required = false) String job,
                            @RequestParam(value = "location", required = false) String location,
                            Model model){

        model.addAttribute("partTime", Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        boolean type= true;
        boolean remote= true;
        boolean dateSearchFlag= true;
        LocalDate localDate= null;
        List<JobPostActivity> jobPostActivityList;

        if(partTime == null && fullTime == null && freelance == null){
            partTime= "Part-Time";
            fullTime= "Full-Time";
            freelance= "Freelance";
            type= false;
        }
        if(remoteOnly == null && officeOnly == null && partialRemote == null){
            remoteOnly= "Remote-Only";
            officeOnly= "Office-Only";
            partialRemote= "Partial-Remote";
            remote= false;
        }
        if(today){
            localDate= LocalDate.now();
        } else if (days7) {
            localDate= LocalDate.now().minusDays(7);
        } else if (days30) {
            localDate= LocalDate.now().minusDays(30);
        }else {
            dateSearchFlag= false;
        }

        if(!type && ! remote && !dateSearchFlag && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPostActivityList=jobPostActivityService.getAll();
        }else {
            jobPostActivityList= jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freelance),
                    Arrays.asList(remoteOnly,officeOnly,partialRemote),localDate);
        }

        Object users= usersService.getCurrentUserProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobSDto> recruiterJobs= jobPostActivityService.getRecruiterJobs(((RecruiterProfile)users).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            }else {
                List<JobSeekerApply> jobSeekerApplyList= jobSeekerApplyService.getCandidateJobs((JobSeekerProfile) users);
                List<JobSeekerSave> jobSeekerSaveList= jobSeekerSaveService.getCandidateJobs((JobSeekerProfile) users);

                for (JobPostActivity jobPostActivity: jobPostActivityList){


                    for (JobSeekerApply jobSeekerApply: jobSeekerApplyList){
                        if (jobSeekerApply.getJob().getJobPostId() == jobPostActivity.getJobPostId()){
                            jobPostActivity.setActive(true);
                            break;
                        }
                    }

                    for (JobSeekerSave jobSeekerSave: jobSeekerSaveList){
                        if(jobSeekerSave.getJob().getJobPostId() == jobPostActivity.getJobPostId()){
                            jobPostActivity.setSaved(true);
                            break;
                        }
                    }

                }
                model.addAttribute("jobPost", jobPostActivityList);
            }
        }
        model.addAttribute("user", users);
        return "dashboard";
    }
    @GetMapping("/add")
    public String addJob(Model model){
        model.addAttribute("jobPostActivity", new JobPostActivity());
        return "add-jobs";
    }
    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity){
        Users users= usersService.getCurrentUser();
        jobPostActivity.setPostedById(users);
        jobPostActivity.setPostedDate(new Date());
        jobPostActivityService.save(jobPostActivity);
        return "redirect:/dashboard/";
    }
    @PostMapping("/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model){
        model.addAttribute("jobPostActivity", jobPostActivityService.getJobPostActivityById(id));
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }

}

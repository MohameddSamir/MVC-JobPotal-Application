package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.JobPostActivity;
import com.jobportal.jobportal.services.JobPostActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/global-search/")
public class GlobalSearchController {

    private final JobPostActivityService jobPostActivityService;
    @Autowired
    public GlobalSearchController(JobPostActivityService jobPostActivityService){
        this.jobPostActivityService=jobPostActivityService;
    }

    @GetMapping
    public String globalSearch(@RequestParam(value = "partTime", required = false) String partTime,
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
        LocalDate searchDate= null;
        List<JobPostActivity> jobPostActivityList= null;

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
            searchDate= LocalDate.now();
        } else if (days7) {
            searchDate= LocalDate.now().minusDays(7);
        } else if (days30) {
            searchDate= LocalDate.now().minusDays(30);
        }else {
            dateSearchFlag= false;
        }

        if(!type && ! remote && !dateSearchFlag && !StringUtils.hasText(job) && StringUtils.hasText(location)){
            jobPostActivityList= jobPostActivityService.getAll();
        }else {
            jobPostActivityList= jobPostActivityService.search(job, location, Arrays.asList(partTime,fullTime,freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }
        model.addAttribute("jobPost", jobPostActivityList);
        return "global-search";
    }
}

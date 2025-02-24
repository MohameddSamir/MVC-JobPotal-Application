package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.JobSeekerProfile;
import com.jobportal.jobportal.entity.Skills;
import com.jobportal.jobportal.entity.Users;
import com.jobportal.jobportal.services.JobSeekerProfileService;
import com.jobportal.jobportal.services.UsersService;
import com.jobportal.jobportal.util.FileDownloadUtil;
import com.jobportal.jobportal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersService usersService){
        this.jobSeekerProfileService=jobSeekerProfileService;
        this.usersService=usersService;
    }
    @GetMapping("/")
    public String jobSeekerProfile(Model model){
        Users users= usersService.getCurrentUser();
        JobSeekerProfile jobSeekerProfile= jobSeekerProfileService.getCurrentJobSeekerProfile(users.getId());
        model.addAttribute("profile", jobSeekerProfile);
        List<Skills> skills= new ArrayList<>();
        if(jobSeekerProfile.getSkills().isEmpty()){
            skills.add(new Skills());
            jobSeekerProfile.setSkills(skills);
        }
        model.addAttribute("skills",skills);
        return "job-seeker-profile";
    }
    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("profile") JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image")MultipartFile image, @RequestParam("pdf") MultipartFile pdf){
        Users users= usersService.getCurrentUser();
        if(users != null) {
            jobSeekerProfile.setUserId(users);
            jobSeekerProfile.setUserAccountId(users.getId());
            for(Skills skills: jobSeekerProfile.getSkills()){
                skills.setJobSeekerProfile(jobSeekerProfile);
            }

            String imageName = "", pdfName = "";
            if (!image.getOriginalFilename().equals("")) {
                imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
                jobSeekerProfile.setProfilePhoto(imageName);
            }
            if (!pdf.getOriginalFilename().equals("")) {
                pdfName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
                jobSeekerProfile.setResume(pdfName);
            }
            jobSeekerProfileService.save(jobSeekerProfile);

            String uploadDir = "job seeker/" + jobSeekerProfile.getUserAccountId();
            try {
                FileUploadUtil.saveFile(uploadDir, image, imageName);
                FileUploadUtil.saveFile(uploadDir,pdf,pdfName);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return "redirect:/dashboard/";
    }
    @GetMapping("/{id}")
    public String displayJobSeekerProfile(@PathVariable("id")int id, Model model){
        model.addAttribute("profile",jobSeekerProfileService.getCurrentJobSeekerProfile(id));
        return "job-seeker-profile";
    }
    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam("fileName")String fileName, @RequestParam("userID")int userId){
       Resource resource;
        try {
            resource= FileDownloadUtil.getFileAsResource("job seeker/"+userId, fileName);
        }catch (IOException ioe){
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType= "application/octet-stream";
        String headerValue= "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}

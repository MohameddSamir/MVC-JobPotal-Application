package com.jobportal.jobportal.controller;

import com.jobportal.jobportal.entity.RecruiterProfile;
import com.jobportal.jobportal.entity.Users;
import com.jobportal.jobportal.services.RecruiterProfileService;
import com.jobportal.jobportal.services.UsersService;
import com.jobportal.jobportal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final UsersService usersService;
    @Autowired
    public RecruiterProfileController(UsersService usersService, RecruiterProfileService recruiterProfileService){
        this.recruiterProfileService=recruiterProfileService;
        this.usersService=usersService;
    }
    @GetMapping("/")
    public String recruiterProfile(Model model){
        Users users= usersService.getCurrentUser();
        RecruiterProfile recruiterProfile = recruiterProfileService.getCurrentRecruiterProfile(users.getId());
        model.addAttribute("profile", recruiterProfile);
        return "recruiter_profile";
    }
    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("profile") RecruiterProfile recruiterProfile,
                         @RequestParam("image")MultipartFile multipartFile){
        Users users= usersService.getCurrentUser();
        if(users != null) {
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getId());

            String fileName="";
            if(!multipartFile.getOriginalFilename().equals("")){
                fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                recruiterProfile.setProfilePhoto(fileName);
            }
            recruiterProfileService.save(recruiterProfile);
            String uploadDir= "recruiter/"+users.getId();
            try {
                FileUploadUtil.saveFile(uploadDir,multipartFile,fileName);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return "redirect:/dashboard/";

    }
}

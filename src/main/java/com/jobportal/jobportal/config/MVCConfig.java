package com.jobportal.jobportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    private final static String RECRUITER_UPLOAD_DIR="recruiter";
    private final static String JOB_SEEKER_UPLOAD_DIR="job seeker";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(registry);
    }

    private void exposeDirectory(ResourceHandlerRegistry registry) {
        Path recruiterPath= Paths.get(RECRUITER_UPLOAD_DIR);
        Path joSeekerPath= Paths.get(JOB_SEEKER_UPLOAD_DIR);
        registry.addResourceHandler("/"+RECRUITER_UPLOAD_DIR+"/**").addResourceLocations("file:"+recruiterPath.toAbsolutePath()+"/");
        registry.addResourceHandler("/"+JOB_SEEKER_UPLOAD_DIR+"/**").addResourceLocations("file:"+joSeekerPath.toAbsolutePath()+"/");
    }
}

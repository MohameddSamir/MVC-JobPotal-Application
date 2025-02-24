package com.jobportal.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, MultipartFile multipartFile, String fileName) throws IOException {
        Path uploadPath= Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        InputStream inputStream= multipartFile.getInputStream();
        Path path= uploadPath.resolve(fileName);
        Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
        inputStream.close();
    }
}

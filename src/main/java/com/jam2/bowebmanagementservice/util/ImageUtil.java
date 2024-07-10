package com.jam2.bowebmanagementservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Component
@Slf4j
public class ImageUtil {

    public String saveImage(MultipartFile image) throws IOException {
        log.debug("image :: {}", image);

        String directory = "C:/Users/Mervyn/Downloads/Test";
        String fileName = image.getOriginalFilename();
        Path filePath = Paths.get(directory,fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    public String modifiedPath(String path){
        String modifiedPath = path.replace("\\","/");

        return modifiedPath;

    }

}

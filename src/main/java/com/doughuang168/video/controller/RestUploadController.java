package com.doughuang168.video.controller;


import com.amazonaws.services.s3.model.PutObjectResult;
import com.doughuang168.video.service.mediacontainer.ContainerInfoService;
import com.doughuang168.video.service.s3.AsyncService;
import com.doughuang168.video.service.s3.S3Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

    //Save the uploaded file to this folder
    @Value("${video.upload.folder}")
    private String uploadFolder;

    private final String maxAllowedDuration="00:10:00.00";

    @Autowired
    private ContainerInfoService containerInfoService;

    @Autowired
    private S3Wrapper s3Wrapper;

    @Autowired
    private AsyncService realAsyncService;

    //Single file upload
    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        String info="";
        String message="";
        Future<String> uploadService;
        logger.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        try {
            info = containerInfoService.GetBasicDuration(uploadFolder + uploadfile.getOriginalFilename());
            List<String> strlst = Arrays.stream(info.split(","))
                    .collect(Collectors.toList());
            logger.debug(strlst.get(0));
            if ( strlst.get(0).compareTo(maxAllowedDuration) > 0 ) {

                deleteUploadedFile(uploadfile.getOriginalFilename());
                message = "Upload not allowed, duration > 10 min";
            } else {
                message = "Successfully uploaded - " +"\n" +
                          uploadfile.getOriginalFilename() + " Duration: " +info;

                //Upload to AWS S3, start async process
                logger.debug(uploadFolder + uploadfile.getOriginalFilename());
                uploadService = this.realAsyncService.doUpload(uploadfile.getOriginalFilename());
            }
        } catch (Exception e) {
            logger.debug(e.toString());
        }

        return new ResponseEntity(message, new HttpHeaders(), HttpStatus.OK);

    }

    //save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            Files.write(path, bytes);

        }

    }

    //delete file
    private void deleteUploadedFile(String file) throws IOException {
            Path path = Paths.get(uploadFolder + file);
            Files.deleteIfExists(path);
    }
}

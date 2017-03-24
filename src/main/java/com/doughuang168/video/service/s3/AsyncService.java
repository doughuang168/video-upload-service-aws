package com.doughuang168.video.service.s3;

import com.amazonaws.services.s3.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

@Service
public class AsyncService {
    private final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Value("${video.upload.folder}")
    private String uploadFolder;

    @Autowired
    private S3Wrapper s3Wrapper;

    @Async
    public Future doUpload(String uploadfile)
            throws InterruptedException {

        logger.debug("about to start async Upload service for "+uploadfile);
        try {
            //Upload to AWS S3
            PutObjectResult responseResult = s3Wrapper.upload(uploadFolder + uploadfile, "transcoder/"+uploadfile);
            deleteUploadedFile(uploadfile);//claim disk space after upload to s3

            logger.debug("finishing async Upload service for "+uploadfile);
        } catch (Exception e) {
            logger.debug(e.toString());
        }
        return new AsyncResult(
                "Congrats. You finished a real thread from Foo Service (good async)");
    }

    //delete file
    private void deleteUploadedFile(String file) throws IOException {
        Path path = Paths.get(uploadFolder + file);
        Files.deleteIfExists(path);
    }
}



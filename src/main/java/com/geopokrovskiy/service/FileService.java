package com.geopokrovskiy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.geopokrovskiy.entity.*;
import com.geopokrovskiy.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final UserService userService;
    private final EventService eventService;
    private final AmazonS3 s3client;
    public final String bucketName = "geopokrovskiybucket";

    public void createBucket() {
        if (s3client.doesBucketExistV2(bucketName)) {
            log.info("Bucket {} already exists, use a different name ", bucketName);
            return;
        }
        s3client.createBucket(bucketName);
        log.info("Bucket {} has been created ", bucketName);
    }

    public void listBuckets() {
        List<Bucket> buckets = s3client.listBuckets();
        log.info("Bucket list: {} ", buckets);
    }

    public Mono<EventEntity> uploadFile(FileEntity fileEntity, UserEntity userEntity) {
        return fileRepository.save(fileEntity.toBuilder()
                        .location(fileEntity.getLocation())
                        .status(Status.ACTIVE)
                        .build()).doOnSuccess(
                        u -> {
                            String location = u.getLocation();
                            ClassLoader loader = FileService.class.getClassLoader();
                            File file = new File(loader.getResource(location).getFile());
                            s3client.putObject(bucketName, location, file);
                        }
                ).flatMap(fileEntity1 -> eventService.addEvent(fileEntity1, userEntity, EventString.UPLOADED))
                .doOnSuccess(v -> {
                    log.info("File {} has been uploaded by {} !", fileEntity, userEntity.getUsername());
                });
    }

}

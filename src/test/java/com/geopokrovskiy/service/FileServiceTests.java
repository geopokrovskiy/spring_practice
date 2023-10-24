package com.geopokrovskiy.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileServiceTests {
    @Autowired
    private FileService fileService;

    @Test
    public void testCreateBucket() {
        fileService.createBucket();
    }

    @Test
    public void testListBuckets(){
        fileService.listBuckets();
    }
}

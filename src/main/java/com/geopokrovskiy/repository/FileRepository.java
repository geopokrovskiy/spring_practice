package com.geopokrovskiy.repository;

import com.geopokrovskiy.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FileRepository extends R2dbcRepository<FileEntity, Integer> {
}


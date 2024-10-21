package com.example.smartcast.repository;

import com.example.smartcast.dto.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage,Long> {
}

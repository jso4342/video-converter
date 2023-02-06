package com.shoplive.converter.application.storage.repository;

import com.shoplive.converter.application.storage.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
}

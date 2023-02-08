package com.shoplive.converter.application.video.repository;

import com.shoplive.converter.application.video.domain.Resized;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResizedRepository extends JpaRepository<Resized, Long> {
}

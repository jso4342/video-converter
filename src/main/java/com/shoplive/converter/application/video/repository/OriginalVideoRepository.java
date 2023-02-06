package com.shoplive.converter.application.video.repository;

import com.shoplive.converter.application.video.domain.OriginalVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalVideoRepository extends JpaRepository<OriginalVideo, Long> {
}

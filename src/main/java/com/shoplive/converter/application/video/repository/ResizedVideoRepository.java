package com.shoplive.converter.application.video.repository;

import com.shoplive.converter.application.video.domain.ResizedVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResizedVideoRepository extends JpaRepository<ResizedVideo, Long> {
}

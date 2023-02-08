package com.shoplive.converter.application.video.repository;

import com.shoplive.converter.application.video.domain.Original;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalRepository extends JpaRepository<Original, Long> {
}

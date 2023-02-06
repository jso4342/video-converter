package com.shoplive.converter.application.video.service;

import com.shoplive.converter.application.video.domain.Video;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.repository.VideoRepository;
import com.shoplive.converter.core.exception.ErrorCode;
import com.shoplive.converter.core.exception.customException.VideoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository){
        this.videoRepository = videoRepository;
    }
    // 영상의 상세 정보를 조회할 수 있는 API
    @Transactional(readOnly = true)
    public VideoResponse getVideoById(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("video id" + videoId, ErrorCode.VIDEO_NOT_FOUND));

        return VideoResponse.from(video);
    }
}
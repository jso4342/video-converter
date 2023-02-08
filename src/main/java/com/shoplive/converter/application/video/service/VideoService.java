package com.shoplive.converter.application.video.service;

import com.shoplive.converter.application.video.domain.Original;
import com.shoplive.converter.application.video.domain.Resized;
import com.shoplive.converter.application.video.domain.Video;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.repository.OriginalRepository;
import com.shoplive.converter.application.video.repository.ResizedRepository;
import com.shoplive.converter.application.video.repository.VideoRepository;
import com.shoplive.converter.core.exception.ErrorCode;
import com.shoplive.converter.core.exception.customException.OriginalNotFoundException;
import com.shoplive.converter.core.exception.customException.ResizedNotFoundException;
import com.shoplive.converter.core.exception.customException.VideoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;
    private final OriginalRepository originalRepository;
    private final ResizedRepository resizedRepository;

    public VideoService(
            VideoRepository videoRepository,
            OriginalRepository originalRepository,
            ResizedRepository resizedRepository
    ){
        this.videoRepository = videoRepository;
        this.originalRepository = originalRepository;
        this.resizedRepository = resizedRepository;
    }

    public VideoResponse storeVideo(VideoRequest request) {
        Long originalId = request.originalId();
        Long resizedId = request.resizedId();

        Original original = originalRepository.findById(originalId)
                .orElseThrow(() -> new OriginalNotFoundException("original video id: " + originalId, ErrorCode.ORIGINAL_NOT_FOUND));
        Resized resized = resizedRepository.findById(resizedId)
                .orElseThrow(() -> new ResizedNotFoundException("resized video id: " + resizedId, ErrorCode.RESIZED_NOT_FOUND));
        Video video = videoRepository.save(request.toEntity(original, resized));

        return VideoResponse.from(video);
    }

    @Transactional(readOnly = true)
    public VideoResponse getVideoById(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("video id" + videoId, ErrorCode.VIDEO_NOT_FOUND));

        return VideoResponse.from(video);
    }
}
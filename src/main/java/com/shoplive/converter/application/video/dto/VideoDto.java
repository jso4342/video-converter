package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.Video;

public class VideoDto {
//    public record StorageRequest

    public record VideoResponse(
            Long id,
            String title
    ) {
        public static VideoResponse from (Video video) {
            return new VideoResponse(video.getId(), video.getTitle());
        }
    }
}

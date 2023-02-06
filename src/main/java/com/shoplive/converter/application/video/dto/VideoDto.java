package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.Original;
import com.shoplive.converter.application.video.domain.Resized;
import com.shoplive.converter.application.video.domain.Video;

import java.time.LocalDateTime;

public class VideoDto {
//    public record StorageRequest

    public record VideoResponse(
            Long id,
            String title,
            Original original,
            Resized resized,
            LocalDateTime createdAt

    ) {
        public static VideoResponse from (Video video) {
            return new VideoResponse(
                    video.getId(),
                    video.getTitle(),
                    video.getOriginal(),
                    video.getResized(),
                    video.getCreatedAt());
        }
    }
}

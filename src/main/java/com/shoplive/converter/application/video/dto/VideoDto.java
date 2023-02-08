package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.Video;
import com.shoplive.converter.application.video.domain.VideoData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VideoDto {
    public record VideoRequest(
            @NotBlank
            String title,
            @NotNull
            VideoData original,
            @NotNull
            VideoData resized,
            @NotBlank
            String thumbnailUrl
    ){
        public Video toEntity(
                String title,
                VideoData original,
                VideoData resized,
                String thumbnailUrl
        ) {
            return new Video(
                    title,
                    original,
                    resized,
                    thumbnailUrl
            );
        }
    }

    public record VideoResponse(
            Long id,
            String title,
            VideoData original,
            VideoData resized,
            String thumbnailUrl,
            LocalDateTime createdAt
    ) {
        public static VideoResponse from (Video video) {
            return new VideoResponse(
                    video.getId(),
                    video.getTitle(),
                    video.getOriginal(),
                    video.getResized(),
                    video.getThumbnailUrl(),
                    video.getCreatedAt()
            );
        }
    }
}

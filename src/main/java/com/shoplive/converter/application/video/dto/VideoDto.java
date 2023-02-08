package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.Original;
import com.shoplive.converter.application.video.domain.Resized;
import com.shoplive.converter.application.video.domain.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VideoDto {
    public record VideoRequest(
            @NotBlank
            String title,
            @NotNull
            Long originalId,
            @NotNull
            Long resizedId,
            @NotBlank
            String thumbnailUrl
    ){
        public Video toEntity(
                Original original,
                Resized resized
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
            Original original,
            Resized resized,
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
                    video.getCreatedAt());
        }
    }
}

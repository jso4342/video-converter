package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.Original;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OriginalDto {
    public record OriginalRequest(
            @NotNull
            Long fileSize,
            @NotNull
            Integer width,
            @NotNull
            Integer height,
            @NotBlank
            String videoUrl
    ){
        public Original toEntity() {
            return new Original(
                    fileSize,
                    width,
                    height,
                    videoUrl
            );
        }
    }

    public record OriginalResponse(
            Long id,
            Long fileSize,
            Integer width,
            Integer height,
            String videoUrl
    ){
        public static OriginalResponse from(Original original) {
            return new OriginalResponse(
                    original.getId(),
                    original.getFileSize(),
                    original.getWidth(),
                    original.getHeight(),
                    original.getVideoUrl()
            );
        }
    }
}

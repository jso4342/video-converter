package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.Resized;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResizedDto {
    public record ResizedRequest(
            @NotNull
            Long fileSize,
            @NotNull
            Integer width,
            @NotNull
            Integer height,
            @NotBlank
            String videoUrl
    ){
        public Resized toEntity() {
            return new Resized(
                    fileSize,
                    width,
                    height,
                    videoUrl
            );
        }
    }

    public record ResizedResponse(
            Long id,
            Long fileSize,
            Integer width,
            Integer height,
            String videoUrl
    ){
        public static ResizedResponse from(Resized resized) {
            return new ResizedResponse(
                    resized.getId(),
                    resized.getFileSize(),
                    resized.getWidth(),
                    resized.getHeight(),
                    resized.getVideoUrl()
            );
        }
    }
}

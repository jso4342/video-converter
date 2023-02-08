package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.domain.VideoData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VideoDataDto {
    public record VideoDataRequest(
            @NotNull
            Integer width,
            @NotNull
            Integer height,
            @NotBlank
            String videoUrl
    ){
        public VideoData toEntity() {
            return new VideoData(
                    width,
                    height,
                    videoUrl
            );
        }
    }

    public record VideoDataResponse(
            Integer width,
            Integer height,
            String videoUrl
    ){
        public static VideoDataResponse from(VideoData videoData) {
            return new VideoDataResponse(
                    videoData.getWidth(),
                    videoData.getHeight(),
                    videoData.getVideoUrl()
            );
        }
    }
}

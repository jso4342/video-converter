package com.shoplive.converter.application.video.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public class VideoData {
    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private String videoUrl;

    protected VideoData() { }

    public VideoData(
            Integer width,
            Integer height,
            String videoUrl
    ) {
        validateVideoData(width, height, videoUrl);

        this.width = width;
        this.height = height;
        this.videoUrl = videoUrl;
    }


    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void validateVideoData(
            int width,
            int height,
            String videoUrl
    ){
        Assert.isTrue(width > 0, "width must not be below 0");
        Assert.isTrue(height > 0, "height must not be below 0");
        Assert.notNull(videoUrl, "video url must not be null");
        Assert.hasText(videoUrl, "video url must be at least 0 character long");
    }
}

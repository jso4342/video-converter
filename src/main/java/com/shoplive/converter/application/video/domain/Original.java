package com.shoplive.converter.application.video.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.Assert;

@Entity
public class Original {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "original_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @Column(name = "original_url", nullable = false)
    private String videoUrl;

    protected Original() { }

    public Original(Long id, Long fileSize, Integer width, Integer height, String videoUrl) {
        validateFileSize(fileSize);
        validateWidth(width);
        validateHeight(height);
        validateVideoUrl(videoUrl);

        this.id = id;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.videoUrl = videoUrl;
    }

    public Original(Long fileSize, Integer width, Integer height, String videoUrl) {
        this(null, fileSize, width, height, videoUrl);
    }

    public Long getId() {
        return id;
    }

    public Long getFileSize() {
        return fileSize;
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

    public void validateFileSize(Long fileSize){
        Assert.notNull(fileSize, "file size must not be null");
    }

    private void validateWidth(Integer width) {
        Assert.notNull(width, "width must not be null");
    }

    private void validateHeight(Integer height) {
        Assert.notNull(height, "height must not be null");
    }

    private void validateVideoUrl(String videoUrl) {
        Assert.notNull(videoUrl, "video url must not be null");
        Assert.hasText(videoUrl, "video url must be at least 0 character long");
    }
}

package com.shoplive.converter.application.video.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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

    @Column(name = "original_url")
    private String videoUrl;

    protected Original() { }

    public Original(Long fileSize, Integer width, Integer height, String videoUrl) {
        //validate

        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.videoUrl = videoUrl;
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
}
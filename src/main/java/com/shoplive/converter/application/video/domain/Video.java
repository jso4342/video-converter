package com.shoplive.converter.application.video.domain;

import com.shoplive.converter.core.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.Assert;

@Entity
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Embedded
    @AttributeOverride(name = "fileSize", column = @Column(name = "original_file_size"))
    @AttributeOverride(name = "width", column = @Column(name = "original_width"))
    @AttributeOverride(name = "height", column = @Column(name = "original_height"))
    @AttributeOverride(name = "videoUrl", column = @Column(name = "original_video_url"))
    private VideoData original;

    @Embedded
    @AttributeOverride(name = "fileSize", column = @Column(name = "resized_file_size"))
    @AttributeOverride(name = "width", column = @Column(name = "resized_width"))
    @AttributeOverride(name = "height", column = @Column(name = "resized_height"))
    @AttributeOverride(name = "videoUrl", column = @Column(name = "resized_video_url"))
    private VideoData resized;

    @Column(nullable = false)
    private String thumbnailUrl;

    protected Video() { }

    public Video(
            Long id,
            String title,
            VideoData original,
            VideoData resized,
            String thumbnailUrl
    ) {
        validateVideo(title, thumbnailUrl);

        this.id = id;
        this.title = title;
        this.original = original;
        this.resized = resized;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Video(
            String title,
            VideoData original,
            VideoData resized,
            String thumbnailUrl
    ) {
        this(null, title, original, resized, thumbnailUrl);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public VideoData getOriginal() {
        return original;
    }

    public VideoData getResized() {
        return resized;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void validateVideo(String title, String thumbnailUrl){
        Assert.notNull(title, "title must not be null");
        Assert.hasText(title, "title must be at least 0 character long");
        Assert.notNull(thumbnailUrl, "thumbnail url must not be null");
        Assert.hasText(thumbnailUrl, "thumbnail url must be at least 0 character long");
    }
}

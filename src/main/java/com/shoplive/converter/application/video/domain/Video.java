package com.shoplive.converter.application.video.domain;

import com.shoplive.converter.core.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.util.Assert;

@Entity
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "original_id")
    private Original original;

    @ManyToOne
    @JoinColumn(name = "resized_id")
    private Resized resized;

    @Column(nullable = false)
    private String thumbnailUrl;

    protected Video() { }

    public Video(
            Long id,
            String title,
            Original original,
            Resized resized,
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
            Original original,
            Resized resized,
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

    public Original getOriginal() {
        return original;
    }

    public Resized getResized() {
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

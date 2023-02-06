package com.shoplive.converter.application.video.domain;

import com.shoplive.converter.core.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

@Entity
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("'untitled'")
    private String title;

    @ManyToOne
    @JoinColumn(name = "original_id")
    private Original original;

    @ManyToOne
    @JoinColumn(name = "resized_id")
    private Resized resized;

    protected Video() { }

    public Video(Long id, String title, Original original, Resized resized) {
        validateTitle(title);

        this.id = id;
        this.title = title;
        this.original = original;
        this.resized = resized;
    }

    public Video(String title, Original original, Resized resized) {
        this(null, title, original, resized);
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

    public void validateTitle(String title){
        Assert.hasText(title, "영상의 제목은 적어도 한 글자 이상이어야 합니다.");
    }
}

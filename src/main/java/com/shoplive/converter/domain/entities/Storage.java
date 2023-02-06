package com.shoplive.converter.domain.entities;

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
public class Storage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("'untitled'")
    private String title;

    @ManyToOne
    @JoinColumn(name = "original_url")
    private OriginalVideo original;

    @ManyToOne
    @JoinColumn(name = "resized_url")
    private ResizedVideo resized;

    protected Storage() { }

    public Storage(Long id, String title, OriginalVideo original, ResizedVideo resized) {
        validateTitle(title);

        this.id = id;
        this.title = title;
        this.original = original;
        this.resized = resized;
    }

    public Storage(String title, OriginalVideo original, ResizedVideo resized) {
        this(null, title, original, resized);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public OriginalVideo getOriginal() {
        return original;
    }

    public ResizedVideo getResized() {
        return resized;
    }

    public void validateTitle(String title){
        Assert.hasText(title, "제목은 적어도 한 글자 이상이어야 합니다.");
    }
    /*
    {
      "id": 123, //
      "title": "This is a sample video.", //
      "original": {
          "filesize": 58234223,
          "width": 900,
          "height": 500,
          "videoUrl": "http://.../video/sample.mp4"
      },
      "resized": {
        "filesize": 12831208,
        "width": 180,
        "height": 100,
        "videoUrl": "http://.../video/sample_180.mp4"
      },
      "createdAt": "2023-01-01T10:00:00+09:00"
     }
     */
}

package com.shoplive.converter.application.video.controller;

import com.shoplive.converter.application.video.dto.UploadDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.VideoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    public VideoController(
            VideoService videoService
    ) {
        this.videoService = videoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable(value = "id") Long videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @PostMapping(
            value = "/upload",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<VideoResponse> convertVideo(@RequestPart("title") String title, @RequestPart("file") MultipartFile file) throws IOException {

        VideoResponse response = videoService.convertVideo(title, file);
        URI location = URI.create("/video/" + response.id());
        return ResponseEntity.created(location)
                .build();
    }
}

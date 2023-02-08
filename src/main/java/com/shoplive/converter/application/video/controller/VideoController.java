package com.shoplive.converter.application.video.controller;

import com.shoplive.converter.application.video.dto.UploadDto;
import com.shoplive.converter.application.video.dto.UploadDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final int PORT;

    public VideoController(
            VideoService videoService,
            @Value("${server.port}") int PORT
    ) {
        this.videoService = videoService;
        this.PORT = PORT;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable(value = "id") Long videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @PostMapping(value = "upload")
    public ResponseEntity<VideoResponse> convertVideo(@RequestPart("file") MultipartFile file, @RequestPart("title") String title) throws IOException {
    //public ResponseEntity<VideoResponse> convertVideo(@Valid @RequestBody UploadRequest request) throws IOException {

        UploadRequest request = new UploadRequest(file, title);

        VideoResponse response = videoService.convertVideo(request);
        URI location = URI.create("/video/" + response.id());
        return ResponseEntity.created(location)
                .build();
    }
}

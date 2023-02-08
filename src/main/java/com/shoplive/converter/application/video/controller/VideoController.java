package com.shoplive.converter.application.video.controller;

import com.shoplive.converter.application.video.dto.OriginalDto.*;
import com.shoplive.converter.application.video.dto.ResizedDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.ConvertService;
import com.shoplive.converter.application.video.service.UploadService;
import com.shoplive.converter.application.video.service.VideoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/video")
public class VideoController {
    private static final Logger logger = LogManager.getLogger(VideoController.class);

    private final VideoService videoService;
    private final UploadService uploadService;
    private final ConvertService convertService;

    private final Path root = Paths.get("original");

    public VideoController(
            VideoService videoService,
            UploadService uploadService,
            ConvertService convertservice
    ) {
        this.videoService = videoService;
        this.uploadService = uploadService;
        this.convertService = convertservice;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable(value = "id") Long videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @PostMapping(
            value = "upload",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<VideoResponse> uploadVideo(@RequestPart("file") MultipartFile file, @RequestPart("title") String title) throws IOException {
        //  public ResponseEntity<VideoResponse> uploadVideo(@Valid @RequestBody UploadRequest request) throws IOException {
        // 질문! 위처럼 변수를 dto에서 받으면 인식을 못한다...

        // 파일 로컬에 업로드
        String originalSaveName = uploadService.uploadOriginal(file);

        // static resource 로 저장


        // 파일 정보 추출 및 Original에 저장
        OriginalResponse originalResponse = uploadService.saveOriginal(file, originalSaveName);

        // convert
        String resizedSaveName = convertService.convertVideo(file, originalSaveName);

        // 파일 정보 추출 및 Original에 저장
        ResizedResponse resizedResponse = uploadService.saveResized(resizedSaveName);

        VideoRequest request = new VideoRequest(title,  originalResponse.id(), resizedResponse.id());

        // video 저장
        VideoResponse response = videoService.storeVideo(request);
        URI location = URI.create("/video/" + response.id());

        return ResponseEntity.created(location)
                .build();
    }

/*

    영상 업로드 및 변환 API

    - 영상의 변환 작업은 오랜 시간이 소요되므로 업로드가 완료되면 데이터 저장 후 즉시 성공으로 응답하고,
        변환 작업은 비동기적으로 실행합니다.
    - 업로드한 영상, 변환한 영상 등의 리소스는 모두 임의의 로컬 폴더에 저장하고 API에서는 이 파일을 static resource로 제공합니다.
      e.g.) /path/to/sample video.mp4
            http://localhost:8080/path/to/sample%20video.mp4

     */


}

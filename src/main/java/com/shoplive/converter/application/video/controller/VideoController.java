package com.shoplive.converter.application.video.controller;

import com.shoplive.converter.application.video.dto.OriginalDto.*;
import com.shoplive.converter.application.video.dto.ResizedDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.ConvertService;
import com.shoplive.converter.application.video.service.UploadService;
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
    private final UploadService uploadService;
    private final ConvertService convertService;

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
        // 파라미터가 아니라 json 타입이라 ResponseBody 로 변경 필요
        //  public ResponseEntity<VideoResponse> uploadVideo(@Valid @RequestBody UploadRequest request) throws IOException {
        // 위처럼 변수를 dto에서 받으면 인식을 못한다...

        String originalSaveName = uploadService.uploadOriginal(file);
        OriginalResponse originalResponse = uploadService.saveOriginal(file, originalSaveName);

        String resizedSaveName = convertService.convertVideo(file, originalSaveName);
        ResizedResponse resizedResponse = uploadService.saveResized(resizedSaveName);

        VideoRequest request = new VideoRequest(title,  originalResponse.id(), resizedResponse.id());
        VideoResponse response = videoService.storeVideo(request);
        URI location = URI.create("/video/" + response.id());

        // 비동기 처리 필요
        // 영상의 변환 작업은 오랜 시간이 소요되므로
        // 업로드가 완료되면 데이터 저장 후 즉시 성공으로 응답하고,
        // 변환 작업은 비동기적으로 실행합니다.

        return ResponseEntity.created(location)
                .build();
    }
}

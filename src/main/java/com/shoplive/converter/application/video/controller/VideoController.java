package com.shoplive.converter.application.video.controller;

import com.shoplive.converter.application.video.dto.OriginalDto.*;
import com.shoplive.converter.application.video.dto.ResizedDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.ConvertService;
import com.shoplive.converter.application.video.service.UploadService;
import com.shoplive.converter.application.video.service.VideoService;
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

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;
    private final UploadService uploadService;
    private final ConvertService convertService;
    private final int port;

    public VideoController(
            VideoService videoService,
            UploadService uploadService,
            ConvertService convertservice,
            @Value("${server.port}") int port
    ) {
        this.videoService = videoService;
        this.uploadService = uploadService;
        this.convertService = convertservice;
        this.port = port;
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

        // 썸네일 추출
        String thumbnailName = uploadService.exportThumbnail(file, originalSaveName);
        String thumbnailUrl = "http://localhost:" + port + "/imagePath/thumbnail/" + thumbnailName;

        String resizedSaveName = convertService.convertVideo(file, originalSaveName);
        ResizedResponse resizedResponse = uploadService.saveResized(resizedSaveName);

        VideoRequest request = new VideoRequest(title,  originalResponse.id(), resizedResponse.id(), thumbnailUrl);
        VideoResponse response = videoService.storeVideo(request);
        URI location = URI.create("/video/" + response.id());

        // 비동기 처리 필요
        // 영상의 변환 작업은 오랜 시간이 소요되므로
        // 업로드가 완료되면 데이터 저장 후 즉시 성공으로 응답하고,
        // 변환 작업은 비동기적으로 실행합니다.

        // 현재
        // 업로드 완료
        // 변환 작업
        // 데이터 저장

        return ResponseEntity.created(location)
                .build();
    }


    /*
    1. 업로드 직후 영상의 변환 진행률을 수시로 조회할 수 있는 API를 제공합니다.
    이를 위해 ffmpeg의 영상 변환 과정을 기록하거나 모니터링하는 작업이 필요합니다.
        - 요청 예시
          GET /video/{id}/progress

        - 응답 예시
          {
              "id": 123,
              "progress": "37%"
          }

     */
}

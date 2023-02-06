package com.shoplive.converter.application.video.controller;

import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService){
        this.videoService = videoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    /*

     private static final Logger logger = LogManager.getLogger(VideoController.class);
    private StorageService storageService;



    - 파일 포맷은 mp4로 제한하고 1회에 업로드할 수 있는 영상의 최대 용량을 100MB로 제한합니다.
        조건에 부합하지 않는 경우 에러를 응답합니다.
    - 영상의 제목을 별도로 지정할 수 있습니다.
    - 업로드가 완료되면 가로 사이즈가 360px인 영상 1개를 추가로 생성합니다.
        이 때 원본 영상의 비율과 퀄리티를 유지합니다.
    - 영상의 변환 작업은 오랜 시간이 소요되므로 업로드가 완료되면 데이터 저장 후 즉시 성공으로 응답하고,
        변환 작업은 비동기적으로 실행합니다.
    - 영상의 변환은 ffmpeg을 이용합니다.         https://ffmpeg.org
    - 업로드한 영상, 변환한 영상 등의 리소스는 모두 임의의 로컬 폴더에 저장하고 API에서는 이 파일을 static resource로 제공합니다.
      e.g.) /path/to/sample video.mp4
            http://localhost:8080/path/to/sample%20video.mp4


    @PostMapping(value="uploadFile")
    public ResponseEntity<String> uploadFile(MultipartFile file) throws IllegalStateException, IOException {

        if( !file.isEmpty() ) {
            logger.debug("file org name = {}", file.getOriginalFilename());
            logger.debug("file content type = {}", file.getContentType());
            file.transferTo(new File(file.getOriginalFilename()));
        }

        //  storageService.store(file);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

     */


}

package com.shoplive.converter.application.video.service;

import com.shoplive.converter.application.video.repository.ResizedRepository;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class ConvertService {
    private static final Logger logger = LogManager.getLogger(ConvertService.class);

    private final ResizedRepository resizedRepository;

    public ConvertService(
            ResizedRepository resizedRepository
    ){
        this.resizedRepository = resizedRepository;
    }

    public String convertVideo(MultipartFile file, String originalSaveName) throws IOException {
        String uploadPath = "/Users/macintoshhd/Desktop/upload/";
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String uuid = UUID.randomUUID().toString();
        String resizedSaveName = uuid + "." + extension;
        File resizedPathFile = new File(uploadPath);

        if(!resizedPathFile.exists()){ // 디렉토리가 존재하지 않는다면.
            resizedPathFile.mkdirs(); // 디렉토리를 생성한다.
        }

        FFmpeg ffmpeg = new FFmpeg("/usr/local/bin/ffmpeg");  // ffmpeg 리눅스 경로
        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");  // ffprobe 리눅스 경로

        FFmpegProbeResult probeResult = ffprobe.probe(uploadPath + originalSaveName);
        int width = probeResult.getStreams().get(0).width;
        int height = probeResult.getStreams().get(0).height;
        double proportion = (double) width / 360;
        double resizedHeight = (double) height / proportion;

        logger.info("original video width = {}, original video height = {}", width, height);
        logger.info("resized video width = {}, resized video height = {}", 360, resizedHeight);


        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(uploadPath + originalSaveName) // 파일경로
                .overrideOutputFiles(true) // 오버라이드
                .addOutput(uploadPath + resizedSaveName) // 저장 경로
                .setVideoResolution(360, (int) resizedHeight) // 동영상 해상도
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // ffmpeg 빌더 실행 허용
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        return resizedSaveName;
    }
}

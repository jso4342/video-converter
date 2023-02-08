package com.shoplive.converter.application.video.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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
    private final String uploadPath;
    private final String ffmpegPath;
    private final String ffprobePath;
    public ConvertService(
            @Value("${custom.path.upload}") String uploadPath,
            @Value("${custom.path.ffmpeg}") String ffmpegPath,
            @Value("${custom.path.ffprobe}") String ffprobePath
    ){
        this.uploadPath = uploadPath;
        this.ffmpegPath = ffmpegPath;
        this.ffprobePath = ffprobePath;
    }

    public String convertVideo(MultipartFile file, String originalSaveName) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String uuid = UUID.randomUUID().toString();
        String resizedSaveName = uuid + "." + extension;
        File resizedPathFile = new File(uploadPath);

        if(!resizedPathFile.exists()){
            resizedPathFile.mkdirs();
        }

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FFmpegProbeResult probeResult = ffprobe.probe(uploadPath + originalSaveName);
        int width = probeResult.getStreams().get(0).width;
        int height = probeResult.getStreams().get(0).height;
        double proportion = (double) width / 360;
        double resizedHeight = (double) height / proportion;

        logger.info("original video width = {}, original video height = {}", width, height);
        logger.info("resized video width = {}, resized video height = {}", 360, resizedHeight);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(uploadPath + originalSaveName)
                .overrideOutputFiles(true)
                .addOutput(uploadPath + resizedSaveName)
                .setVideoResolution(360, (int) resizedHeight)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        return resizedSaveName;
    }
}

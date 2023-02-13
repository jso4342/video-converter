package com.shoplive.converter.application.video.service;

import com.shoplive.converter.application.video.domain.Video;
import com.shoplive.converter.application.video.domain.VideoData;
import com.shoplive.converter.application.video.dto.FileDataDto.*;
import com.shoplive.converter.application.video.dto.ConvertDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.repository.VideoRepository;
import com.shoplive.converter.core.exception.ErrorCode;
import com.shoplive.converter.core.exception.customException.VideoNotFoundException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class VideoService {
    private static final Logger logger = LogManager.getLogger(VideoService.class);

    private final VideoRepository videoRepository;
    private final String uploadPath;
    private final String ffmpegPath;
    private final String ffprobePath;
    private final int PORT;
    public VideoService(
            VideoRepository videoRepository,
            @Value("${custom.path.upload}") String uploadPath,
            @Value("${custom.path.ffmpeg}") String ffmpegPath,
            @Value("${custom.path.ffprobe}") String ffprobePath,
            @Value("${server.port}") int PORT
    ){
        this.videoRepository = videoRepository;
        this.uploadPath = uploadPath;
        this.ffmpegPath = ffmpegPath;
        this.ffprobePath = ffprobePath;
        this.PORT = PORT;
    }

    public VideoResponse convertVideo(String title, MultipartFile file) throws IOException {
        String originalName = uploadVideo(file);
        VideoData original = getOriginal(originalName);

        String thumbnailUrl = getThumbnailUrl(file, originalName);

        ConverterSetting setting = setConverter(file, originalName);
        VideoData resized = getResized(setting);

        VideoResponse response = VideoResponse.from(
                videoRepository.save(new Video (title,  original, resized, thumbnailUrl)));

        convert(file, setting);

        return response;
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        FileNameDto fileName = FileNameDto.from(file);
        double fileSize = file.getSize();
        double fileSizeMB = (fileSize / (1024 * 1024));

        logger.debug("file orginal name = {}", fileName.fileName());
        logger.debug("file size = {} MB", fileSizeMB);

        file.transferTo(new File(uploadPath + fileName.newName()));

        return fileName.newName();
    }

    public VideoData getOriginal(String savedName) throws IOException {
        logger.info("ffprobe path is = {}", ffprobePath);
        logger.info("upload path is = {}", uploadPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FileSizeDto fileSizeDto = FileSizeDto.from(ffprobe.probe(uploadPath + savedName));
        String videoUrl = "http://localhost:" + PORT + "/imagePath/" + savedName;

        return new VideoData(fileSizeDto.width(), fileSizeDto.height(), videoUrl);
    }

    public VideoData getResized(ConverterSetting setting) throws IOException {
        String videoUrl = "http://localhost:" + PORT + "/imagePath/" + setting.resizedName();

        return new VideoData(360, setting.newHeight(), videoUrl);
    }

    public String getThumbnailUrl(MultipartFile file, String originalName) throws IOException {
        File thumbnailPathFile = new File(uploadPath + "/thumbnail/");
        if(!thumbnailPathFile.exists()){
            thumbnailPathFile.mkdirs();
        }

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);
        String thumbnailName = FilenameUtils.removeExtension(originalName) + "_thumbnail.jpg";

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(uploadPath + originalName)
                .addExtraArgs("-ss", "00:00:00")
                .addOutput(uploadPath + "/thumbnail/" + thumbnailName)
                .setFrames(1)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        return "http://localhost:" + PORT + "/imagePath/thumbnail/" + thumbnailName;
    }

    public ConverterSetting setConverter(MultipartFile file, String originalName) throws IOException {
        FFprobe ffprobe = new FFprobe(ffprobePath);
        FileNameDto fileNameDto = FileNameDto.from(file);
        FileSizeDto fileSizeDto = FileSizeDto.from(ffprobe.probe(uploadPath + originalName));
        ConverterSetting setting = ConverterSetting.from(originalName, fileNameDto, fileSizeDto);

        logger.info(":::: originalName = {}", originalName);
        logger.info(":::: setting originalName = {}", setting.originalName());
        logger.info(":::: setting resizedName = {}", setting.resizedName());
        logger.info("original video width = {}, original video height = {}", fileSizeDto.width(), fileSizeDto.height());
        logger.info("resized video width = {}, resized video height = {}", 360, setting.newHeight());

        return setting;
    }

    @Async
    public void convert(MultipartFile file, ConverterSetting setting) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(uploadPath + setting.originalName())
                .overrideOutputFiles(true)
                .addOutput(uploadPath + setting.resizedName())
                .setVideoWidth(360)
                .setVideoHeight(setting.newHeight())
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    @Transactional(readOnly = true)
    public VideoResponse getVideoById(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("video id" + videoId, ErrorCode.VIDEO_NOT_FOUND));

        return VideoResponse.from(video);
    }
}

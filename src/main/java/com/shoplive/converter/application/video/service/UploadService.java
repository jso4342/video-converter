package com.shoplive.converter.application.video.service;

import com.shoplive.converter.application.video.domain.Original;
import com.shoplive.converter.application.video.domain.Resized;
import com.shoplive.converter.application.video.dto.OriginalDto.*;
import com.shoplive.converter.application.video.dto.ResizedDto.*;
import com.shoplive.converter.application.video.repository.OriginalRepository;
import com.shoplive.converter.application.video.repository.ResizedRepository;
import com.shoplive.converter.core.exception.ErrorCode;
import com.shoplive.converter.core.exception.customException.UnsupportedFormatException;
import net.bramp.ffmpeg.FFprobe;
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
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.UUID;

@Service
@Transactional
public class UploadService {
    private static final Logger logger = LogManager.getLogger(UploadService.class);
    private final OriginalRepository originalRepository;
    private final ResizedRepository resizedRepository;
    private final String uploadPath;
    private final String ffprobePath;
    private final int port;

    public UploadService(
            OriginalRepository originalRepository,
            ResizedRepository resizedRepository,
            @Value("${custom.path.upload}") String uploadPath,
            @Value("${custom.path.ffprobe}") String ffprobePath,
            @Value("${server.port}") int port
    ){
        this.originalRepository = originalRepository;
        this.resizedRepository = resizedRepository;
        this.uploadPath = uploadPath;
        this.ffprobePath = ffprobePath;
        this.port = port;
    }

    public String uploadOriginal(MultipartFile file){
        String originalSaveName = null;
        try {
            String fileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(fileName);
            String uuid = UUID.randomUUID().toString();
            double fileSize = file.getSize();
            double fileSizeMB = (fileSize / (1024 * 1024));

            originalSaveName = uuid + "." + extension;
            logger.debug("file orginal name = {}", fileName);
            logger.debug("file size = {} MB", fileSizeMB);

            if (!extension.toUpperCase().equals("MP4")) {
                logger.error("file must be in MP4 format. requested file is in = {}", extension);
                throw new UnsupportedFormatException(
                        "file must be in MP4.", ErrorCode.UNSUPPORTED_FORMAT);
                // HttpResponse 로 안들어감
            }

            file.transferTo(new File(originalSaveName));
        } catch (Exception e){ // 더 나은 예외 처리가 있나?
            e.printStackTrace();
        }
        return originalSaveName;
    }

    public OriginalResponse saveOriginal(MultipartFile file, String originalSaveName) throws IOException {
        FFprobe ffprobe = new FFprobe(ffprobePath);
        FFmpegProbeResult probeResult = ffprobe.probe(uploadPath + originalSaveName);

        long fileSize = file.getSize();
        int width = probeResult.getStreams().get(0).width;
        int height = probeResult.getStreams().get(0).height;
        String videoUrl = "http://localhost:" + port + "/imagePath/" + originalSaveName;

        Original original = new Original(fileSize, width, height, videoUrl);
        originalRepository.save(original);

        return OriginalResponse.from(original);
    }

    public ResizedResponse saveResized(String resizedSaveName) throws IOException {
        FFprobe ffprobe = new FFprobe(ffprobePath);
        FFmpegProbeResult probeResult = ffprobe.probe(uploadPath + resizedSaveName);

        long fileSize = Files.size(Paths.get(uploadPath + resizedSaveName));
        int width = probeResult.getStreams().get(0).width;
        int height = probeResult.getStreams().get(0).height;
        String videoUrl = "http://localhost:" + port + "/imagePath/" + resizedSaveName;

        Resized resized = new Resized(fileSize, width, height, videoUrl);
        resizedRepository.save(resized);

        return ResizedResponse.from(resized);
    }
}

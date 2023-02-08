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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

    @Value("${server.port}")
    private int port;

    public UploadService(
            OriginalRepository originalRepository,
            ResizedRepository resizedRepository
    ){
        this.originalRepository = originalRepository;
        this.resizedRepository = resizedRepository;
    }

    public String uploadOriginal(MultipartFile file){
        String originalSaveName = null;
        try {
            String fileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(fileName);
            String uuid = UUID.randomUUID().toString();
            originalSaveName = uuid + "." + extension;
            double fileSize = file.getSize();
            double fileSizeMB = (fileSize / (1024 * 1024));

            logger.debug("file orginal name = {}", fileName);
            logger.debug("file size = {} MB", fileSizeMB);

            if (!extension.toUpperCase().equals("MP4")) {
                logger.error("file must be in MP4 format");
                throw new UnsupportedFormatException("file must be in MP4, but uploaded file is in : " + extension, ErrorCode.UNSUPPORTED_FORMAT);
            }

            if (fileSizeMB > 100) { // 조건에 부합하지 않는 경우 에러를 응답합니다.
                logger.error("file must not exceed 100MB in size");
            }

            file.transferTo(new File(originalSaveName));
        } catch (Exception e){ // 이거 말고 더 자세하게
            e.printStackTrace();
        }
        return originalSaveName;
    }



    public OriginalResponse saveOriginal(MultipartFile file, String originalSaveName) throws IOException {
        String uploadPath = "/Users/macintoshhd/Desktop/upload/";

        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");  // ffprobe 리눅스 경로
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
        String uploadPath = "/Users/macintoshhd/Desktop/upload/";
        String uuid = UUID.randomUUID().toString();

        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");  // ffprobe 리눅스 경로
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

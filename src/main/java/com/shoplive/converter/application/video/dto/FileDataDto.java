package com.shoplive.converter.application.video.dto;

import com.shoplive.converter.application.video.service.VideoService;
import com.shoplive.converter.core.exception.ErrorCode;
import com.shoplive.converter.core.exception.customException.UnsupportedFormatException;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileDataDto {
    private static final Logger logger = LogManager.getLogger(FileDataDto.class);
    public record FileNameDto(
            String fileName,
            String extension,
            String newName
    ){
        public static FileNameDto from (MultipartFile file) {
            String fileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(fileName);
            String uuid = UUID.randomUUID().toString();
            String newName = uuid + "." + extension;
            validateFileFormat(extension);

            return new FileNameDto(
                    fileName,
                    extension,
                    newName
            );
        }

        public static void validateFileFormat(String extension){
            if (!extension.toUpperCase().equals("MP4")){
                logger.error("file must be in MP4 format. requested file is in = {}", extension);
                throw new UnsupportedFormatException("file must be in MP4.", ErrorCode.UNSUPPORTED_FORMAT);
            }
        }
    }

    public record FileSizeDto(
            Integer width,
            Integer height
    ){
        public static FileSizeDto from (FFmpegProbeResult probeResult) {
            int width = probeResult.getStreams().get(0).width;
            int height = probeResult.getStreams().get(0).height;

            return new FileSizeDto(
                    width,
                    height
            );
        }
    }
}

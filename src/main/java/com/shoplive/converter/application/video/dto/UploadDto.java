package com.shoplive.converter.application.video.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class UploadDto {
    public record UploadRequest(
            @NotNull
            MultipartFile file,
            @NotBlank
            String title
    ){ }

    public record UploadResponse(
            MultipartFile file,
            String title
    ){ }

    public record ConverterSetting(
            Integer newHeight,
            String originalName,
            String resizedName
    ){
        public static ConverterSetting from (
                String originalNewName,
                FileDataDto.FileNameDto fileNameDto,
                FileDataDto.FileSizeDto fileSizeDto
        ) {
            double proportion = (double) fileSizeDto.width() / 360;
            double resizedHeight = (double) fileSizeDto.height() / proportion;
            int newHeight = (int) resizedHeight;
            String originalName = originalNewName;
            String resizedName = fileNameDto.newName();


            return new ConverterSetting(
                    newHeight,
                    originalName,
                    resizedName
            );
        }
    }
}

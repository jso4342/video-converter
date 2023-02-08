package com.shoplive.converter.application.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

import com.shoplive.converter.application.video.dto.OriginalDto.*;
import com.shoplive.converter.application.video.dto.ResizedDto.*;
import com.shoplive.converter.application.video.dto.VideoDto.*;
import com.shoplive.converter.application.video.service.ConvertService;
import com.shoplive.converter.application.video.service.UploadService;
import com.shoplive.converter.application.video.service.VideoService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class VideoControllerTest {
    private static final Logger logger = LogManager.getLogger(ConvertService.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ConvertService convertService;

    @Autowired
    private ObjectMapper objectMapper;

    private final long originalId = 1L;
    private final long resizedId = 1L;
    private final long videoId = 1L;

    @Value("${server.port}")
    private int port;

    @BeforeAll
    void setUp() throws IOException {

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "sample.mp4",
                "mp4",
                new FileInputStream("/Users/macintoshhd/Desktop/gitgit/shoplive/src/test/resources/static/sample.mp4"));


        String title = "sample test";

        String originalSaveName = uploadService.uploadOriginal(mockMultipartFile);
        logger.info("::: original save Name is = {} ", originalSaveName);
        logger.info("::: mock file is = {} ", mockMultipartFile);

        OriginalResponse originalResponse = uploadService.saveOriginal(mockMultipartFile, originalSaveName);

        String thumbnailName = uploadService.exportThumbnail(mockMultipartFile, originalSaveName);
        String thumbnailUrl = "http://localhost:" + port + "/static/imagePath/thumbnail/" + thumbnailName;

        String resizedSaveName = convertService.convertVideo(mockMultipartFile, originalSaveName);
        ResizedResponse resizedResponse = uploadService.saveResized(resizedSaveName);

        VideoRequest request = new VideoRequest(title,  originalResponse.id(), resizedResponse.id(), thumbnailUrl);
        videoService.storeVideo(request);
    }

    @DisplayName("영상을 저장할 수 있다.")
    @Test
    void storeVideoTest() throws Exception {
        String fileName = "sample";
        String contentType = "mp4";
        String filePath = "/Users/macintoshhd/Desktop/src/test/resources/static/sample.mp4";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "sample.mp4",
                "mp4",
                new FileInputStream("/Users/macintoshhd/Desktop/gitgit/shoplive/src/test/resources/static/sample.mp4"));
        String title = "sample test";

        String originalSaveName = uploadService.uploadOriginal(mockFile);
        OriginalResponse originalResponse = uploadService.saveOriginal(mockFile, originalSaveName);

        String thumbnailName = uploadService.exportThumbnail(mockFile, originalSaveName);
        String thumbnailUrl = "http://localhost:" + port + "/static/imagePath/thumbnail/" + thumbnailName;

        String resizedSaveName = convertService.convertVideo(mockFile, originalSaveName);
        ResizedResponse resizedResponse = uploadService.saveResized(resizedSaveName);

        VideoRequest request = new VideoRequest(title,  originalResponse.id(), resizedResponse.id(), thumbnailUrl);

        mockMvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("video-save",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("videoTitle"),
                                fieldWithPath("originalId").type(JsonFieldType.NUMBER).description("originalId"),
                                fieldWithPath("resizedId").type(JsonFieldType.NUMBER).description("resizedId"),
                                fieldWithPath("thumbnailUrl").type(JsonFieldType.STRING).description("thumbnailUrl")
                        )));
    }

    @DisplayName("영상을 단건 조회할 수 있다.")
    @Test
    void getVideoByIdTest() throws Exception {
        mockMvc.perform(get("/video/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

package com.shoplive.converter.application.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

import com.shoplive.converter.application.video.dto.UploadDto;
import com.shoplive.converter.application.video.dto.VideoDto.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class VideoControllerTest {
    private static final Logger logger = LogManager.getLogger(VideoControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    private final long videoId = 1L;

    @BeforeAll
    void setUp() throws IOException {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "sample.mp4",
                "mp4",
                new FileInputStream("/Users/macintoshhd/Desktop/gitgit/shoplive/src/test/resources/static/sample.mp4"));


        String title = "sample test";

        VideoResponse response = videoService.convertVideo(title, file);
    }

    @DisplayName("영상을 단건 조회할 수 있다.")
    @Test
    void getVideoByIdTest() throws Exception {
        mockMvc.perform(get("/video/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

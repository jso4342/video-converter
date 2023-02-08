package com.shoplive.converter.application.video.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class VideoTest {
    @DisplayName("제목은 빈값일 수 없다.")
    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    void titleCannotBeNull(String title) {
        Original original = new Original(1L, 6000L, 600, 300, "test/url/1");
        Resized resized = new Resized(1L, 6000L, 360, 180, "test/url/2");
        String thumbnailUrl = "testThumbnailUrl";

        assertThrows(IllegalArgumentException.class,
                () -> new Video(title, original, resized, thumbnailUrl));
    }

    @DisplayName("썸네일 주소는 빈값일 수 없다.")
    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    void thumbnailUrlCannotBeNull(String thumbnailUrl) {
        Original original = new Original(1L, 6000L, 600, 300, "test/url/1");
        Resized resized = new Resized(1L, 6000L, 360, 180, "test/url/2");
        String title = "testTitle";

        assertThrows(IllegalArgumentException.class,
                () -> new Video(title, original, resized, thumbnailUrl));
    }

    static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}

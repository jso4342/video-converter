package com.shoplive.converter.application.video.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResizedTest {
    @DisplayName("파일 사이즈는 0 in byte 이하일 수 없다.")
    @Test
    void fileSizeCannotBeZero(){
        assertThrows(IllegalArgumentException.class,
                () -> new Resized(1L, 0L, 600, 300, "test/url"));
    }

    @DisplayName("영상의 너비는 0 이하일 수 없다.")
    @Test
    void widthCannotBeZero(){
        assertThrows(IllegalArgumentException.class,
                () -> new Resized(1L, 3000L, 0, 300, "test/url"));
    }

    @DisplayName("영상의 높이 이하일 수 없다.")
    @Test
    void heightCannotBeZero(){
        assertThrows(IllegalArgumentException.class,
                () -> new Resized(1L, 3000L, 600, 0, "test/url"));
    }

    @DisplayName("영상의 주소는 빈값일 수 없다.")
    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    void NameCannotNull(String vidoUrl){
        assertThrows(IllegalArgumentException.class,
                () -> new Resized(1L, 3000L, 600, 300, vidoUrl));
    }

    static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}

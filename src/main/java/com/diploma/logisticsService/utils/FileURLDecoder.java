package com.diploma.logisticsService.utils;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
public class FileURLDecoder {
    public String getPathToResource(String fileName) {
        URL url = FileURLDecoder.class.getResource("/" + fileName);
        String path = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
        return path;
    }
}

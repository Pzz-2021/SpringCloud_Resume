package com.resume.parse.utils;

import java.io.File;

public class URLUtil {
    public static String getFileName(String urlString) {
        File tempFile = new File(urlString);
        return tempFile.getName();
    }

    public static String getFileBaseName(String urlString) {
        String fileName = getFileName(urlString);
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static String getFileExtension(String urlString) {
        String fileName = getFileName(urlString);
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}

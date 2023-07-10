package com.resume.parse.utils;

import org.apache.commons.io.FilenameUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class URLUtil {
    public static String getFileName(String urlString) {
        try {
            URL url = new URL(urlString);

//            System.out.println(FilenameUtils.getBaseName(url.getPath())); // -> file
//            System.out.println(FilenameUtils.getExtension(url.getPath())); // -> xml
//            System.out.println(FilenameUtils.getName(url.getPath())); // -> file.xml

            return FilenameUtils.getName(url.getPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFileBaseName(String urlString) {
        try {
            URL url = new URL(urlString);

            return FilenameUtils.getBaseName(url.getPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFileExtension(String urlString) {
        try {
            URL url = new URL(urlString);

            return FilenameUtils.getExtension(url.getPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}

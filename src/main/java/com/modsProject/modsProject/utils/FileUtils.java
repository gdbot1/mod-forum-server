package com.modsProject.modsProject.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static String uploadDirectory = "public/images/creators/";

    public static String extractExtension(String path) {
        if (path != null && path.contains(".")) {
            return path.substring(path.lastIndexOf("."));
        }

        return "";
    }

    public static boolean extensionIsImage(String extension) {
        return extension.equals(".png") || extension.equals(".svg") || extension.equals(".webp") || extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".gif");
    }

    public static void saveMultipartFile(Path path, MultipartFile file) throws IOException {
        file.transferTo(path);
    }

    public static void removeFile(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    public static void changeMultipartFile(Path path, MultipartFile file) throws IOException {
        removeFile(path);
        saveMultipartFile(path, file);
    }
}
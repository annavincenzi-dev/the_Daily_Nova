package dev.annavincenzi.the_daily_nova.utils;

public class StringManipulation {
    public static String getFileExtension(String nameFile) {
        int dotIndex = nameFile.indexOf('.');
        String extension = nameFile.substring(dotIndex + 1);
        return extension;
    }
}

package com.tproject.services;

public interface CsvBuildService {
//    String buildFile(String path);
    byte[] createFile();
    String saveFile(byte[] fileData, String path);
}

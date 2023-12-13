package com.tproject.services;

public interface HtmlBuildService {
    byte[] createHtmlFile();
    String saveHtmlFile(byte[] fileContent);

    String convertHtmlToPdf(String htmlFilePath);
}

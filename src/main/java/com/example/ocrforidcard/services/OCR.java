package com.example.ocrforidcard.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface OCR {
    public Map<String, String> performOCR(MultipartFile file);
    public double calculateConfidence(Map<String, String> ocrData);
}

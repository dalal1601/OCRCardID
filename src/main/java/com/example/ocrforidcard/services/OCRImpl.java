package com.example.ocrforidcard.services;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class OCRImpl implements OCR {
    private Tesseract tesseract;

    public OCRImpl() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng+fra+ara");
    }

    @Override
    public Map<String, String> performOCR(MultipartFile file) {
        Map<String, String> ocrData = new HashMap<>();

        try {
            // Convert the MultipartFile to a BufferedImage
            BufferedImage image = ImageIO.read(file.getInputStream());

            // Perform OCR
            String result = tesseract.doOCR(image);

            // Process the result (for simplicity, let's assume it contains structured text)
            ocrData.put("rawText", result);
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }

        return ocrData;
    }

    @Override
    public double calculateConfidence(Map<String, String> ocrData) {
        // Confidence calculation based on OCR output quality (optional implementation)
        return 92.3; // Returning a mock confidence value
    }
}

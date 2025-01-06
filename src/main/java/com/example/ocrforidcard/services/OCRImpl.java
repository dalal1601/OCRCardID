package com.example.ocrforidcard.services;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class OCRImpl implements OCR {
    private Tesseract tesseract;

    public OCRImpl() {
        // Initialize Tesseract
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng+fra+ara");
    }

    @Override
    public Map<String, String> performOCR(MultipartFile file) {
        Map<String, String> ocrData = new HashMap<>();

        try {
            // Convert MultipartFile to BufferedImage
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Convert BufferedImage to Mat for OpenCV processing
            Mat matImage = bufferedImageToMat(originalImage);

            // Preprocess the image using OpenCV
            Mat preprocessedImage = preprocessImage(matImage);

            // Convert Mat back to BufferedImage for Tesseract OCR
            BufferedImage processedBufferedImage = matToBufferedImage(preprocessedImage);

            // Perform OCR
            String result = tesseract.doOCR(processedBufferedImage);

            // Process the result (assume it contains structured text)
            ocrData.put("rawText", result);
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }

        return ocrData;
    }

    private Mat preprocessImage(Mat image) {
        // Convert to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Apply adaptive thresholding
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(gray, binary, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);

        // Denoise the image
        Mat denoised = new Mat();
        Imgproc.medianBlur(binary, denoised, 3);

        return denoised;
    }

    private Mat bufferedImageToMat(BufferedImage bi) {
        byte[] pixels = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, pixels);
        return mat;
    }

    private BufferedImage matToBufferedImage(Mat mat) throws IOException {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", mat, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }

    @Override
    public double calculateConfidence(Map<String, String> ocrData) {
        // Example confidence value (adjust based on OCR output quality)
        return 92.3;
    }
}

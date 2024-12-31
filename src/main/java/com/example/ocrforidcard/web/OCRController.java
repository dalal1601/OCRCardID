package com.example.ocrforidcard.web;

import com.example.ocrforidcard.dao.entities.CardData;
import com.example.ocrforidcard.dao.repositories.CardDataRepository;
import com.example.ocrforidcard.services.OCR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin("*")
public class OCRController {

        @Autowired
        private OCR ocrService;

        @Autowired
        private CardDataRepository cardDataRepository;

        // Endpoint to upload an image and perform OCR
        @PostMapping("/upload")
        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
            try {
                Map<String, String> ocrResult = ocrService.performOCR(file);
                double confidence = ocrService.calculateConfidence(ocrResult);

                // Debugging the OCR result to check the extracted values
                System.out.println("OCR Result: " + ocrResult);

                // Check if the OCR result is not empty
                if (ocrResult == null || ocrResult.isEmpty()) {
                    return new ResponseEntity<>("OCR failed to extract text from the image", HttpStatus.BAD_REQUEST);
                }

                // Create a new CardData entity
                CardData cardData = new CardData();
                cardData.setFirstName(ocrResult.get("firstname"));
                cardData.setLastName(ocrResult.get("lastname"));
                cardData.setBirthDate(ocrResult.get("birthDate"));
                cardData.setCardNumber(ocrResult.get("cardNumber"));
                cardData.setAddress(ocrResult.get("address"));
                cardData.setConfidence(confidence);

                // Save the data to the database
                cardDataRepository.save(cardData);

                return new ResponseEntity<>("OCR successful. Confidence: " + confidence + "%", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error processing the image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    // Endpoint to validate and update OCR data
        @PostMapping("/validate")
        public ResponseEntity<String> validateData(@RequestBody CardData data) {
            try {
                // Save the validated data to the database
                cardDataRepository.save(data);
                return new ResponseEntity<>("Data validated and saved successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error validating the data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        //getall....1 - FULL STACK (SPRING BOOT 3 - ANGULAR 16) - PREPARER BACKEND vid1


}

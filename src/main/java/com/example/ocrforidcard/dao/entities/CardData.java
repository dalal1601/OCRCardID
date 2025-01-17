package com.example.ocrforidcard.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CardDataId;

    private String firstName;
    private String lastName;
    private String birthDate;
    private String address;
    private String cardNumber;

    private Double confidence; // Confidence percentage for OCR results

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}


package com.example.ocrforidcard.dao.repositories;

import com.example.ocrforidcard.dao.entities.CardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDataRepository  extends JpaRepository<CardData, Long> {

}

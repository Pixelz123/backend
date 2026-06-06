package com.prodrag.backend.repositories;

import org.springframework.stereotype.Repository;

import com.prodrag.backend.entities.Document;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,UUID> {
    
}

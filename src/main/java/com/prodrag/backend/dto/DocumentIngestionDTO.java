package com.prodrag.backend.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentIngestionDTO {
    private UUID documentId;
    private String tenantId;
    private String fileName;
    private String storagePath;
}

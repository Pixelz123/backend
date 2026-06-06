package com.prodrag.backend.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prodrag.backend.entities.Document;
import com.prodrag.backend.entities.User;
import com.prodrag.backend.repositories.DocumentRepository;
import com.prodrag.backend.services.cmp;
import com.rabbitmq.client.RpcClient.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/documents")
@RequiredArgsConstructor
public class ApiController {
    private final DocumentRepository documentRepository;
    
    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<?> uploadDocument(
       @RequestHeader("X-Tenant-ID") String tenantId,
       @RequestParam("file") MultipartFile file
    ){
         if (file.isEmpty() || tenantId==null || tenantId.trim().isEmpty()){
            return ResponseEntity.badRequest().body("Missing tenant id or corrupt file");
         }
         Path targetLocation = null;
         try{
            UUID documentId= UUID.randomUUID();
            String originalFileName=file.getOriginalFilename();
            Path tenentStorageDir= Path.of(uploadDir,tenantId).toAbsolutePath().normalize();
            Files.createDirectories(tenentStorageDir);
            targetLocation = tenentStorageDir.resolve(documentId+"_"+originalFileName);
            Files.copy(file.getInputStream(),targetLocation);
            Document docMetadata = Document.builder()
                                      .id(documentId)
                                      .tenant_id(tenantId)
                                      .file_name(originalFileName)
                                      .storage_path(targetLocation.toString())
                                      .status("PENDING")
                                      .build();
            documentRepository.save(docMetadata);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                  "Document id ", documentId.toString(),
                  "status","PENDING"
            ));
         } catch (Exception ex){
            if (targetLocation != null) {
               try {
                  Files.deleteIfExists(targetLocation);
               } catch (Exception ignored) {} // Failsafe, don't crash if delete fails
            }
            System.out.println(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
         }
    }
    

}

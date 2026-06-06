package com.prodrag.backend.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
   @Id
   private UUID id;
   @Column(name= "tenant_id",nullable=false)
   private String tenant_id;
   @Column(name= "file_name" ,nullable=false)
   private String file_name;
   @Column(name = "storage_path", nullable = false)
   private String storage_path;
   @Column(name= "status" , nullable = false)
   private String status;
}

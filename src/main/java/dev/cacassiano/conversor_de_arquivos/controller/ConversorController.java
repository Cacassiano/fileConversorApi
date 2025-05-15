package dev.cacassiano.conversor_de_arquivos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.BadRequestException;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.UnresponsableEntityException;
import dev.cacassiano.conversor_de_arquivos.service.ConversionService;
import dev.cacassiano.conversor_de_arquivos.service.ConversionServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class ConversorController {
    
    private ConversionService service;

    public ConversorController(ConversionServiceImpl service) {
        this.service = service;
    }

    @SuppressWarnings("resource")
    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> convertFile(MultipartFile file, String target) throws IOException, BadRequestException, UnresponsableEntityException {
        if(file ==null || target == null || target.length() < 1 || file.getSize() < 1 ) {
            return ResponseEntity.badRequest().build();
        }
        String contentType = service.targetContentType(target);
        File convertedFile = service.convertSingleFile(file, target);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename = "+UUID.randomUUID().toString()+"."+target+";")
                .header("Content-Type", contentType)
                .body(new FileInputStream(convertedFile).readAllBytes());
                
    }
    

}

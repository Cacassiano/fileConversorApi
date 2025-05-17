package dev.cacassiano.conversor_de_arquivos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.cacassiano.conversor_de_arquivos.dtos.SuportedTypesDTO;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.BadRequestException;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.UnresponsableEntityException;
import dev.cacassiano.conversor_de_arquivos.service.ConversionService;
import dev.cacassiano.conversor_de_arquivos.service.ConversionServiceImpl;
import dev.cacassiano.conversor_de_arquivos.service.SuportedTypes;

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
            throw new BadRequestException("Missing information");
        }
        String contentType = service.targetContentType(target, file.getOriginalFilename());
        File convertedFile = service.convertSingleFile(file, target, contentType);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename = converted."+target+";")
                .header("Content-Type", contentType)
                .body(new FileInputStream(convertedFile).readAllBytes());
                
    }
    
    @GetMapping(value = "/formats")
    public ResponseEntity<SuportedTypesDTO> getSuportedTypes() {
        SuportedTypes inputs = SuportedTypes.IMAGE_INPUT_TYPES;
        SuportedTypes outputs = SuportedTypes.IMAGE_OUTPUT_TYPES;
        return ResponseEntity
            .ok(new SuportedTypesDTO(inputs.getAllInputs(), outputs.getAllOutputs()));
    }
    @SuppressWarnings("resource")
    @PostMapping(value = "/convert/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> convertMany(MultipartFile[] files, String target) throws BadRequestException, UnresponsableEntityException, FileNotFoundException, IOException {
        if (files.length < 1 || target == null) {
            throw new BadRequestException("Missing information");
        }

        File zip = service.convertManyAndZip(files, target, service.targetContentType(target, "."+target));
        byte[] zipBytes = new FileInputStream(zip).readAllBytes();
        zip.delete(); 
        return ResponseEntity
            .ok()
            .header("Content-Type", "application/zip")
            .header("Content-Disposition", " attachment; filename = convertedFiles.zip;")
            .body(zipBytes);

    }

}

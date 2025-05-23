package dev.cacassiano.conversor_de_arquivos.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.BadRequestException;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.UnresponsableEntityException;

public interface ConversionService {
    public File convertSingleFile(MultipartFile file, String target, String contentType) throws UnresponsableEntityException;
    public File convertManyAndZip(MultipartFile[] files, String target, String contentType) throws UnresponsableEntityException, IOException;  
    public String targetContentType(String target, String originalFileName) throws BadRequestException;
}

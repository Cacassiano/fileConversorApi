package dev.cacassiano.conversor_de_arquivos.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.BadRequestException;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.UnresponsableEntityException;

public interface ConversionService {
    public File convertSingleFile(MultipartFile file, String target) throws UnresponsableEntityException;
    public File convertManyAndZip(MultipartFile[] files, String target) throws UnresponsableEntityException;  
    public String targetContentType(String target) throws BadRequestException;
}

package dev.cacassiano.conversor_de_arquivos.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.BadRequestException;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.UnresponsableEntityException;

@Service
public class ConversionServiceImpl implements ConversionService{

    

    @Override
    public File convertSingleFile(MultipartFile file, String target) throws UnresponsableEntityException {
        File targetFile = new File("fileTemp."+target);
        targetFile.deleteOnExit();
        try{
            try(FileOutputStream output = new FileOutputStream(targetFile)){
                output.write(file.getInputStream().readAllBytes());
            }
        } catch (IOException e) {
            throw new UnresponsableEntityException("Unresponsable entity/ error while converting file: "+file.getOriginalFilename());
        }
        return targetFile;
    
    }

    @SuppressWarnings("resource")
    @Override
    public File convertManyAndZip(MultipartFile[] files, String target) throws UnresponsableEntityException {
        File zipTargetFile = new File("zipTempFile.zip");
        zipTargetFile.deleteOnExit();

        for(MultipartFile file: files){
            try(ZipOutputStream output = new ZipOutputStream(new FileOutputStream(zipTargetFile))){
                File convertedFile = convertSingleFile(file, target);
                ZipEntry entry = new ZipEntry(file.getOriginalFilename());
                entry.setExtra(new FileInputStream(convertedFile).readAllBytes());

                output.putNextEntry(entry);
                output.closeEntry();
            } catch (IOException e ) {
                throw new UnresponsableEntityException("Unresponsable entity/ error while converting file: "+file.getOriginalFilename());
            }
        }
        return zipTargetFile;
    }

    @Override
    public String targetContentType(String target) throws BadRequestException {
        SuportedTypes suportedTypes = SuportedTypes.IMAGE_TYPES;
        for (String i : suportedTypes.getTypes()) {
            if(target.equals(i)) {
                return "image/"+target;
            }
        }
        suportedTypes = SuportedTypes.FILE_TYPES;
        for (String i : suportedTypes.getTypes()) {
            if(target.equals(i)) {
                return "application/"+target;
            }
        }
        throw new BadRequestException("The type \""+target+"\" is not supported.");
    }

}

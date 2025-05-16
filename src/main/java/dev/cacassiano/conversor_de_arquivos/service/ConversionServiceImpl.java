package dev.cacassiano.conversor_de_arquivos.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;
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
        File targetFile = new File(OffsetDateTime.now().toString()+UUID.randomUUID()+"."+target);
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
    public File convertManyAndZip(MultipartFile[] files, String target) throws UnresponsableEntityException, IOException {
        File zipTargetFile = new File(UUID.randomUUID()+".zip");
        zipTargetFile.deleteOnExit();
        try(ZipOutputStream output = new ZipOutputStream(new FileOutputStream(zipTargetFile))){
            for(MultipartFile file: files){
                File convertedFile = convertSingleFile(file, target);
                ZipEntry entry = new ZipEntry(convertedFile.getName());
                
                output.putNextEntry(entry);
                output.write(new FileInputStream(convertedFile).readAllBytes());
            }
        } catch (IOException e ) {
            zipTargetFile.delete();
            throw new UnresponsableEntityException("error while converting one of the files");
        }
        return zipTargetFile;
    }
    private void userFileValidation(String originalFileName, String[] suporteds) throws BadRequestException {
        for (String i : suporteds) {
            if(originalFileName.endsWith("."+i)) {
                return;
            }
        }
        throw new BadRequestException("the file "+ originalFileName +" is not suported");
    }
    @Override
    public String targetContentType(String target, String originalFileName) throws BadRequestException {
        SuportedTypes suportedTypes = SuportedTypes.IMAGE_OUTPUT_TYPES;
        userFileValidation(originalFileName, suportedTypes.getAllInputs());
        for (String i : suportedTypes.getTypes()) {
            if(target.equals(i)) {
                return "image/"+target;
            }
        }
        suportedTypes = SuportedTypes.TEXT_OUTPUT_TYPES;
        for (String i : suportedTypes.getTypes()) {
            if(target.equals(i)) {
                return "application/"+target;
            }
        }
        throw new BadRequestException("The type \""+target+"\" is not supported for output.");
    }

}

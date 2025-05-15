package dev.cacassiano.conversor_de_arquivos.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.BadRequestException;
import dev.cacassiano.conversor_de_arquivos.errors.customExceptions.UnresponsableEntityException;

@RestControllerAdvice
class GlobalExceptionHandler extends ExceptionHandlerExceptionResolver {
    
    @ExceptionHandler(UnresponsableEntityException.class)
    public ResponseEntity<String> UnresponsableEntityExceptionHandler(UnresponsableEntityException e) {
        return ResponseEntity
            .unprocessableEntity()
            .body(e.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> BadRequestExceptionHandler(BadRequestException e) {
        return ResponseEntity
            .unprocessableEntity()
            .body(e.getMessage());
    }

}

package org.tasktracker.demo.taskservice.presentation.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tasktracker.demo.taskservice.application.dto.ErrorResponse;
import org.tasktracker.demo.taskservice.domain.exception.DataNotFoundException;
import org.tasktracker.demo.taskservice.domain.exception.TaskValidationException;

import java.nio.file.AccessDeniedException;

/**
 * Author: Artyom Aroyan
 * Date: 23.03.26
 * Time: 15:08:03
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(TaskValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(TaskValidationException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("ACCESS_DENIED", ex.getMessage()));
    }
}
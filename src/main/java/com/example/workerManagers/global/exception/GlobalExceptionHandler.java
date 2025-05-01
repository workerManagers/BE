package com.example.workerManagers.global.exception;

import com.example.workerManagers.domain.company.exception.CompanyException;
import com.example.workerManagers.domain.industrialaccident.exception.IndustrialAccidentException;
import com.example.workerManagers.domain.jobcode.exception.JobCodeException;
import com.example.workerManagers.domain.jobpost.exception.JobPostException;
import com.example.workerManagers.domain.resume.exception.ResumeException;
import com.example.workerManagers.domain.talentbookmark.exception.TalentBookmarkException;
import com.example.workerManagers.domain.bookmark.exception.BookmarkException;
import com.example.workerManagers.domain.auth.exception.TokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(com.example.workerManagers.domain.users.exception.UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(com.example.workerManagers.domain.users.exception.UserException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, String>> handleTokenException(TokenException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<Map<String, String>> handleCompanyException(CompanyException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IndustrialAccidentException.class)
    public ResponseEntity<Map<String, String>> handleIndustrialAccidentException(IndustrialAccidentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(JobCodeException.class)
    public ResponseEntity<Map<String, String>> handleJobCodeException(JobCodeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(JobPostException.class)
    public ResponseEntity<Map<String, String>> handleJobPostException(JobPostException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResumeException.class)
    public ResponseEntity<Map<String, String>> handleResumeException(ResumeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BookmarkException.class)
    public ResponseEntity<Map<String, String>> handleBookmarkException(BookmarkException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TalentBookmarkException.class)
    public ResponseEntity<Map<String, String>> handleTalentBookmarkException(TalentBookmarkException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
} 
package com.inventory.exception;
import com.inventory.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handle(Exception ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Unknown error";
        if (ex.getCause() != null && ex.getCause().getMessage() != null) msg = ex.getCause().getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error: " + msg, null));
    }
}

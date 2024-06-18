package ru.practicum.ewm.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.ForbiddenDataException;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.model.ApiError;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "An exception throws";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    @ExceptionHandler(IncorrectDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectDataException(IncorrectDataException e) {
        log.error("400: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "Incorrectly made request.";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    @ExceptionHandler(ConflictDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictDataException(ConflictDataException e) {
        log.error("409: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "Integrity constraint has been violated.";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoDataException.class)
    public ApiError handleNoDataException(NoDataException e) {
        log.error("404: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "The required object was not found.";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        log.error("400: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "The validation by annotation is not passed.";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    public ApiError handleServletRequestBindingException(ServletRequestBindingException e) {
        log.error("400: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "The validation by annotation in RequestParam is not passed.";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenDataException.class)
    public ApiError handleForbiddenDataException(ForbiddenDataException e) {
        log.error("409: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        String errorMessage = "For the requested operation the conditions are not met.";
        return apiErrorReturn(errorMessage, stackTrace, e);
    }

    private ApiError apiErrorReturn(String errorMessage, String stackTrace, Exception e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason(errorMessage)
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }
}

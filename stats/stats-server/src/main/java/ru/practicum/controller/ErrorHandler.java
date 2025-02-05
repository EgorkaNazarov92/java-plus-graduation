package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.expection.BadRequestExceptions;
import ru.practicum.error.model.ErrorResponse;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DateTimeParseException.class)
	ErrorResponse handleDateTimeParseException(DateTimeParseException e) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(e.getMessage()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		List<String> errors = new ArrayList<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.add(fieldName + " - " + errorMessage);
		});
		return new ErrorResponse(e.getStatusCode().value(), errors);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestExceptions.class)
	ErrorResponse handleBadRequestException(BadRequestExceptions e) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(e.getMessage()));
	}
}

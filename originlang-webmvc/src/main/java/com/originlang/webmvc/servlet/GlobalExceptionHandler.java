package com.originlang.webmvc.servlet;

import com.originlang.base.exception.Auth401Exception;
import com.originlang.webmvc.Result;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("GlobalExceptionHandler", e);
		BindingResult bindingResult = e.getBindingResult();
		StringBuffer sb = new StringBuffer();
		bindingResult.getFieldErrors().forEach((fieldError) -> {
			sb.append(fieldError.getDefaultMessage());
		});
		return Result.failed(sb.toString());
	}

	@ExceptionHandler(Auth401Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(Auth401Exception e) {
		// info level
		log.info("GlobalExceptionHandler", e);
		return Result.failed(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(Exception e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed(e.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(MissingServletRequestParameterException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed(e.getMessage());
	}

	@ExceptionHandler({ org.springframework.web.servlet.NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(org.springframework.web.servlet.NoHandlerFoundException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed("api 404 not found");
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(ValidationException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed("validate not pass");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(IllegalArgumentException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed(e.getMessage());
	}

	@ExceptionHandler({ BindException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(BindException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed("BindException");
	}

	@ExceptionHandler({ RuntimeException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(RuntimeException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed("run time exception");
	}

	// @ExceptionHandler({ JsonProcessingException.class })
	// @ResponseStatus(HttpStatus.OK)
	// public Result<Void> handleException(JsonProcessingException e) {
	// log.error("GlobalExceptionHandler", e);
	// return Result.failed("json parse error");
	// }

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result<Void> handleException(MethodArgumentTypeMismatchException e) {
		log.error("GlobalExceptionHandler", e);
		return Result.failed("method argument type mismatch");
	}

}

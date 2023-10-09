package mumage.mumagebackend.exception;

import lombok.extern.slf4j.Slf4j;
import mumage.mumagebackend.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class RestControllerAdvisor {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handlerMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        log.warn(methodArgumentNotValidException.getMessage());
        HashMap<String, String> response = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String responseMessage = fieldError.getDefaultMessage() + ", 입력된 값 : [" + fieldError.getRejectedValue() + "]";
            response.put(fieldError.getField(), responseMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponseDto(HttpStatus.BAD_REQUEST.value(), response));
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ExceptionResponseDto> handlerCustomeException(CustomException customException) {
        log.warn(customException.getMessage());
        return ResponseEntity.status(customException.getHttpStatus()).body(new ExceptionResponseDto(customException.getErrCode()));
    }

}

package ru.job4j.auth.handler;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler (value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        Map<String, String> errorDetails = Map.of(
                "message", "Some fields are empty",
                "details", e.getMessage()
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        log.error(e.getMessage());
    }

}
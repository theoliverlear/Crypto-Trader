package org.theoliverlear.config;
//=================================-Imports-==================================
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    //=============================-Methods-==================================

    //------------------------Handle-Any-Exception----------------------------
    @ExceptionHandler(value = {Exception.class})
    public void handleAnyException(Exception ex) {
        log.error("An exception occurred: ", ex);
    }
}

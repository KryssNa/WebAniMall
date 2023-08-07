package com.example.AniMall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ModelAndView handleAppException(AppException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(ex.getStatus());

        // Set the appropriate view name for the error page based on the status code
        if (ex.getStatus() == HttpStatus.NOT_FOUND) {
            modelAndView.setViewName("error/404");
        } else if (ex.getStatus() == HttpStatus.BAD_REQUEST) {
            modelAndView.setViewName("error/400");
        } else if (ex.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            modelAndView.setViewName("error/500");
        } else if(ex.getStatus() == HttpStatus.FORBIDDEN) {
            modelAndView.setViewName("error/403");
        } else if(ex.getStatus() == HttpStatus.UNAUTHORIZED) {
            modelAndView.setViewName("error/401");
        }
        else if(ex.getStatus() == HttpStatus.TOO_MANY_REQUESTS) {
            modelAndView.setViewName("error/429");
        }
        else {
            // For other status codes, set a generic error page
            modelAndView.setViewName("error/error");
        }


        return modelAndView;
    }
}

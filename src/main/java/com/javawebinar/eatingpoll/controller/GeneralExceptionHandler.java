package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.exceptions.BadRequestException;
import com.javawebinar.eatingpoll.exceptions.EntityNotFoundException;
import com.javawebinar.eatingpoll.exceptions.TimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GeneralExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler({BadRequestException.class, EntityNotFoundException.class, TimeException.class})
    public ModelAndView handleExceptions(RuntimeException exception) {
        logger.error("An exception occurred: " + exception.toString());
        ModelAndView mav = new ModelAndView("exception");
        mav.addObject("message", exception.getMessage());
        return mav;
    }
}

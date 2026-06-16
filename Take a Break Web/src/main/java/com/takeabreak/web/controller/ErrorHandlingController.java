package com.takeabreak.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class ErrorHandlingController implements ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");

        model.addAttribute("statusCode", statusCode != null ? statusCode : "Erro Desconhecido");
        model.addAttribute("message", message != null ? message : "Ocorreu um erro inesperado.");
        if (throwable != null) {
            model.addAttribute("exception", throwable.getMessage());
        }

        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}

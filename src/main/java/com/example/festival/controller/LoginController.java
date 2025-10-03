package com.example.festival.controller;

import com.example.festival.entity.Usuario;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

            System.out.println("logueando Usuario:" + auth.getPrincipal());

            return "redirect:/heroes";

        } else {
            System.out.println("logueando Usuario Anonymous ");

            return "login";
        }
    }
}

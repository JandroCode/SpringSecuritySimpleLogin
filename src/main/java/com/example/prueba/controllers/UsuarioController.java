package com.example.prueba.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {


    @GetMapping("/public")
    public String home(){
        return "home";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/admin/home")
    public String adminHome() {
        return "adminHome";  // Devuelve la vista del home de ADMIN
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "userHome";  // Devuelve la vista del home de USER
    }
}

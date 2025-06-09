package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        Usuario usuario = usuarioService.usuarioLogado();
        model.addAttribute("usuario", usuario);
        model.addAttribute("conteudo", "home");
        return "layout";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
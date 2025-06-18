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

    @GetMapping("/admin/home")
    public String homeFuncionario(Model model, Principal principal) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/home");
        return "/admin/layout";
    }

    @GetMapping("/admin/login")
    public String loginFuncionario() {
        return "/admin/login";
    }

    @GetMapping("/login")
    public String loginCliente() {
        return "/login";
    }

    @GetMapping("/")
    public String homeCliente() {
        return "/home";
    }
}
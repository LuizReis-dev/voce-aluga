package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuario", usuarioLogado);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("conteudo", "/usuarios/listagem");
        return "layout";
    }
}

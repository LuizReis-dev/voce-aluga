package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO;
import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.GrupoService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GrupoController {
    private final GrupoService grupoService;
    private final UsuarioService usuarioService;
    public GrupoController(GrupoService grupoService, UsuarioService usuarioService) {
        this.grupoService = grupoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin/grupos")
    public String grupos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Grupo> grupos = grupoService.findAll();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("grupos", grupos);
        model.addAttribute("conteudo", "/admin/grupos/listagem");
        return "/admin/layout";
    }
}

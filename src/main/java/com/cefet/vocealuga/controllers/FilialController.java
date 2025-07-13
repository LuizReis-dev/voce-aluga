package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.FilialService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FilialController {
    private final UsuarioService usuarioService;
    private final FilialService filialService;

    public FilialController(UsuarioService usuarioService, FilialService filialService) {
        this.usuarioService = usuarioService;
        this.filialService = filialService;
    }

    @GetMapping("/admin/filiais")
    public String filiais(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Filial> filiais = filialService.findAll();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("filiais", filiais);
        model.addAttribute("conteudo", "/admin/filiais/listagem");
        return "admin/layout";
    }
}

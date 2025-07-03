package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.FormaPagamento;
import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.GrupoService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class ReservaController {

    private final UsuarioService usuarioService;
    private final GrupoService grupoService;

    public ReservaController(UsuarioService usuarioService, GrupoService grupoService) {
        this.usuarioService = usuarioService;
        this.grupoService = grupoService;
    }

    @GetMapping("/admin/reservas/cadastro")
    public String cadastrarReserva(Model model) {
        List<Grupo> grupos = grupoService.findAll();
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("grupos", grupos);
        model.addAttribute("formasPagamento", FormaPagamento.values());
        model.addAttribute("conteudo", "/admin/reservas/cadastro");
        return "/admin/layout";
    }
}

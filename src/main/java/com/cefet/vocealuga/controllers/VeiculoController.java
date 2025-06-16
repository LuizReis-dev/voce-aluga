package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.models.Veiculo;
import com.cefet.vocealuga.repositories.VeiculoRepository;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioService usuarioService;

    public VeiculoController(VeiculoRepository veiculoRepository, UsuarioService usuarioService) {
        this.veiculoRepository = veiculoRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/veiculos")
    public String listarVeiculos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoRepository.findAll();
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/veiculos/listagem");

        return "layout";
    }
}

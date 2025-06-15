package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.ModeloVeiculoService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ModeloVeiculoController {
    private final UsuarioService usuarioService;
    private final ModeloVeiculoService modeloVeiculoService;

    public ModeloVeiculoController(UsuarioService usuarioService, ModeloVeiculoService modeloVeiculoService) {
        this.usuarioService = usuarioService;
        this.modeloVeiculoService = modeloVeiculoService;
    }

    @GetMapping("/modelos-veiculos")
    public String modeloVeiculos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = modeloVeiculoService.findAll();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("modelos", modelos);
        model.addAttribute("conteudo", "/modelos-veiculos/listagem");
        return "layout";
    }
}

package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.models.Veiculo;
import com.cefet.vocealuga.repositories.VeiculoRepository;
import com.cefet.vocealuga.services.ModeloVeiculoService;
import com.cefet.vocealuga.services.UsuarioService;
import com.cefet.vocealuga.services.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioService usuarioService;
    private final ModeloVeiculoService modeloVeiculoService;
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoRepository veiculoRepository, UsuarioService usuarioService, ModeloVeiculoService modeloVeiculoService, VeiculoService veiculoService) {
        this.veiculoRepository = veiculoRepository;
        this.usuarioService = usuarioService;
        this.modeloVeiculoService = modeloVeiculoService;
        this.veiculoService = veiculoService;
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

    @GetMapping("/veiculos/compra")
    public String compra(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = this.modeloVeiculoService.findAll();
        model.addAttribute("modelos", modelos);
        model.addAttribute("compraVeiculoDTO", new CompraVeiculoDTO());
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/veiculos/compra");

        return "layout";
    }

    @PostMapping("/veiculos/compra")
    public String registrarCompra(@ModelAttribute CompraVeiculoDTO compraVeiculoDTO, RedirectAttributes redirectAttributes) {
        try {
            veiculoService.compra(compraVeiculoDTO);
            redirectAttributes.addFlashAttribute("success", "Veículo salvo com sucesso!");
            return "redirect:/veiculos";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o veículo: " + e.getMessage());
            return "redirect:/veiculos";
        }
    }
}

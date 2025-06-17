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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VeiculoController {

    private final UsuarioService usuarioService;
    private final ModeloVeiculoService modeloVeiculoService;
    private final VeiculoService veiculoService;

    public VeiculoController(UsuarioService usuarioService, ModeloVeiculoService modeloVeiculoService, VeiculoService veiculoService) {
        this.usuarioService = usuarioService;
        this.modeloVeiculoService = modeloVeiculoService;
        this.veiculoService = veiculoService;
    }

    @GetMapping("/veiculos")
    public String listarVeiculos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoService.findAll();
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

    @GetMapping("/veiculos/{id}/modelo")
    public String listarPorModelo(Model model, @PathVariable Integer id) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoService.findAllByModelo(id);
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/veiculos/listagem");

        return "layout";
    }

    @PostMapping("/veiculos/compra")
    public String registrarCompra(@ModelAttribute CompraVeiculoDTO compraVeiculoDTO, RedirectAttributes redirectAttributes) {
        try {
            veiculoService.compra(compraVeiculoDTO);
            redirectAttributes.addFlashAttribute("success", "Veículo salvo com sucesso!");
            return "redirect:/veiculos/compra";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o veículo: " + e.getMessage());
            return "redirect:/veiculos/compra";
        }
    }
}

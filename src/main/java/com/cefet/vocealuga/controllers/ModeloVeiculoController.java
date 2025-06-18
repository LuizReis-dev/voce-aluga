package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO;
import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.ModeloVeiculoService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<ModeloVeiculoDTO> modelos = modeloVeiculoService.findAllQuantidade();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("modelos", modelos);
        model.addAttribute("conteudo", "/modelos-veiculos/listagem");
        return "layout";
    }

    @GetMapping("/modelos-veiculos/cadastro")
    public String novoUsuario(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("modelo", new ModeloVeiculo());
        model.addAttribute("conteudo", "/modelos-veiculos/cadastro");
        return "layout";
    }

    @PostMapping("/modelos-veiculos")
    public String salvarModelo(@ModelAttribute ModeloVeiculo modelo, @RequestParam("imagemFile") MultipartFile imagem, RedirectAttributes redirectAttributes) {
        try {
            modeloVeiculoService.salvar(modelo, imagem);
            redirectAttributes.addFlashAttribute("success", "Modelo Veículo salvo com sucesso!");
            return "redirect:/modelos-veiculos";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o modelo: " + e.getMessage());
            if(modelo.getId() != null) {
                return "redirect:/modelos-veiculos/" +modelo.getId()+"/editar";
            }

            return "redirect:/modelos-veiculos/cadastro";
        }
    }

    @GetMapping("/modelos-veiculos/{id}/editar")
    public String editarVeiculo(@PathVariable Integer id, Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);

        ModeloVeiculo modelo = modeloVeiculoService.findById(id);
        model.addAttribute("modelo", modelo);
        model.addAttribute("conteudo", "/modelos-veiculos/cadastro");
        return "layout";
    }

    @PostMapping("/modelos-veiculos/{id}/excluir")
    public String deletarModelo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            modeloVeiculoService.deletarModelo(id);
            redirectAttributes.addFlashAttribute("success", "Modelo deletado com sucesso!");
            return "redirect:/modelos-veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar o modelo de veículo: " + e.getMessage());
            return "redirect:/modelos-veiculos";
        }
    }
}

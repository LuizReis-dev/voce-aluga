package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.dtos.veiculos.VendaVeiculoDTO;
import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.models.Veiculo;
import com.cefet.vocealuga.services.ModeloVeiculoService;
import com.cefet.vocealuga.services.UsuarioService;
import com.cefet.vocealuga.services.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/admin/veiculos")
    public String listarVeiculos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoService.findAll();
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/listagem");

        return "/admin/layout";
    }

    @GetMapping("/admin/veiculos/compra")
    public String compra(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = this.modeloVeiculoService.findAll();
        model.addAttribute("modelos", modelos);
        model.addAttribute("compraVeiculoDTO", new CompraVeiculoDTO());
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/compra");

        return "/admin/layout";
    }

    @GetMapping("/admin/veiculos/{id}/modelo")
    public String listarPorModelo(Model model, @PathVariable Integer id) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoService.findAllByModelo(id);
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/listagem");

        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/compra")
    public String registrarCompra(@ModelAttribute @Valid CompraVeiculoDTO compraVeiculoDTO, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o veículo: verifique os dados informados.");
            redirectAttributes.addFlashAttribute("compraVeiculoDTO", compraVeiculoDTO);

            List<String> mensagensErro = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            redirectAttributes.addFlashAttribute("erros", mensagensErro);

            return "redirect:/admin/veiculos/compra";
        }

        try {
            veiculoService.compra(compraVeiculoDTO);
            redirectAttributes.addFlashAttribute("success", "Veículo salvo com sucesso!");
            return "redirect:/admin/veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o veículo: " + e.getMessage());
            return "redirect:/admin/veiculos/compra";
        }
    }

    @GetMapping("/admin/veiculos/venda")
    public String venda(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = this.modeloVeiculoService.findAll();
        model.addAttribute("modelos", modelos);
        model.addAttribute("vendaVeiculoDTO", new VendaVeiculoDTO());
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/venda");

        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/venda")
    public String registrarVenda(@ModelAttribute @Valid VendaVeiculoDTO vendaVeiculoDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao vender o veículo: verifique os dados informados.");
            redirectAttributes.addFlashAttribute("vendaVeiculoDTO", vendaVeiculoDTO);

            List<String> mensagensErro = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            redirectAttributes.addFlashAttribute("erros", mensagensErro);

            return "redirect:/admin/veiculos/venda";
        }

        try {
            veiculoService.venda(vendaVeiculoDTO);
            redirectAttributes.addFlashAttribute("success", "Veículo vendido com sucesso!");
            return "redirect:/admin/veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao vender o veículo: " + e.getMessage());
            return "redirect:/admin/veiculos/venda";
        }
    }
}

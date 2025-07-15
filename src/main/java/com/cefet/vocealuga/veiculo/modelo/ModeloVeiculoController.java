package com.cefet.vocealuga.veiculo.modelo;

import com.cefet.vocealuga.veiculo.dtos.ModeloVeiculoDTO;
import com.cefet.vocealuga.veiculo.grupo.Grupo;
import com.cefet.vocealuga.usuario.Usuario;
import com.cefet.vocealuga.veiculo.grupo.GrupoService;
import com.cefet.vocealuga.usuario.UsuarioService;
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
    private final GrupoService grupoService;

    public ModeloVeiculoController(UsuarioService usuarioService, ModeloVeiculoService modeloVeiculoService, GrupoService grupoService) {
        this.usuarioService = usuarioService;
        this.modeloVeiculoService = modeloVeiculoService;
        this.grupoService = grupoService;
    }

    @GetMapping("/admin/modelos-veiculos")
    public String modeloVeiculos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculoDTO> modelos = modeloVeiculoService.findAllQuantidade();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("modelos", modelos);
        model.addAttribute("conteudo", "/admin/modelos-veiculos/listagem");
        return "/admin/layout";
    }

    @GetMapping("/admin/modelos-veiculos/cadastro")
    public String novoModelo(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Grupo> grupos = grupoService.findAll();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("modelo", new ModeloVeiculo());
        model.addAttribute("grupos", grupos);
        model.addAttribute("conteudo", "/admin/modelos-veiculos/cadastro");
        return "/admin/layout";
    }

    @PostMapping("/admin/modelos-veiculos")
    public String salvarModelo(@ModelAttribute ModeloVeiculo modelo, @RequestParam("imagemFile") MultipartFile imagem, RedirectAttributes redirectAttributes) {
        try {
            modeloVeiculoService.salvar(modelo, imagem);
            redirectAttributes.addFlashAttribute("success", "Modelo Veículo salvo com sucesso!");
            return "redirect:/admin/modelos-veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o modelo: " + e.getMessage());
            if(modelo.getId() != null) {
                return "redirect:/admin/modelos-veiculos/" +modelo.getId()+"/editar";
            }

            return "redirect:/admin/modelos-veiculos/cadastro";
        }
    }

    @GetMapping("/admin/modelos-veiculos/{id}/editar")
    public String editarVeiculo(@PathVariable Integer id, Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);
        List<Grupo> grupos = grupoService.findAll();

        ModeloVeiculo modelo = modeloVeiculoService.findById(id);
        model.addAttribute("modelo", modelo);
        model.addAttribute("grupos", grupos);

        model.addAttribute("conteudo", "/admin/modelos-veiculos/cadastro");
        return "/admin/layout";
    }

    @PostMapping("/admin/modelos-veiculos/{id}/excluir")
    public String deletarModelo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            modeloVeiculoService.deletarModelo(id);
            redirectAttributes.addFlashAttribute("success", "Modelo deletado com sucesso!");
            return "redirect:/admin/modelos-veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar o modelo de veículo: " + e.getMessage());
            return "redirect:/admin/modelos-veiculos";
        }
    }
}

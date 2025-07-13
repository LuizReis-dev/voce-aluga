package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.FilialService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/admin/filiais/cadastro")
    public String cadastro(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        Filial filial = new Filial();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("filial", filial);
        model.addAttribute("conteudo", "/admin/filiais/cadastro");
        return "admin/layout";
    }

    @PostMapping("/admin/filiais")
    public String salvarFilial(Filial filial, RedirectAttributes redirectAttributes) {
        try {
            filialService.salvar(filial);
            redirectAttributes.addFlashAttribute("success", "Filial salva com sucesso!");
            return "redirect:/admin/filiais";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar filial: " + e.getMessage());
            if (filial.getId() != null) {
                return "redirect:/admin/filiais/" + filial.getId() + "/editar";
            }

            return "redirect:/admin/filiais/cadastro";
        }
    }

    @GetMapping("/admin/filiais/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        Filial filial = filialService.findById(id);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("filial", filial);
        model.addAttribute("conteudo", "/admin/filiais/cadastro");
        return "admin/layout";
    }

}

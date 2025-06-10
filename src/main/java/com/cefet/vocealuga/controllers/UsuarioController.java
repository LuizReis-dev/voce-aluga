package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Usuario;
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
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("conteudo", "/usuarios/listagem");
        return "layout";
    }

    @GetMapping("/usuarios/cadastro")
    public String novoUsuario(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("conteudo", "/usuarios/cadastro");
        return "layout";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        model.addAttribute("usuarioLogado", usuarioLogado);
        Usuario usuario = usuarioService.findById(id);
        System.out.println(usuario.getDataNascimento());
        model.addAttribute("usuario", usuario);
        model.addAttribute("conteudo", "/usuarios/cadastro");
        return "layout";
    }

    @PostMapping("/usuarios")
    public String salvarUsuario(Usuario usuario, RedirectAttributes redirectAttributes, @RequestParam(required = false) String senha) {
        try {
            usuario.setSenha(senha);
            usuarioService.salvar(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuário salvo com sucesso!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o usuário: " + e.getMessage());
            if(usuario.getId() != null) {
                return "redirect:/usuarios/" +usuario.getId()+"/editar";
            }

            return "redirect:/usuarios/cadastro";
        }
    }

}

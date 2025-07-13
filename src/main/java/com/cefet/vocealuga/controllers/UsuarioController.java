package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.CargoOperador;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.FilialService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UsuarioController {
	private final UsuarioService usuarioService;
	private final FilialService filialService;
	public UsuarioController(UsuarioService usuarioService, FilialService filialService) {
		this.usuarioService = usuarioService;
        this.filialService = filialService;
    }

	@GetMapping("/admin/usuarios")
	public String listarUsuarios(Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		List<Usuario> usuarios = usuarioService.findAll();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("conteudo", "/admin/usuarios/listagem");
		return "/admin/layout";
	}

	@GetMapping("/admin/usuarios/cadastro")
	public String novoUsuario(Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		model.addAttribute("cargos", CargoOperador.values());
		model.addAttribute("filiais", filialService.findAll());
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("conteudo", "/admin/usuarios/cadastro");
		return "/admin/layout";
	}

	@GetMapping("/admin/usuarios/{id}/editar")
	public String editarUsuario(@PathVariable Integer id, Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		model.addAttribute("usuarioLogado", usuarioLogado);
		Usuario usuario = usuarioService.findById(id);
		System.out.println(usuario.getDataNascimento());
		model.addAttribute("usuario", usuario);
		model.addAttribute("filiais", filialService.findAll());
		model.addAttribute("cargos", CargoOperador.values());
		model.addAttribute("conteudo", "/admin/usuarios/cadastro");
		return "/admin/layout";
	}

	@PostMapping("/admin/usuarios")
	public String salvarUsuario(Usuario usuario, RedirectAttributes redirectAttributes,
			@RequestParam(required = false) String senha) {
		try {
			usuario.setSenha(senha);
			usuarioService.salvar(usuario);
			redirectAttributes.addFlashAttribute("success", "Usuário salvo com sucesso!");
			return "redirect:/admin/usuarios";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erro ao salvar o usuário: " + e.getMessage());
			if (usuario.getId() != null) {
				return "redirect:/admin/usuarios/" + usuario.getId() + "/editar";
			}

			return "redirect:/admin/usuarios/cadastro";
		}
	}

	@PostMapping("/admin/usuarios/{id}/excluir")
	public String deletarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.deletarUsuario(id);
			redirectAttributes.addFlashAttribute("success", "Usuário deletado com sucesso!");
			return "redirect:/admin/usuarios";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erro ao deletar o usuário: " + e.getMessage());
			return "redirect:/admin/usuarios";
		}
	}

	@GetMapping("/cadastro")
	public String cadastroCliente(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "/cliente/cadastro";
	}

	@PostMapping("/cadastro")
	public String cadastroCliente(Usuario usuario, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.salvarCliente(usuario);

			redirectAttributes.addFlashAttribute("success", "cadastro realizado com sucesso!");
			return "redirect:/login";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erro ao criar conta: " + e.getMessage());
			return "redirect:/cadastro";
		}
	}

}

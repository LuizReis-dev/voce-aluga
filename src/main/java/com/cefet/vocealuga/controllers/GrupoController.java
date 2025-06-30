package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.GrupoService;
import com.cefet.vocealuga.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GrupoController {
	private final GrupoService grupoService;
	private final UsuarioService usuarioService;

	public GrupoController(GrupoService grupoService, UsuarioService usuarioService) {
		this.grupoService = grupoService;
		this.usuarioService = usuarioService;
	}

	@GetMapping("/admin/grupos")
	public String grupos(Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		List<Grupo> grupos = grupoService.findAll();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("grupos", grupos);
		model.addAttribute("conteudo", "/admin/grupos/listagem");
		return "/admin/layout";
	}

	@GetMapping("/admin/grupos/{id}/editar")
	public String editarGrupo(@PathVariable Integer id, Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		Grupo grupo = grupoService.findById(id);

		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("grupo", grupo);
		model.addAttribute("conteudo", "/admin/grupos/cadastro");
		return "/admin/layout";
	}

	@PostMapping("/admin/grupos")
	public String salvarGrupo(@ModelAttribute Grupo grupo, RedirectAttributes redirectAttributes) {
		try {
			grupoService.salvar(grupo);
			redirectAttributes.addFlashAttribute("success", "Preço alterado com sucesso!");
			return "redirect:/admin/grupos";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erro ao salvar o modelo: " + e.getMessage());
			return "redirect:/admin/grupos/" + grupo.getId() + "/editar";

		}
	}

}

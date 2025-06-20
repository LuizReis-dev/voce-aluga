package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.UsuarioService;
import com.cefet.vocealuga.services.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

	private final UsuarioService usuarioService;
	private final VeiculoService veiculoService;

	public HomeController(UsuarioService usuarioService, VeiculoService veiculoService) {
		this.usuarioService = usuarioService;
		this.veiculoService = veiculoService;
	}

	@GetMapping("/admin/home")
	public String homeFuncionario(Model model, Principal principal) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		boolean existeSolicitacaoTransferencia = veiculoService.existeSolicitacaoTransferencia();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("notificacaoTransferencia", existeSolicitacaoTransferencia);
		model.addAttribute("conteudo", "/admin/home");
		return "/admin/layout";
	}

	@GetMapping("/admin/login")
	public String loginFuncionario() {
		return "/admin/login";
	}

	@GetMapping("/login")
	public String loginCliente() {
		return "/cliente/login";
	}

	@GetMapping("/")
	public String homeCliente() {
		return "/cliente/home";
	}
}
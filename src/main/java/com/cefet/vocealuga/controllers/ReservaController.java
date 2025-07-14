package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.dtos.reservas.ReservaDTO;
import com.cefet.vocealuga.models.FormaPagamento;
import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.models.Reserva;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.services.GrupoService;
import com.cefet.vocealuga.services.ReservaService;
import com.cefet.vocealuga.services.UsuarioService;
import com.cefet.vocealuga.tasks.ReservaTask;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReservaController {
	private final UsuarioService usuarioService;
	private final ReservaService reservaService;
	private final GrupoService grupoService;
	private final ReservaTask reservaTask;

	public ReservaController(UsuarioService usuarioService, ReservaService reservaService, GrupoService grupoService,
			ReservaTask reservaTask) {
		this.usuarioService = usuarioService;
		this.reservaService = reservaService;
		this.grupoService = grupoService;
		this.reservaTask = reservaTask;
	}

	@GetMapping("/admin/reservas/cadastro")
	public String cadastrarReserva(Model model) {
		List<Grupo> grupos = grupoService.findAll();
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("grupos", grupos);
		model.addAttribute("formasPagamento", FormaPagamento.values());
		model.addAttribute("conteudo", "/admin/reservas/cadastro");
		return "/admin/layout";
	}

	@GetMapping("/admin/reservas/virada-de-dia")
	public String viradaDeDia(Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("conteudo", "/admin/reservas/virada-de-dia");
		return "/admin/layout";
	}

	@PostMapping("/admin/simular-virada-dia")
	public String simularVirada(
			@RequestParam("dataVirada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVirada) {
		System.out.println("Data de virada recebida: " + dataVirada);

		reservaTask.atualizaVeiculosReservados(dataVirada);
		return "redirect:/admin/reservas/virada-de-dia";
	}

	@GetMapping("/admin/reservas")
	public String reservas(Model model) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		List<ReservaDTO> reservas = reservaService.findAll();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("reservas", reservas);

		model.addAttribute("conteudo", "/admin/reservas/listagem");

		return "/admin/layout";
	}

	@GetMapping("/admin/reservas/{id}/detalhes")
	public String detalhes(@PathVariable Integer id, Model model) {
		Reserva reserva = reservaService.findById(id);
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("reserva", reserva);

		model.addAttribute("conteudo", "/admin/reservas/detalhes");

		return "/admin/layout";
	}

	@PostMapping("/admin/reservas/{id}/alterar-status")
	public String alterarStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			String mensagem = reservaService.alterarStatusReserva(id);

			redirectAttributes.addFlashAttribute("success", mensagem);
			return "redirect:/admin/reservas/" + id + "/detalhes";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erro ao alterar status: " + e.getMessage());

			return "redirect:/admin/reservas/" + id + "/detalhes";
		}
	}

	@GetMapping("/admin/reservas/{id}/cliente")
	public String listarPorCliente(@PathVariable Integer id, Model model) {
		List<ReservaDTO> reservas = reservaService.findByClienteId(id);
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("reservas", reservas);

		model.addAttribute("conteudo", "/admin/reservas/listagem");

		return "/admin/layout";
	}

	@GetMapping("/reservas/{id}")
	public String ListaReservaCliente(@PathVariable Integer id, Model model) {
		List<ReservaDTO> reservas = reservaService.findByClienteId(id);
		model.addAttribute("reservas", reservas);

		return "/cliente/listaReserva";
	}
}

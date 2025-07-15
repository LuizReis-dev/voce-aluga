package com.cefet.vocealuga;

import com.cefet.vocealuga.veiculo.dtos.ModeloVeiculoDTO;
import com.cefet.vocealuga.usuario.Usuario;
import com.cefet.vocealuga.veiculo.modelo.ModeloVeiculoService;
import com.cefet.vocealuga.reserva.ReservaService;
import com.cefet.vocealuga.usuario.UsuarioService;
import com.cefet.vocealuga.veiculo.VeiculoService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

	private final UsuarioService usuarioService;
	private final VeiculoService veiculoService;
	private final ModeloVeiculoService modeloVeiculoService;
	private final ReservaService reservaService;

	public HomeController(UsuarioService usuarioService, VeiculoService veiculoService,
			ModeloVeiculoService modeloVeiculoService, ReservaService reservaService) {
		this.usuarioService = usuarioService;
		this.modeloVeiculoService = modeloVeiculoService;
		this.veiculoService = veiculoService;
		this.reservaService = reservaService;
	}

	@GetMapping("/admin/home")
	public String homeFuncionario(Model model, Principal principal) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		boolean existeSolicitacaoTransferencia = veiculoService.existeSolicitacaoTransferencia();
		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("notificacaoTransferencia", existeSolicitacaoTransferencia);
		var reservasPorDia = reservaService.quantidadeReservaDiaDaSemana();
		var reservasPorOrigem = reservaService.contarReservaPorOrigem();
		var veiculosDisponiveis = veiculoService.quantidadeVeiculosDisponiveis();

		model.addAttribute("reservasPorDia", reservasPorDia);
		model.addAttribute("reservasPorOrigem", reservasPorOrigem);
		model.addAttribute("veiculosDisponiveis", veiculosDisponiveis);
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
	public String homeCliente(
			Model model,
			@RequestParam(required = false) String marca,
			@RequestParam(required = false) String modelo,
			@RequestParam(required = false) String ano,
			@RequestParam(required = false) String grupo,
			@RequestParam(defaultValue = "0") int page) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();

		org.springframework.data.domain.Pageable pageable = PageRequest.of(page, 16);
		Page<ModeloVeiculoDTO> pageResult = modeloVeiculoService.findAllPublico(pageable);

		List<ModeloVeiculoDTO> todos = modeloVeiculoService.findAllPublico(pageable).stream()
				.filter(m -> m.getQuantidade() > 0)
				.collect(Collectors.toList());

		List<ModeloVeiculoDTO> filtrados = todos.stream()
				.filter(m -> marca == null || marca.isBlank() || m.getMarca().equalsIgnoreCase(marca))
				.filter(m -> modelo == null || modelo.isBlank() || m.getModelo().equalsIgnoreCase(modelo))
				.filter(m -> ano == null || ano.isBlank() || String.valueOf(m.getAno()).equals(ano))
				.filter(m -> grupo == null || grupo.isBlank() || m.getGrupo().equalsIgnoreCase(grupo))
				.collect(Collectors.toList());

		Set<String> marcas = todos.stream().map(ModeloVeiculoDTO::getMarca).collect(Collectors.toSet());
		Set<String> modelos = todos.stream().map(ModeloVeiculoDTO::getModelo).collect(Collectors.toSet());
		Set<String> anos = todos.stream().map(m -> String.valueOf(m.getAno())).collect(Collectors.toSet());
		Set<String> grupos = todos.stream().map(ModeloVeiculoDTO::getGrupo).collect(Collectors.toSet());

		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("clienteId", usuarioLogado.getCliente().getId());
		model.addAttribute("modelos", filtrados);
		model.addAttribute("marcas", marcas);
		model.addAttribute("modelosDisponiveis", modelos);
		model.addAttribute("anos", anos);
		model.addAttribute("grupos", grupos);
		model.addAttribute("paginaAtual", page);
		model.addAttribute("totalPaginas", pageResult.getTotalPages());

		return "cliente/home";
	}
}
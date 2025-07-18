package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.reservas.ReservaClienteDTO;
import com.cefet.vocealuga.dtos.reservas.ReservaDTO;
import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.*;
import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import com.cefet.vocealuga.webservices.requests.CalculoValorReservaRequest;
import com.cefet.vocealuga.webservices.requests.CreateReservaRequest;
import com.cefet.vocealuga.webservices.responses.CalculoValorReservaResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {
	private final UsuarioService usuarioService;
	private final GrupoRepository grupoRepository;
	private final ClienteRepository clienteRepository;
	private final VeiculoRepository veiculoRepository;
	private final ReservaRepository reservaRepository;
	private final PagamentoRepository pagamentoRepository;

	public ReservaService(UsuarioService usuarioService, GrupoRepository grupoRepository,
			ClienteRepository clienteRepository, VeiculoRepository veiculoRepository, ReservaRepository reservaRepository,
			PagamentoRepository pagamentoRepository) {
		this.usuarioService = usuarioService;
		this.grupoRepository = grupoRepository;
		this.clienteRepository = clienteRepository;
		this.veiculoRepository = veiculoRepository;
		this.reservaRepository = reservaRepository;
		this.pagamentoRepository = pagamentoRepository;
	}

	@Transactional
	public void criarReserva(CreateReservaRequest request) {
		Operador operador = usuarioService.usuarioLogado().getOperador();
		LocalDate hoje = LocalDate.now();
		Cliente cliente = clienteRepository.findById(request.getClienteId())
				.orElseThrow(() -> new WebserviceException("Cliente não encontrado!", false));

		if (cliente.getUsuario().getDataNascimento() != null) {
			Period idade = Period.between(cliente.getUsuario().getDataNascimento(), hoje);

			if (idade.getYears() < 25) {
				throw new WebserviceException("Cliente deve ter pelo menos 25 anos para realizar a reserva!", false);
			}
		}
		LocalDate dataDevolucao = request.getDataDevolucao();
		LocalDate dataEntrega = request.getDataEntrega();

		if (reservaRepository.clientePossuiReservaNoPeriodo(cliente.getId(), dataDevolucao, dataEntrega)) {
			throw new WebserviceException("Cliente já possui uma reserva para o periodo selecionado!", false);
		}

		Veiculo veiculo = this.veiculoRepository
				.findFirstDisponiveisSemReserva(operador.getFilial().getId(), request.getModeloId(), dataEntrega, dataDevolucao)
				.stream()
				.findFirst()
				.orElseThrow(() -> new WebserviceException("Nenhum veículo encontrado, selecione outro modelo!", false));

		Grupo grupo = veiculo.getModelo().getGrupo();

		if (dataDevolucao.isBefore(dataEntrega)) {
			throw new WebserviceException("Data de devolução não pode ser antes da data de entrega.", false);
		}

		long dias = ChronoUnit.DAYS.between(dataEntrega, dataDevolucao) + 1;

		BigDecimal valorDiario = grupo.getPrecoPorDia();

		BigDecimal valorReserva = valorDiario.multiply(BigDecimal.valueOf(dias));

		Reserva reserva = new Reserva();
		reserva.setCliente(cliente);
		reserva.setOperador(operador);
		reserva.setVeiculo(veiculo);
		reserva.setDataDevolucao(dataDevolucao);
		reserva.setDataEntrega(dataEntrega);
		reserva.setValor(valorReserva);

		reserva.setOrigem("PRESENCIAL");
		if (dataEntrega.equals(hoje)) {
			veiculo.setEstadoVeiculo(EstadoVeiculo.RESERVADO);
			veiculoRepository.save(veiculo);
		}

		reservaRepository.save(reserva);

		Pagamento pagamento = new Pagamento();
		pagamento.setReserva(reserva);

		pagamento.setDataPagamento(LocalDate.now());
		pagamento.setValor(valorReserva);
		pagamento.setNotaFiscal(UUID.randomUUID().toString());
		pagamento.setFormaPagamento(request.getFormaPagamento());
		pagamentoRepository.save(pagamento);
	}

	public CalculoValorReservaResponse calcularValorReserva(CalculoValorReservaRequest calculoValorReservaRequest) {
		if (calculoValorReservaRequest.getDateEntrega() == null || calculoValorReservaRequest.getDataDevolucao() == null
				|| calculoValorReservaRequest.getGrupoId() == null) {
			throw new WebserviceException("Datas e grupo id não podem ser nulos.", false);
		}
		LocalDate dataDevolucao = calculoValorReservaRequest.getDataDevolucao();
		LocalDate dataEntrega = calculoValorReservaRequest.getDateEntrega();
		Grupo grupo = grupoRepository.findById(calculoValorReservaRequest.getGrupoId())
				.orElseThrow(() -> new WebserviceException("Grupo não encontrado.", false));
		BigDecimal valorDiario = grupo.getPrecoPorDia();

		if (dataDevolucao.isBefore(dataEntrega)) {
			throw new WebserviceException("Data de devolução não pode ser antes da data de entrega.", false);
		}

		// Quantidade de dias (incluindo o dia da entrega)
		long dias = ChronoUnit.DAYS.between(dataEntrega, dataDevolucao) + 1;

		return new CalculoValorReservaResponse(valorDiario.multiply(BigDecimal.valueOf(dias)));
	}

	@Transactional
	public void criarReservaCliente(ReservaClienteDTO dto) {
		LocalDate hoje = LocalDate.now();
		Cliente cliente = usuarioService.usuarioLogado().getCliente();

		if (cliente.getUsuario().getDataNascimento() != null) {
			Period idade = Period.between(cliente.getUsuario().getDataNascimento(), hoje);

			if (idade.getYears() < 25) {
				throw new WebserviceException("Cliente deve ter pelo menos 25 anos para realizar a reserva!", false);
			}
		}
		LocalDate dataDevolucao = dto.getDataDevolucao();
		LocalDate dataEntrega = dto.getDataEntrega();

		if (reservaRepository.clientePossuiReservaNoPeriodo(cliente.getId(), dataDevolucao, dataEntrega)) {
			throw new WebserviceException("Cliente já possui uma reserva para o periodo selecionado!", false);
		}

		Veiculo veiculo = this.veiculoRepository
				.findFirstDisponiveisSemReserva(dto.getFilialId(), dto.getModeloId(), dataEntrega, dataDevolucao)
				.stream()
				.findFirst()
				.orElseThrow(() -> new WebserviceException("Nenhum veículo encontrado, selecione outro modelo!", false));

		Grupo grupo = veiculo.getModelo().getGrupo();

		if (dataDevolucao.isBefore(dataEntrega)) {
			throw new WebserviceException("Data de devolução não pode ser antes da data de entrega.", false);
		}

		long dias = ChronoUnit.DAYS.between(dataEntrega, dataDevolucao) + 1;

		BigDecimal valorDiario = grupo.getPrecoPorDia();

		BigDecimal valorReserva = valorDiario.multiply(BigDecimal.valueOf(dias));

		Reserva reserva = new Reserva();
		reserva.setCliente(cliente);
		reserva.setVeiculo(veiculo);
		reserva.setDataDevolucao(dataDevolucao);
		reserva.setDataEntrega(dataEntrega);
		reserva.setValor(valorReserva);

		reserva.setOrigem("ONLINE");
		if (dataEntrega.equals(hoje)) {
			veiculo.setEstadoVeiculo(EstadoVeiculo.RESERVADO);
			veiculoRepository.save(veiculo);
		}

		reservaRepository.save(reserva);

		Pagamento pagamento = new Pagamento();
		pagamento.setReserva(reserva);
		pagamento.setDataPagamento(LocalDate.now());
		pagamento.setValor(valorReserva);
		pagamento.setNotaFiscal(UUID.randomUUID().toString());
		pagamento.setFormaPagamento(dto.getFormaPagamento());
		pagamentoRepository.save(pagamento);
	}


	public List<Object[]> quantidadeReservaDiaDaSemana() {
		LocalDate hoje = LocalDate.now();
		LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY);
		LocalDate fimSemana = hoje.with(DayOfWeek.SUNDAY);
		return reservaRepository.contarReservasPorDia(inicioSemana, fimSemana);
	}

	public List<Object[]> contarReservaPorOrigem() {
		return reservaRepository.contarReservasPorOrigem();
	}

	public List<ReservaDTO> findAll() {
		Usuario usuarioLogado = usuarioService.usuarioLogado();

		return reservaRepository.findAll(usuarioLogado.getOperador().getFilial());
	}

	public Reserva findById(Integer id) {
		return reservaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada!"));
	}

	@Transactional
	public String alterarStatusReserva(Integer id) {
		Reserva reserva =  reservaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada!"));
		String mensagem = "";

		if(StatusReserva.CRIADA.equals(reserva.getStatus())) {
			reserva.getVeiculo().setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);
			reserva.setStatus(StatusReserva.CANCELADA);
			reserva.getPagamento().setReembolsado(true);
			mensagem = "Reserva cancelada com sucesso.";
		}

		if(StatusReserva.ENTREGUE.equals(reserva.getStatus())) {
			reserva.getVeiculo().setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);
			reserva.setStatus(StatusReserva.FINALIZADA);
			mensagem = "Cliente retornou o veículo. Reserva finalizada com sucesso!";
		}

		if(StatusReserva.AGUARDANDO_ENTREGA.equals(reserva.getStatus())) {
			reserva.setStatus(StatusReserva.ENTREGUE);
			mensagem = "Veículo entregue ao cliente";
		}
		reservaRepository.save(reserva);
		return mensagem;
	}

	public List<ReservaDTO> findByClienteId(Integer clienteId) {
		return reservaRepository.findAllByClienteId(clienteId);
	}
}

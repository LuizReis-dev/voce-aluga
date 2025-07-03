package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.*;
import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import com.cefet.vocealuga.webservices.requests.CalculoValorReservaRequest;
import com.cefet.vocealuga.webservices.requests.CreateReservaRequest;
import com.cefet.vocealuga.webservices.responses.CalculoValorReservaResponse;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ReservaService {
    private final UsuarioService usuarioService;
    private final GrupoRepository grupoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ReservaRepository reservaRepository;
    private final PagamentoRepository pagamentoRepository;

    public ReservaService(UsuarioService usuarioService, GrupoRepository grupoRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository, ReservaRepository reservaRepository, PagamentoRepository pagamentoRepository) {
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

        if(reservaRepository.clientePossuiReservaNoPeriodo(cliente.getId(), dataDevolucao, dataEntrega)){
            throw new WebserviceException("Cliente já possui uma reserva para o periodo selecionado!", false);
        }

        Veiculo veiculo = this.veiculoRepository
                .findFirstDisponivelSemReserva(operador.getFilial().getId(), request.getModeloId(), dataEntrega, dataDevolucao)
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

        if(dataEntrega.equals(hoje)) {
            veiculo.setEstadoVeiculo(EstadoVeiculo.RESERVADO);
            veiculoRepository.save(veiculo);
        }

        reservaRepository.save(reserva);

        Pagamento pagamento = new Pagamento();
        pagamento.setDataPagamento(LocalDate.now());
        pagamento.setValor(valorReserva);
        pagamento.setNotaFiscal(UUID.randomUUID().toString());
        pagamento.setFormaPagamento(request.getFormaPagamento());
        pagamentoRepository.save(pagamento);
    }

    public CalculoValorReservaResponse calcularValorReserva(CalculoValorReservaRequest calculoValorReservaRequest) {
        if (calculoValorReservaRequest.getDateEntrega() == null || calculoValorReservaRequest.getDataDevolucao() == null || calculoValorReservaRequest.getGrupoId() == null) {
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
}

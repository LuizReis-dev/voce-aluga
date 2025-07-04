package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.*;
import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import com.cefet.vocealuga.webservices.requests.CreateReservaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
public class ReservaServiceTest {

    private ReservaService reservaService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservaService = spy(new ReservaService(
                usuarioService,
                grupoRepository,
                clienteRepository,
                veiculoRepository,
                reservaRepository,
                pagamentoRepository
        ));
    }
    private Usuario criarUsuarioComDataNascimento(LocalDate dataNascimento) {
        Usuario usuario = new Usuario();
        usuario.setDataNascimento(dataNascimento);
        return usuario;
    }

    private Cliente criarClienteComUsuario(Usuario usuario) {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setUsuario(usuario);
        return cliente;
    }

    private Operador criarOperadorComFilial(Integer filialId) {
        Operador operador = new Operador();
        Filial filial = new Filial();
        filial.setId(filialId);
        operador.setFilial(filial);
        return operador;
    }

    @Test
    void criarReservaDeveSalvarReservaEPagamentoQuandoDadosValidos() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataEntrega = hoje.plusDays(1);
        LocalDate dataDevolucao = hoje.plusDays(5);

        Usuario usuarioLogado = criarUsuarioComDataNascimento(LocalDate.now().minusYears(30));
        usuarioLogado.setOperador(criarOperadorComFilial(10));

        Cliente cliente = criarClienteComUsuario(criarUsuarioComDataNascimento(LocalDate.now().minusYears(30)));

        Grupo grupo = new Grupo();
        grupo.setPrecoPorDia(new BigDecimal("100"));

        ModeloVeiculo modelo = new ModeloVeiculo();
        modelo.setGrupo(grupo);

        Veiculo veiculo = new Veiculo();
        veiculo.setModelo(modelo);
        veiculo.setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(cliente.getId());
        request.setModeloId(5);
        request.setDataEntrega(dataEntrega);
        request.setDataDevolucao(dataDevolucao);
        request.setFormaPagamento(FormaPagamento.Dinheiro);

        when(usuarioService.usuarioLogado()).thenReturn(usuarioLogado);
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(reservaRepository.clientePossuiReservaNoPeriodo(cliente.getId(), dataDevolucao, dataEntrega)).thenReturn(false);
        when(veiculoRepository.findFirstDisponivelSemReserva(
                usuarioLogado.getOperador().getFilial().getId(),
                request.getModeloId(),
                dataEntrega,
                dataDevolucao))
                .thenReturn(Optional.of(veiculo));

        reservaService.criarReserva(request);

        verify(reservaRepository, times(1)).save(any(Reserva.class));
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
        // Como dataEntrega != hoje, estado do veículo não muda para RESERVADO
        assertEquals(EstadoVeiculo.DISPONIVEL, veiculo.getEstadoVeiculo());
    }

    @Test
    void criarReservaDeveRetornarExcecaoQuandoClienteNaoEncontrado() {
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setOperador(new Operador());
        when(usuarioService.usuarioLogado()).thenReturn(usuarioLogado);

        when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(999);

        WebserviceException ex = assertThrows(WebserviceException.class, () -> reservaService.criarReserva(request));
        assertEquals("Cliente não encontrado!", ex.getMessage());
    }

    @Test
    void criarReservaDeveRetornarExcecaoQuandoClienteMenorQue25Anos() {
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setOperador(new Operador());
        when(usuarioService.usuarioLogado()).thenReturn(usuarioLogado);

        Usuario usuario = criarUsuarioComDataNascimento(LocalDate.now().minusYears(24));
        Cliente cliente = criarClienteComUsuario(usuario);

        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(cliente.getId());

        WebserviceException ex = assertThrows(WebserviceException.class, () -> reservaService.criarReserva(request));
        assertEquals("Cliente deve ter pelo menos 25 anos para realizar a reserva!", ex.getMessage());
    }

    @Test
    void criarReservaDeveRetornarExcecaoQuandoClientePossuiReservaNoPeriodo() {
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setOperador(new Operador());
        when(usuarioService.usuarioLogado()).thenReturn(usuarioLogado);

        Usuario usuario = criarUsuarioComDataNascimento(LocalDate.now().minusYears(30));
        Cliente cliente = criarClienteComUsuario(usuario);

        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(reservaRepository.clientePossuiReservaNoPeriodo(any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(cliente.getId());
        request.setDataEntrega(LocalDate.now());
        request.setDataDevolucao(LocalDate.now().plusDays(1));

        WebserviceException ex = assertThrows(WebserviceException.class, () -> reservaService.criarReserva(request));
        assertEquals("Cliente já possui uma reserva para o periodo selecionado!", ex.getMessage());
    }

    @Test
    void criarReservaDeveRetornarExcecaoQuandoNenhumVeiculoDisponivel() {
        Usuario usuario = criarUsuarioComDataNascimento(LocalDate.now().minusYears(30));
        Cliente cliente = criarClienteComUsuario(usuario);
        Operador operador = criarOperadorComFilial(1);

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        usuario.setOperador(operador);

        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(reservaRepository.clientePossuiReservaNoPeriodo(any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);
        when(veiculoRepository.findFirstDisponivelSemReserva(any(Integer.class), any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(cliente.getId());
        request.setModeloId(1);
        request.setDataEntrega(LocalDate.now());
        request.setDataDevolucao(LocalDate.now().plusDays(1));

        WebserviceException ex = assertThrows(WebserviceException.class, () -> reservaService.criarReserva(request));
        assertEquals("Nenhum veículo encontrado, selecione outro modelo!", ex.getMessage());
    }

    @Test
    void criarReservaDeveRetornarExcecaoQuandoDataDevolucaoAntesDaEntrega() {
        Usuario usuario = criarUsuarioComDataNascimento(LocalDate.now().minusYears(30));
        Cliente cliente = criarClienteComUsuario(usuario);
        Operador operador = criarOperadorComFilial(1);

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        usuario.setOperador(operador);

        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(reservaRepository.clientePossuiReservaNoPeriodo(any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        Grupo grupo = new Grupo();
        grupo.setPrecoPorDia(BigDecimal.TEN);

        ModeloVeiculo modelo = new ModeloVeiculo();
        modelo.setGrupo(grupo);

        Veiculo veiculo = new Veiculo();
        veiculo.setModelo(modelo);

        when(veiculoRepository.findFirstDisponivelSemReserva(any(Integer.class), any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Optional.of(veiculo));

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(cliente.getId());
        request.setModeloId(1);
        request.setDataEntrega(LocalDate.now().plusDays(10));
        request.setDataDevolucao(LocalDate.now());

        WebserviceException ex = assertThrows(WebserviceException.class, () -> reservaService.criarReserva(request));
        assertEquals("Data de devolução não pode ser antes da data de entrega.", ex.getMessage());
    }


    @Test
    void criarReservaDeveSetarVeiculoComoReservadoQuandoDataEntregaEhHoje() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataDevolucao = hoje.plusDays(3);

        Usuario usuarioLogado = criarUsuarioComDataNascimento(LocalDate.now().minusYears(30));
        Operador operador = criarOperadorComFilial(1);
        usuarioLogado.setOperador(operador);

        Cliente cliente = criarClienteComUsuario(criarUsuarioComDataNascimento(LocalDate.now().minusYears(30)));

        Grupo grupo = new Grupo();
        grupo.setPrecoPorDia(new BigDecimal("150"));

        ModeloVeiculo modelo = new ModeloVeiculo();
        modelo.setGrupo(grupo);

        Veiculo veiculo = new Veiculo();
        veiculo.setModelo(modelo);
        veiculo.setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);

        CreateReservaRequest request = new CreateReservaRequest();
        request.setClienteId(cliente.getId());
        request.setModeloId(1);
        request.setDataEntrega(hoje);
        request.setDataDevolucao(dataDevolucao);
        request.setFormaPagamento(FormaPagamento.Credito);

        when(usuarioService.usuarioLogado()).thenReturn(usuarioLogado);
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(reservaRepository.clientePossuiReservaNoPeriodo(cliente.getId(), dataDevolucao, hoje)).thenReturn(false);
        when(veiculoRepository.findFirstDisponivelSemReserva(operador.getFilial().getId(), request.getModeloId(), hoje, dataDevolucao))
                .thenReturn(Optional.of(veiculo));

        reservaService.criarReserva(request);

        verify(veiculoRepository, times(1)).save(veiculo);
        assertEquals(EstadoVeiculo.RESERVADO, veiculo.getEstadoVeiculo());
    }
}

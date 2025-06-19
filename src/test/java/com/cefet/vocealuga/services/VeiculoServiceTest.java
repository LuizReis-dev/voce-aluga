package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.dtos.veiculos.VendaVeiculoDTO;
import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.GerenciamentoTransacaoVeiculoRepository;
import com.cefet.vocealuga.repositories.VeiculoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VeiculoServiceTest {

    @InjectMocks
    private VeiculoService veiculoService;

    @Mock
    private ModeloVeiculoService modeloVeiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private GerenciamentoTransacaoVeiculoRepository gerenciamentoTransacaoVeiculoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void compraDeveSalvarQuandoDadosValidos() {
        // Arrange
        CompraVeiculoDTO dto = criarDTO();
        Usuario usuario = criarUsuario();
        ModeloVeiculo modelo = new ModeloVeiculo();

        when(veiculoRepository.existsByPlaca(dto.getPlaca())).thenReturn(false);
        when(veiculoRepository.existsByChassi(dto.getChassi())).thenReturn(false);
        when(modeloVeiculoService.findById(dto.getModeloId())).thenReturn(modelo);
        when(usuarioService.usuarioLogado()).thenReturn(usuario);

        // Act
        veiculoService.compra(dto);

        // Assert
        verify(veiculoRepository).save(any(Veiculo.class));
        verify(gerenciamentoTransacaoVeiculoRepository).save(any(GerenciamentoTransacaoVeiculo.class));
    }

    @Test
    void compraDeveLancarExcecaoQuandoPlacaJaExistir() {
        CompraVeiculoDTO dto = criarDTO();
        when(veiculoRepository.existsByPlaca(dto.getPlaca())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veiculoService.compra(dto);
        });

        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void compraDeveLancarExcecaoQuandoChassiJaExistir() {
        CompraVeiculoDTO dto = criarDTO();
        when(veiculoRepository.existsByPlaca(dto.getPlaca())).thenReturn(false);
        when(veiculoRepository.existsByChassi(dto.getChassi())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veiculoService.compra(dto);
        });

        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void compraDeveLancarExcecaoQuandoModeloNaoExistir() {
        CompraVeiculoDTO dto = criarDTO();
        when(veiculoRepository.existsByPlaca(dto.getPlaca())).thenReturn(false);
        when(veiculoRepository.existsByChassi(dto.getChassi())).thenReturn(false);
        when(modeloVeiculoService.findById(dto.getModeloId()))
                .thenThrow(new RuntimeException("Modelo não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            veiculoService.compra(dto);
        });

        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void vendaDeveSalvarQuandoVeiculoDisponivel() {
        // Arrange
        VendaVeiculoDTO dto = criarVendaDTO();
        Usuario usuario = criarUsuario();
        Veiculo veiculo = criarVeiculoDisponivel(usuario.getOperador().getFilial());

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        when(veiculoRepository.findByPlacaAndFilialAndModeloId(
                dto.getPlaca(),
                usuario.getOperador().getFilial(),
                dto.getModeloId()))
                .thenReturn(Optional.of(veiculo));

        // Act
        veiculoService.venda(dto);

        // Assert
        verify(veiculoRepository).save(any(Veiculo.class));
        verify(gerenciamentoTransacaoVeiculoRepository).save(any(GerenciamentoTransacaoVeiculo.class));
    }

    @Test
    void vendaDeveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        VendaVeiculoDTO dto = criarVendaDTO();
        Usuario usuario = criarUsuario();

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        when(veiculoRepository.findByPlacaAndFilialAndModeloId(
                dto.getPlaca(),
                usuario.getOperador().getFilial(),
                dto.getModeloId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            veiculoService.venda(dto);
        });

        verify(veiculoRepository, never()).save(any());
        verify(gerenciamentoTransacaoVeiculoRepository, never()).save(any());
    }

    @Test
    void vendaDeveLancarExcecaoQuandoVeiculoNaoDisponivel() {
        VendaVeiculoDTO dto = criarVendaDTO();
        Usuario usuario = criarUsuario();
        Veiculo veiculo = criarVeiculoDisponivel(usuario.getOperador().getFilial());
        veiculo.setEstadoVeiculo(EstadoVeiculo.VENDIDO); // já vendido

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        when(veiculoRepository.findByPlacaAndFilialAndModeloId(
                dto.getPlaca(),
                usuario.getOperador().getFilial(),
                dto.getModeloId()))
                .thenReturn(Optional.of(veiculo));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veiculoService.venda(dto);
        });

        verify(veiculoRepository, never()).save(any());
        verify(gerenciamentoTransacaoVeiculoRepository, never()).save(any());
    }

    @Test
    void solicitarManutencaoDeveAtualizarVeiculoQuandoDisponivel() {
        // Arrange
        Integer veiculoId = 1;
        Usuario usuario = criarUsuario();
        Veiculo veiculo = criarVeiculoDisponivel(usuario.getOperador().getFilial());

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));

        // Act
        veiculoService.solicitarManutencao(veiculoId);

        // Assert
        verify(veiculoRepository).save(any(Veiculo.class));
        verify(gerenciamentoTransacaoVeiculoRepository).save(any(GerenciamentoTransacaoVeiculo.class));
    }

    @Test
    void solicitarManutencaoDeveLancarExcecaoSeVeiculoNaoDisponivel() {
        Integer veiculoId = 1;
        Usuario usuario = criarUsuario();
        Veiculo veiculo = criarVeiculoDisponivel(usuario.getOperador().getFilial());
        veiculo.setEstadoVeiculo(EstadoVeiculo.VENDIDO); // não disponível

        when(usuarioService.usuarioLogado()).thenReturn(usuario);
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veiculoService.solicitarManutencao(veiculoId);
        });

        verify(veiculoRepository, never()).save(any());
        verify(gerenciamentoTransacaoVeiculoRepository, never()).save(any());
    }

    @Test
    void finalizarManutencaoDeveAtualizarVeiculoQuandoEmManutencao() {
        // Arrange
        Integer veiculoId = 1;
        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoId);
        veiculo.setEstadoVeiculo(EstadoVeiculo.EM_MANUTENCAO);

        GerenciamentoTransacaoVeiculo transacao = new GerenciamentoTransacaoVeiculo();
        transacao.setVeiculo(veiculo);

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));
        when(gerenciamentoTransacaoVeiculoRepository.findByVeiculoManutencao(veiculo)).thenReturn(Optional.of(transacao));

        // Act
        veiculoService.finalizarManutencao(veiculoId);

        // Assert
        verify(gerenciamentoTransacaoVeiculoRepository).save(transacao);
        assertEquals(EstadoVeiculo.DISPONIVEL, veiculo.getEstadoVeiculo());
        assertNotNull(transacao.getDataFimTransacao());
    }

    @Test
    void finalizarManutencaoDeveLancarExcecaoSeVeiculoNaoEmManutencao() {
        Integer veiculoId = 1;
        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoId);
        veiculo.setEstadoVeiculo(EstadoVeiculo.VENDIDO);

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veiculoService.finalizarManutencao(veiculoId);
        });

        verify(gerenciamentoTransacaoVeiculoRepository, never()).save(any());
    }

    @Test
    void finalizarManutencaoDeveLancarExcecaoSeTransacaoNaoEncontrada() {
        Integer veiculoId = 1;
        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoId);
        veiculo.setEstadoVeiculo(EstadoVeiculo.EM_MANUTENCAO);

        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));
        when(gerenciamentoTransacaoVeiculoRepository.findByVeiculoManutencao(veiculo)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            veiculoService.finalizarManutencao(veiculoId);
        });

        verify(gerenciamentoTransacaoVeiculoRepository, never()).save(any());
    }

    private CompraVeiculoDTO criarDTO() {
        CompraVeiculoDTO dto = new CompraVeiculoDTO();
        dto.setModeloId(1);
        dto.setPlaca("ABC1D23");
        dto.setChassi("9BWZZZ377VT004251");
        dto.setCor("Preto");
        dto.setQuilometragem(1000);
        dto.setValor(50000.00);
        return dto;
    }

    private Usuario criarUsuario() {
        Usuario u = new Usuario();
        Operador o = new Operador();
        Filial f = new Filial();
        o.setFilial(f);
        u.setOperador(o);
        return u;
    }

    private Veiculo criarVeiculoDisponivel(Filial filial) {
        ModeloVeiculo modelo = new ModeloVeiculo();
        modelo.setId(1);
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("ABC1234");
        veiculo.setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);
        veiculo.setModelo(modelo);
        veiculo.setFilial(filial);
        return veiculo;
    }

    private VendaVeiculoDTO criarVendaDTO() {
        VendaVeiculoDTO dto = new VendaVeiculoDTO();
        dto.setPlaca("ABC1234");
        dto.setModeloId(1);
        dto.setValor(30000.00);
        return dto;
    }
}

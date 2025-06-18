package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.GerenciamentoTransacaoVeiculoRepository;
import com.cefet.vocealuga.repositories.VeiculoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


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
}

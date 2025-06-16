package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.GerenciamentoTransacaoVeiculoRepository;
import com.cefet.vocealuga.repositories.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final GerenciamentoTransacaoVeiculoRepository gerenciamentoTransacaoVeiculoRepository;
    private final ModeloVeiculoService modeloVeiculoService;
    private final UsuarioService usuarioService;

    public VeiculoService(VeiculoRepository veiculoRepository, GerenciamentoTransacaoVeiculoRepository gerenciamentoTransacaoVeiculoRepository, ModeloVeiculoService modeloVeiculoService, UsuarioService usuarioService) {
        this.veiculoRepository = veiculoRepository;
        this.gerenciamentoTransacaoVeiculoRepository = gerenciamentoTransacaoVeiculoRepository;
        this.modeloVeiculoService = modeloVeiculoService;
        this.usuarioService = usuarioService;
    }

    public List<Veiculo> findAll() {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return veiculoRepository.findAllByFilial(usuarioLogado.getOperador().getFilial());
    }

    @Transactional
    public void compra(CompraVeiculoDTO compraVeiculoDTO) {
        ModeloVeiculo modeloVeiculo = modeloVeiculoService.findById(compraVeiculoDTO.getModeloId());
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        Veiculo veiculo = new Veiculo();

        veiculo.setModelo(modeloVeiculo);
        veiculo.setPlaca(compraVeiculoDTO.getPlaca());
        veiculo.setChassi(compraVeiculoDTO.getChassi());
        veiculo.setFilial(usuarioLogado.getOperador().getFilial());
        veiculo.setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);
        veiculo.setCor(compraVeiculoDTO.getCor());
        veiculo.setQuilometragem(compraVeiculoDTO.getQuilometragem());
        veiculoRepository.save(veiculo);

        GerenciamentoTransacaoVeiculo gerenciamentoTransacaoVeiculo = new GerenciamentoTransacaoVeiculo();
        gerenciamentoTransacaoVeiculo.setTipoTransacao(TipoTransacaoVeiculo.COMPRA);
        gerenciamentoTransacaoVeiculo.setVeiculo(veiculo);
        gerenciamentoTransacaoVeiculo.setDataTransacao(LocalDate.now());
        gerenciamentoTransacaoVeiculo.setOperador(usuarioLogado.getOperador());
        gerenciamentoTransacaoVeiculo.setValor(compraVeiculoDTO.getValor());

        gerenciamentoTransacaoVeiculoRepository.save(gerenciamentoTransacaoVeiculo);
    }
}

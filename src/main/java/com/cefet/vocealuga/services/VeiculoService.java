package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.dtos.veiculos.SolicitacaoTransferenciaDTO;
import com.cefet.vocealuga.dtos.veiculos.VendaVeiculoDTO;
import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.GerenciamentoTransacaoVeiculoRepository;
import com.cefet.vocealuga.repositories.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class VeiculoService {
    private final static Logger LOGGER = Logger.getLogger(VeiculoService.class.getName());
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

    public List<Veiculo> findAllByModelo(Integer modeloId) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return veiculoRepository.findAllByFilialAndModeloId(usuarioLogado.getOperador().getFilial(), modeloId);
    }


    @Transactional
    public void compra(CompraVeiculoDTO compraVeiculoDTO) {
        ModeloVeiculo modeloVeiculo = modeloVeiculoService.findById(compraVeiculoDTO.getModeloId());

        if(veiculoRepository.existsByPlaca(compraVeiculoDTO.getPlaca())) {
            throw new IllegalArgumentException("Já existe um veículo com esta placa");
        }

        if(veiculoRepository.existsByChassi(compraVeiculoDTO.getChassi())) {
            throw new IllegalArgumentException("Já existe um veículo com este chassi");
        }

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

    @Transactional
    public void venda(@Valid VendaVeiculoDTO vendaVeiculoDTO) {
        LOGGER.info("venda de veiculo");
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        Veiculo veiculo = veiculoRepository.findByPlacaAndFilialAndModeloId(vendaVeiculoDTO.getPlaca(),
                        usuarioLogado.getOperador().getFilial(),
                        vendaVeiculoDTO.getModeloId())
                        .orElseThrow(() -> new EntityNotFoundException("Veiculo não encontrado!"));

        if(!EstadoVeiculo.DISPONIVEL.equals(veiculo.getEstadoVeiculo())) {
            throw new IllegalArgumentException("O veículo deve estar disponível para ser vendido!");
        }

        veiculo.setEstadoVeiculo(EstadoVeiculo.VENDIDO);
        veiculoRepository.save(veiculo);

        GerenciamentoTransacaoVeiculo gerenciamentoTransacaoVeiculo = new GerenciamentoTransacaoVeiculo();
        gerenciamentoTransacaoVeiculo.setTipoTransacao(TipoTransacaoVeiculo.VENDA);
        gerenciamentoTransacaoVeiculo.setVeiculo(veiculo);
        gerenciamentoTransacaoVeiculo.setDataTransacao(LocalDate.now());
        gerenciamentoTransacaoVeiculo.setValor(vendaVeiculoDTO.getValor());
        gerenciamentoTransacaoVeiculo.setOperador(usuarioLogado.getOperador());

        gerenciamentoTransacaoVeiculoRepository.save(gerenciamentoTransacaoVeiculo);
    }

    public List<GerenciamentoTransacaoVeiculo> historicoTransacoes() {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return gerenciamentoTransacaoVeiculoRepository.findAll(usuarioLogado.getOperador().getFilial());
    }

    public List<GerenciamentoTransacaoVeiculo> historicoTransacoesPorVeiculo(Veiculo veiculo) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return gerenciamentoTransacaoVeiculoRepository.findAllByVeiculo(veiculo);
    }

    public Veiculo findById(Integer id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }

    @Transactional
    public void solicitarManutencao(Integer id) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        Veiculo veiculo = findById(id);

        if(!EstadoVeiculo.DISPONIVEL.equals(veiculo.getEstadoVeiculo())) {
            throw new IllegalArgumentException("Veículo deve estar dísponível!");
        }

        LocalDate dataManutencao = LocalDate.now();
        veiculo.setUltimaManutencao(dataManutencao);
        veiculo.setEstadoVeiculo(EstadoVeiculo.EM_MANUTENCAO);
        veiculoRepository.save(veiculo);

        GerenciamentoTransacaoVeiculo gerenciamentoTransacaoVeiculo = new GerenciamentoTransacaoVeiculo();
        gerenciamentoTransacaoVeiculo.setTipoTransacao(TipoTransacaoVeiculo.MANUTENCAO);
        gerenciamentoTransacaoVeiculo.setVeiculo(veiculo);
        gerenciamentoTransacaoVeiculo.setDataTransacao(dataManutencao);
        gerenciamentoTransacaoVeiculo.setOperador(usuarioLogado.getOperador());
        gerenciamentoTransacaoVeiculo.setQuilometragem(veiculo.getQuilometragem());

        gerenciamentoTransacaoVeiculoRepository.save(gerenciamentoTransacaoVeiculo);
    }

    @Transactional
    public void finalizarManutencao(Integer id) {
        Veiculo veiculo = findById(id);

        if(!EstadoVeiculo.EM_MANUTENCAO.equals(veiculo.getEstadoVeiculo())) {
            throw new IllegalArgumentException("Veículo deve estar em manutenção!");
        }

        veiculo.setEstadoVeiculo(EstadoVeiculo.DISPONIVEL);

        GerenciamentoTransacaoVeiculo gerenciamentoTransacaoVeiculo = gerenciamentoTransacaoVeiculoRepository.findByVeiculoManutencao(veiculo)
                .orElseThrow(() -> new IllegalArgumentException("Ocorreu um erro ao tirar veículo de manutenção"));

        gerenciamentoTransacaoVeiculo.setDataFimTransacao(LocalDate.now());
        gerenciamentoTransacaoVeiculoRepository.save(gerenciamentoTransacaoVeiculo);
    }

    @Transactional
    public void solicitarTransferencia(@Valid SolicitacaoTransferenciaDTO solicitacaoTransferenciaDTO) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        Veiculo veiculo = veiculoRepository.findFirstByFilialIdAndModeloId(solicitacaoTransferenciaDTO.filialId(), solicitacaoTransferenciaDTO.modeloId())
                .orElseThrow(() -> new IllegalArgumentException("Nenhum veículo encontrado"));

        GerenciamentoTransacaoVeiculo gerenciamentoTransacaoVeiculo = new GerenciamentoTransacaoVeiculo();
        gerenciamentoTransacaoVeiculo.setDataTransacao(LocalDate.now());
        gerenciamentoTransacaoVeiculo.setVeiculo(veiculo);
        gerenciamentoTransacaoVeiculo.setOperador(usuarioLogado.getOperador());
        gerenciamentoTransacaoVeiculo.setFilialOrigem(veiculo.getFilial());
        gerenciamentoTransacaoVeiculo.setFilialDestino(usuarioLogado.getOperador().getFilial());
        gerenciamentoTransacaoVeiculo.setTipoTransacao(TipoTransacaoVeiculo.TRANSFERENCIA_SOLICITADA);

        gerenciamentoTransacaoVeiculoRepository.save(gerenciamentoTransacaoVeiculo);
    }

    public List<GerenciamentoTransacaoVeiculo> listarSolicitacoesTransferencia() {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return gerenciamentoTransacaoVeiculoRepository.buscaSolicitacoesTransferencia(usuarioLogado.getOperador().getFilial());
    }
}

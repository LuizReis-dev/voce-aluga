package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO;
import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.repositories.ModeloVeiculoRepository;
import com.cefet.vocealuga.repositories.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ModeloVeiculoService {

    private final ModeloVeiculoRepository modeloVeiculoRepository;
    private final UsuarioService usuarioService;
    private final VeiculoRepository veiculoRepository;

    public ModeloVeiculoService(ModeloVeiculoRepository modeloVeiculoRepository, UsuarioService usuarioService, VeiculoRepository veiculoRepository) {
        this.modeloVeiculoRepository = modeloVeiculoRepository;
        this.usuarioService = usuarioService;
        this.veiculoRepository = veiculoRepository;
    }

    public List<ModeloVeiculoDTO> findAllQuantidade() {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return modeloVeiculoRepository.listarModelosComQuantidade(usuarioLogado.getOperador().getFilial().getId());
    }

    public List<ModeloVeiculo> findAll() {
        return modeloVeiculoRepository.findAll();
    }

    public ModeloVeiculo findById(int id) {
        return modeloVeiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo Veiculo não encontrado"));
    }

    public List<ModeloVeiculoDTO> findAllByGrupo(Integer grupoId, LocalDate dataEntrega, LocalDate dataDevolucao) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        return modeloVeiculoRepository.listarModelosDisponiveisPorGrupoEFilial(usuarioLogado.getOperador().getFilial().getId(), grupoId, dataEntrega, dataDevolucao);
    }

    @Transactional
    public void salvar(ModeloVeiculo modeloVeiculo, MultipartFile imagem) {
        try {
            if(modeloVeiculo.getId() != null) {
                ModeloVeiculo modeloBD = findById(modeloVeiculo.getId());
                modeloVeiculo.setImagem(modeloBD.getImagem());
            }
            if (!imagem.isEmpty()) {
                String nomeArquivo = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
                Path pasta = Paths.get("uploads");
                Files.createDirectories(pasta);
                Path caminho = pasta.resolve(nomeArquivo);
                Files.copy(imagem.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
                modeloVeiculo.setImagem(nomeArquivo);
            }
            modeloVeiculoRepository.save(modeloVeiculo);
        }
         catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deletarModelo(Integer id) {
        if(veiculoRepository.existsByModeloId(id)) {
            throw new IllegalArgumentException("Não foi possível deletar esse modelo pois já possui veículos associados");
        }
        modeloVeiculoRepository.deleteById(id);
    }
}

package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.repositories.ModeloVeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeloVeiculoService {

    private final ModeloVeiculoRepository modeloVeiculoRepository;

    public ModeloVeiculoService(ModeloVeiculoRepository modeloVeiculoRepository) {
        this.modeloVeiculoRepository = modeloVeiculoRepository;
    }

    public List<ModeloVeiculo> findAll() {
        return modeloVeiculoRepository.findAll();
    }

    public ModeloVeiculo findById(int id) {
        return modeloVeiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo Veiculo não encontrado"));
    }

    @Transactional
    public ModeloVeiculo salvar(ModeloVeiculo modeloVeiculo) {
        return modeloVeiculoRepository.save(modeloVeiculo);
    }

    @Transactional
    public void deletarModelo(Integer id) {
        modeloVeiculoRepository.deleteById(id);
    }
}

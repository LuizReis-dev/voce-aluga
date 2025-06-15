package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.repositories.ModeloVeiculoRepository;
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
}

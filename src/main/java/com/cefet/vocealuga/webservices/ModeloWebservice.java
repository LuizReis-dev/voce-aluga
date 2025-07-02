package com.cefet.vocealuga.webservices;

import com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO;
import com.cefet.vocealuga.services.ModeloVeiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/api/v1/modelos")
public class ModeloWebservice {
    private final ModeloVeiculoService modeloVeiculoService;

    public ModeloWebservice(ModeloVeiculoService modeloVeiculoService) {
        this.modeloVeiculoService = modeloVeiculoService;
    }

    @GetMapping("/{grupoId}")
    public ResponseEntity<List<ModeloVeiculoDTO>> buscarModelosPorGrupo(@PathVariable Integer grupoId) {
        List<ModeloVeiculoDTO> response = this.modeloVeiculoService.findAllByGrupo(grupoId);
        return ResponseEntity.ok(response);
    }
}

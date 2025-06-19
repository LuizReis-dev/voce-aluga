package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

    List<Veiculo> findAllByFilial(Filial filial);
    List<Veiculo> findAllByFilialAndModeloId(Filial filial, Integer modeloId);
    boolean existsByPlaca(String placa);
    boolean existsByChassi(String chassi);

    Optional<Veiculo> findByPlacaAndFilialAndModeloId(String placa, Filial filial, Integer modeloId);
}

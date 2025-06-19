package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.EstadoVeiculo;
import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

    @Query("SELECT v FROM Veiculo v WHERE v.filial = :filial AND v.estadoVeiculo <> com.cefet.vocealuga.models.EstadoVeiculo.VENDIDO")
    List<Veiculo> findAllByFilial(@Param("filial") Filial filial);

    @Query("SELECT v FROM Veiculo v WHERE v.filial = :filial AND v.modelo.id = :modeloId AND v.estadoVeiculo <> com.cefet.vocealuga.models.EstadoVeiculo.VENDIDO")
    List<Veiculo> findAllByFilialAndModeloId(@Param("filial") Filial filial, @Param("modeloId") Integer modeloId);
    boolean existsByPlaca(String placa);
    boolean existsByChassi(String chassi);

    Optional<Veiculo> findByPlacaAndFilialAndModeloId(String placa, Filial filial, Integer modeloId);
}

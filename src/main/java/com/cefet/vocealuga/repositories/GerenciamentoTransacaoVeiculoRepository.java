package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.GerenciamentoTransacaoVeiculo;
import com.cefet.vocealuga.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GerenciamentoTransacaoVeiculoRepository extends JpaRepository<GerenciamentoTransacaoVeiculo, Integer> {

    @Query("SELECT gt FROM GerenciamentoTransacaoVeiculo gt " +
            "JOIN FETCH gt.veiculo JOIN FETCH gt.veiculo.filial " +
            "JOIN FETCH gt.veiculo.modelo " +
            "WHERE gt.veiculo.filial = :filial")
    List<GerenciamentoTransacaoVeiculo> findAll(@Param("filial") Filial filial);

    @Query("SELECT gt FROM GerenciamentoTransacaoVeiculo gt " +
            "JOIN FETCH gt.veiculo JOIN FETCH gt.veiculo.filial " +
            "JOIN FETCH gt.veiculo.modelo " +
            "WHERE gt.veiculo = :veiculo")
    List<GerenciamentoTransacaoVeiculo> findAllByVeiculo(@Param("veiculo") Veiculo veiculo);
}

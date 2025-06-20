package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.GerenciamentoTransacaoVeiculo;
import com.cefet.vocealuga.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GerenciamentoTransacaoVeiculoRepository extends JpaRepository<GerenciamentoTransacaoVeiculo, Integer> {

    @Query("SELECT gt FROM GerenciamentoTransacaoVeiculo gt " +
            "JOIN FETCH gt.veiculo JOIN FETCH gt.veiculo.filial " +
            "JOIN FETCH gt.veiculo.modelo " +
            "WHERE gt.veiculo.filial = :filial " +
            "ORDER BY gt.dataTransacao DESC, gt.id DESC")
    List<GerenciamentoTransacaoVeiculo> findAll(@Param("filial") Filial filial);

    @Query("SELECT gt FROM GerenciamentoTransacaoVeiculo gt " +
            "JOIN FETCH gt.veiculo JOIN FETCH gt.veiculo.filial " +
            "JOIN FETCH gt.veiculo.modelo " +
            "WHERE gt.veiculo = :veiculo " +
            "ORDER BY gt.dataTransacao DESC, gt.id DESC")
    List<GerenciamentoTransacaoVeiculo> findAllByVeiculo(@Param("veiculo") Veiculo veiculo);


    @Query("SELECT gt FROM GerenciamentoTransacaoVeiculo gt " +
            "WHERE gt.veiculo = :veiculo " +
            " AND gt.tipoTransacao = com.cefet.vocealuga.models.TipoTransacaoVeiculo.MANUTENCAO " +
            " AND gt.dataFimTransacao IS NULL " +
            "ORDER BY gt.dataTransacao DESC, gt.id DESC")
    Optional<GerenciamentoTransacaoVeiculo> findByVeiculoManutencao(Veiculo veiculo);

    @Query("SELECT gt FROM GerenciamentoTransacaoVeiculo gt " +
            "WHERE gt.filialOrigem = :filial " +
            " AND gt.tipoTransacao = com.cefet.vocealuga.models.TipoTransacaoVeiculo.TRANSFERENCIA_SOLICITADA " +
            " AND gt.dataFimTransacao IS NULL " +
            "ORDER BY gt.dataTransacao DESC, gt.id DESC")
    List<GerenciamentoTransacaoVeiculo> buscaSolicitacoesTransferencia(@Param("filial") Filial filial);

    @Query("SELECT COUNT(gt) > 0 FROM GerenciamentoTransacaoVeiculo gt " +
            "WHERE gt.filialOrigem = :filial " +
            "AND gt.tipoTransacao = com.cefet.vocealuga.models.TipoTransacaoVeiculo.TRANSFERENCIA_SOLICITADA " +
            "AND gt.dataFimTransacao IS NULL")
    boolean existeSolicitacaoTransferencia(@Param("filial") Filial filial);

}

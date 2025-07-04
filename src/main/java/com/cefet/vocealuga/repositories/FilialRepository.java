package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.dtos.filial.FilialDTO;
import com.cefet.vocealuga.models.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilialRepository extends JpaRepository<Filial, Integer> {
	@Query("""
			SELECT DISTINCT new com.cefet.vocealuga.dtos.filial.FilialDTO(f.id, f.nome)
			FROM Filial f
			JOIN f.veiculos v
			JOIN v.modelo mv
			WHERE mv.id = :modeloId
			  AND v.estadoVeiculo = com.cefet.vocealuga.models.EstadoVeiculo.DISPONIVEL
			  AND f <> :filial
			""")
	List<FilialDTO> buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(
			@Param("modeloId") Integer modeloId,
			@Param("filial") Filial filial);

	@Query("""
			SELECT DISTINCT new com.cefet.vocealuga.dtos.filial.FilialDTO(f.id, f.nome)
			FROM Filial f
			JOIN f.veiculos v
			JOIN v.modelo mv
			WHERE mv.id = :modeloId
			  AND v.estadoVeiculo = com.cefet.vocealuga.models.EstadoVeiculo.DISPONIVEL
			""")
	List<FilialDTO> buscarFiliaisComVeiculoDisponivel(@Param("modeloId") Integer modeloId);
}

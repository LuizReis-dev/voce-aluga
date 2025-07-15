package com.cefet.vocealuga.filial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilialRepository extends JpaRepository<Filial, Integer> {
	@Query("""
		SELECT DISTINCT new com.cefet.vocealuga.filial.FilialDTO(
			f.id,
			f.nome,
			CONCAT(f.endereco.logradouro, ' ', f.endereco.numero, ' ', f.endereco.complemento)
		)
		FROM Filial f
		JOIN f.veiculos v
		JOIN v.modelo mv
		WHERE mv.id = :modeloId
		  AND v.estadoVeiculo = com.cefet.vocealuga.veiculo.EstadoVeiculo.DISPONIVEL
		  AND f <> :filial
	""")
	List<FilialDTO> buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(
			@Param("modeloId") Integer modeloId,
			@Param("filial") Filial filial);

	@Query("""
			SELECT DISTINCT new com.cefet.vocealuga.filial.FilialDTO(
				f.id,
				f.nome,
				CONCAT(f.endereco.logradouro, ' ', f.endereco.numero, ' ', f.endereco.complemento)
			)
			FROM Filial f
			JOIN f.veiculos v
			JOIN v.modelo mv
			WHERE mv.id = :modeloId
			  AND v.estadoVeiculo = com.cefet.vocealuga.veiculo.EstadoVeiculo.DISPONIVEL
			""")
	List<FilialDTO> buscarFiliaisComVeiculoDisponivel(@Param("modeloId") Integer modeloId);
}

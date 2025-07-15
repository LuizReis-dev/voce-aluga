package com.cefet.vocealuga.veiculo.modelo;

import com.cefet.vocealuga.veiculo.dtos.ModeloMarcaDTO;
import com.cefet.vocealuga.veiculo.dtos.ModeloVeiculoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ModeloVeiculoRepository extends JpaRepository<ModeloVeiculo, Integer> {

	@Query("""
			     SELECT new com.cefet.vocealuga.veiculo.dtos.ModeloVeiculoDTO(
			         mv.id, mv.modelo, mv.marca, mv.ano, mv.imagem,
			         g.nome,
			         COUNT(v.id)
			     )
			     FROM ModeloVeiculo mv
			     JOIN mv.grupo g
			     LEFT JOIN Veiculo v ON v.modelo.id = mv.id AND v.filial.id = :filialId AND v.estadoVeiculo <> com.cefet.vocealuga.veiculo.EstadoVeiculo.VENDIDO\s
			     GROUP BY mv.id, mv.modelo, mv.marca, mv.ano
			     ORDER BY mv.marca
			\s""")
	List<ModeloVeiculoDTO> listarModelosComQuantidade(@Param("filialId") Integer filialId);

	@Query("""
			SELECT new com.cefet.vocealuga.veiculo.dtos.ModeloVeiculoDTO(
			    mv.id, mv.modelo, mv.marca, mv.ano, mv.imagem,
			    g.nome,
			    COUNT(v.id)
			)
			FROM ModeloVeiculo mv
			JOIN mv.grupo g
			LEFT JOIN Veiculo v
			    ON v.modelo.id = mv.id
			    AND v.filial.id = :filialId
			    AND v.estadoVeiculo = com.cefet.vocealuga.veiculo.EstadoVeiculo.DISPONIVEL
			    AND NOT EXISTS (
			        SELECT 1 FROM Reserva r
			        WHERE r.veiculo.id = v.id
			          AND r.dataEntrega <= :dataDevolucao
			          AND r.dataDevolucao >= :dataEntrega
      			      AND r.status != com.cefet.vocealuga.reserva.StatusReserva.CANCELADA
			    )
			WHERE mv.grupo.id = :grupoId
			GROUP BY mv.id, mv.modelo, mv.marca, mv.ano, g.nome
			ORDER BY mv.marca
			""")
	List<ModeloVeiculoDTO> listarModelosDisponiveisPorGrupoEFilial(
			@Param("filialId") Integer filialId,
			@Param("grupoId") Integer grupoId,
			@Param("dataEntrega") LocalDate dataEntrega,
			@Param("dataDevolucao") LocalDate dataDevolucao);

	@Query("""
			     SELECT new com.cefet.vocealuga.veiculo.dtos.ModeloVeiculoDTO(
			         mv.id, mv.modelo, mv.marca, mv.ano, mv.imagem,
			         g.nome,
			         COUNT(v.id)
			     )
			     FROM ModeloVeiculo mv
			     JOIN mv.grupo g
			     LEFT JOIN Veiculo v ON v.modelo.id = mv.id AND v.estadoVeiculo = com.cefet.vocealuga.veiculo.EstadoVeiculo.DISPONIVEL\s
			     GROUP BY mv.id, mv.modelo, mv.marca, mv.ano
			     ORDER BY mv.marca
			\s""")
	Page<ModeloVeiculoDTO> listarModelosComQuantidadePublico(org.springframework.data.domain.Pageable pageable);

	@Query("""
			    SELECT new com.cefet.vocealuga.veiculo.dtos.ModeloMarcaDTO(
			        mv.modelo,
			        mv.marca
			    )
			    FROM ModeloVeiculo mv
			    WHERE mv.id = :id
			""")
	ModeloMarcaDTO findModeloMarcaById(@Param("id") Integer id);
}

package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO;
import com.cefet.vocealuga.models.ModeloVeiculo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModeloVeiculoRepository extends JpaRepository<ModeloVeiculo, Integer> {

	@Query("""
			     SELECT new com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO(
			         mv.id, mv.modelo, mv.marca, mv.ano, mv.imagem,
			         g.nome,
			         COUNT(v.id)
			     )
			     FROM ModeloVeiculo mv
			     JOIN mv.grupo g
			     LEFT JOIN Veiculo v ON v.modelo.id = mv.id AND v.filial.id = :filialId AND v.estadoVeiculo <> com.cefet.vocealuga.models.EstadoVeiculo.VENDIDO\s
			     GROUP BY mv.id, mv.modelo, mv.marca, mv.ano
			     ORDER BY mv.marca
			\s""")
	List<ModeloVeiculoDTO> listarModelosComQuantidade(@Param("filialId") Integer filialId);

	@Query("""
			     SELECT new com.cefet.vocealuga.dtos.veiculos.ModeloVeiculoDTO(
			         mv.id, mv.modelo, mv.marca, mv.ano, mv.imagem,
			         g.nome,
			         COUNT(v.id)
			     )
			     FROM ModeloVeiculo mv
			     JOIN mv.grupo g
			     LEFT JOIN Veiculo v ON v.modelo.id = mv.id AND v.estadoVeiculo = com.cefet.vocealuga.models.EstadoVeiculo.DISPONIVEL\s
			     GROUP BY mv.id, mv.modelo, mv.marca, mv.ano
			     ORDER BY mv.marca
			\s""")
	Page<ModeloVeiculoDTO> listarModelosComQuantidadePublico(org.springframework.data.domain.Pageable pageable);
}

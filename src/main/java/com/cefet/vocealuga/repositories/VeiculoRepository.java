package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.EstadoVeiculo;
import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

	@Query("SELECT v FROM Veiculo v JOIN FETCH v.modelo WHERE v.filial = :filial AND v.estadoVeiculo <> com.cefet.vocealuga.models.EstadoVeiculo.VENDIDO")
	List<Veiculo> findAllByFilial(@Param("filial") Filial filial);

	@Query("SELECT v FROM Veiculo v JOIN FETCH v.modelo WHERE v.filial = :filial AND v.modelo.id = :modeloId AND v.estadoVeiculo <> com.cefet.vocealuga.models.EstadoVeiculo.VENDIDO")
	List<Veiculo> findAllByFilialAndModeloId(@Param("filial") Filial filial, @Param("modeloId") Integer modeloId);

	boolean existsByPlaca(String placa);

	boolean existsByChassi(String chassi);

	Optional<Veiculo> findByPlacaAndFilialAndModeloId(String placa, Filial filial, Integer modeloId);

	Optional<Veiculo> findFirstByFilialIdAndModeloIdAndEstadoVeiculoOrderByIdAsc(
			Integer filialId,
			Integer modeloId,
			EstadoVeiculo estadoVeiculo);

	@Query("""
			    SELECT v FROM Veiculo v
			    WHERE v.filial.id = :filialId
			      AND v.modelo.id = :modeloId
			      AND v.estadoVeiculo = com.cefet.vocealuga.models.EstadoVeiculo.DISPONIVEL
			      AND NOT EXISTS (
			          SELECT 1 FROM Reserva r
			          WHERE r.veiculo.id = v.id
			            AND r.dataEntrega <= :dataDevolucao
			            AND r.dataDevolucao >= :dataEntrega
			      )
			    ORDER BY v.id ASC
			""")
	List<Veiculo>findFirstDisponiveisSemReserva(
			@Param("filialId") Integer filialId,
			@Param("modeloId") Integer modeloId,
			@Param("dataEntrega") LocalDate dataEntrega,
			@Param("dataDevolucao") LocalDate dataDevolucao);

	@Query("SELECT new com.cefet.vocealuga.dtos.veiculos.GrupoDTO(g.id, g.nome) " +
			"FROM ModeloVeiculo mv LEFT JOIN mv.grupo g WHERE mv.id = :id")
	com.cefet.vocealuga.dtos.veiculos.GrupoDTO findGrupoNomeAndIdByModeloVeiculoId(@Param("id") Integer id);

	boolean existsByModeloId(Integer modeloId);
}

package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Reserva;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reserva r
        WHERE r.cliente.id = :clienteId
          AND r.dataEntrega <= :dataDevolucao
          AND r.dataDevolucao >= :dataEntrega
    """)
    boolean clientePossuiReservaNoPeriodo(
            @Param("clienteId") Integer clienteId,
            @Param("dataEntrega") LocalDate dataEntrega,
            @Param("dataDevolucao") LocalDate dataDevolucao
    );

    @Query("""
        SELECT r
        FROM Reserva r
        WHERE
          r.dataEntrega = :dataEntrega
    """)
    List<Reserva> buscaReservasPorDiaDeEntrega(
            @Param("dataEntrega") LocalDate dataEntrega
    );

    @Query("""
        SELECT DATE(r.dataEntrega), COUNT(r)
        FROM Reserva r
        WHERE r.dataEntrega >= :inicioSemana AND r.dataEntrega <= :fimSemana
        GROUP BY DATE(r.dataEntrega)
        ORDER BY DATE(r.dataEntrega)
    """)
    List<Object[]> contarReservasPorDia(@Param("inicioSemana") LocalDate inicio, @Param("fimSemana") LocalDate fim);

    @Query("""
        SELECT r.origem, COUNT(r)
        FROM Reserva r
        GROUP BY r.origem
    """)
    List<Object[]> contarReservasPorOrigem();
}

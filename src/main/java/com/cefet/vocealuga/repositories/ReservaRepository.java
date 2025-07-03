package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

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

}

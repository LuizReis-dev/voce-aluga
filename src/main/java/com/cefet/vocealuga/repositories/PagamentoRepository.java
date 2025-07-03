package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}

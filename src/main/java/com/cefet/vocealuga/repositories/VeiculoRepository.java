package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {

    List<Veiculo> findAllByFilial(Filial filial);
}

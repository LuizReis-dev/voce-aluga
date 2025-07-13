package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
}

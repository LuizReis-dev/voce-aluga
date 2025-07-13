package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Usuario;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	@EntityGraph(attributePaths = { "operador", "cliente" })
	Optional<Usuario> findByEmail(String username);

	Optional<Usuario> findByCpf(String cpf);

	boolean existsByEmailOrCpf(String email, String cpf);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE (u.email = :email OR u.cpf = :cpf) AND u.id <> :id")
	boolean existsByEmailOrCpfAndIdNot(@Param("email") String email, @Param("cpf") String cpf, @Param("id") Integer id);

	@Query("SELECT u FROM Usuario u WHERE u.operador IS NOT NULL")
	List<Usuario> buscaOperadores();
}
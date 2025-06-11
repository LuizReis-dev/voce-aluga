package com.cefet.vocealuga.repositories;

import com.cefet.vocealuga.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String username);
    boolean existsByEmailOrCpf(String email, String cpf);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE (u.email = :email OR u.cpf = :cpf) AND u.id <> :id")
    boolean existsByEmailOrCpfAndIdNot(@Param("email") String email, @Param("cpf") String cpf, @Param("id") Integer id);
}
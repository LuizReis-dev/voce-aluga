package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.CargoOperador;
import com.cefet.vocealuga.models.Operador;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.repositories.UsuarioRepository;
import com.cefet.vocealuga.utils.CPFValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private static Usuario usuarioLogado;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public Usuario usuarioLogado() {
        var usuario = (Usuario) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if(usuarioLogado == null || !usuarioLogado.getId().equals(usuario.getId())) {

            usuarioLogado = findById(usuario.getId());
        }
        return usuarioLogado;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if(usuario.getId() != null) {
            return editar(usuario);
        }

        if(usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())) {
            throw new EntityExistsException("Já existe um usuário com este email ou cpf");
        }

        if(!CPFValidator.validarCPF(usuario.getCpf())) {
            throw new RuntimeException("CPF inválido");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Operador operador = new Operador();
        operador.setCargo(CargoOperador.OPERADOR);
        operador.setFilial(usuarioLogado().getOperador().getFilial());
        operador.setUsuario(usuario);

        usuario.setOperador(operador);

        return usuarioRepository.save(usuario);
    }

    public Usuario editar(Usuario usuario) {
        Usuario existente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (usuarioRepository.existsByEmailOrCpfAndIdNot(usuario.getEmail(), usuario.getCpf(), usuario.getId())) {
            throw new EntityExistsException("Já existe um usuário com este email ou cpf");
        }

        if (!CPFValidator.validarCPF(usuario.getCpf())) {
            throw new RuntimeException("CPF inválido");
        }

        existente.setNome(usuario.getNome());
        existente.setEmail(usuario.getEmail());
        existente.setCpf(usuario.getCpf());

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank() &&
                !passwordEncoder.matches(usuario.getSenha(), existente.getSenha())) {
            existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(existente);
    }

    @Transactional
    public void deletarUsuario(Integer id) {
        if(usuarioLogado().getId().equals(id)) {
            throw new RuntimeException("Você não pode se auto deletar!");
        }
        usuarioRepository.deleteById(id);
    }
}
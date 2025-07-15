package com.cefet.vocealuga.usuario;

import com.cefet.vocealuga.cliente.Cliente;
import com.cefet.vocealuga.cliente.ClienteRepository;
import com.cefet.vocealuga.utils.CPFValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final ClienteRepository clienteRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
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
		if (usuarioLogado == null || !usuarioLogado.getId().equals(usuario.getId())) {

			usuarioLogado = findById(usuario.getId());
		}
		return usuarioLogado;
	}

	public List<Usuario> findAll() {
		return usuarioRepository.buscaOperadores();
	}

	public List<Cliente> findAllClientes() {
		return clienteRepository.findAll();
	}

	public Usuario findById(Integer id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}

	@Transactional
	public Usuario salvar(Usuario usuario) {
		if (usuario.getId() != null) {
			return editar(usuario);
		}

		if (usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())) {
			throw new EntityExistsException("Já existe um usuário com este email ou cpf");
		}

		if (!CPFValidator.validarCPF(usuario.getCpf())) {
			throw new RuntimeException("CPF inválido");
		}

		Operador operador = usuario.getOperador();
		operador.setUsuario(usuario);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		return usuarioRepository.save(usuario);
	}

	public Usuario salvarCliente(Usuario usuario) {
		if (usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())) {
			throw new EntityExistsException("Já existe um usuário com este email ou cpf");
		}

		if (!CPFValidator.validarCPF(usuario.getCpf())) {
			throw new RuntimeException("CPF inválido");
		}

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		Cliente cliente = new Cliente();
		cliente.setUsuario(usuario);
		usuario.setCliente(cliente);

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
		existente.getOperador().setCargo(usuario.getOperador().getCargo());
		existente.getOperador().setFilial(usuario.getOperador().getFilial());

		if (usuario.getSenha() != null && !usuario.getSenha().isBlank() &&
				!passwordEncoder.matches(usuario.getSenha(), existente.getSenha())) {
			existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}

		return usuarioRepository.save(existente);
	}

	@Transactional
	public void deletarUsuario(Integer id) {
		if (usuarioLogado().getId().equals(id)) {
			throw new RuntimeException("Você não pode se auto deletar!");
		}
		usuarioRepository.deleteById(id);
	}
}
package com.cefet.vocealuga.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.cefet.vocealuga.filial.Filial;
import com.cefet.vocealuga.usuario.Operador;
import com.cefet.vocealuga.usuario.Usuario;
import com.cefet.vocealuga.cliente.ClienteRepository;
import com.cefet.vocealuga.usuario.UsuarioRepository;
import com.cefet.vocealuga.usuario.UsuarioService;
import com.cefet.vocealuga.utils.CPFValidator;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.MockedStatic;

public class UsuarioServiceTest {
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = spy(new UsuarioService(usuarioRepository, clienteRepository, passwordEncoder));
    }

    @Test
    void salvarDeveRetornarExcecaoQuandoUsuarioExistirPorEmailOuCpf() {
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("email@test.com");
        usuario.setCpf("12345678900");

        when(usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())).thenReturn(true);

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            usuarioService.salvar(usuario);
        });

        assertEquals("Já existe um usuário com este email ou cpf", exception.getMessage());
    }

    @Test
    void salvarDeveRetornarExcecaoQuandoCpfForInvalido() {
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("email@test.com");
        usuario.setCpf("12345678900");

        when(usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())).thenReturn(false);

        // Mockando método estático do CPFValidator
        try (MockedStatic<CPFValidator> mockedCpfValidator = mockStatic(CPFValidator.class)) {
            mockedCpfValidator.when(() -> CPFValidator.validarCPF(usuario.getCpf())).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                usuarioService.salvar(usuario);
            });

            assertEquals("CPF inválido", exception.getMessage());
        }
    }

    @Test
    void salvarDeveChamarEditarQuandoIdNaoForNulo() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        // Mocka o método editar para retornar o próprio usuário (simples)
        doReturn(usuario).when(usuarioService).editar(usuario);

        Usuario resultado = usuarioService.salvar(usuario);

        verify(usuarioService, times(1)).editar(usuario);
        assertEquals(usuario, resultado);
    }

    @Test
    void salvarDeveSalvarUsuarioQuandoTudoEstiverCorreto() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("email@test.com");
        usuario.setCpf("12345678900");
        usuario.setSenha("senha123");

        // O operador precisa estar definido para simular corretamente o método salvar
        Operador operador = new Operador();
        usuario.setOperador(operador);

        // Mock para verificar se já existe email ou CPF
        when(usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())).thenReturn(false);

        // Mock do validador de CPF
        try (MockedStatic<CPFValidator> mockedCpfValidator = mockStatic(CPFValidator.class)) {
            mockedCpfValidator.when(() -> CPFValidator.validarCPF(usuario.getCpf())).thenReturn(true);

            // Mock do encode da senha
            when(passwordEncoder.encode(usuario.getSenha())).thenReturn("senhaCodificada");

            // Mock do método usuarioLogado() caso necessário no método
            Usuario usuarioLogado = new Usuario();
            Operador operadorLogado = new Operador();
            operadorLogado.setFilial(new Filial());
            usuarioLogado.setOperador(operadorLogado);
            doReturn(usuarioLogado).when(usuarioService).usuarioLogado();

            // Mock do save
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Usuario resultado = usuarioService.salvar(usuario);

            // Assert
            verify(passwordEncoder).encode("senha123");
            verify(usuarioRepository).save(any(Usuario.class));
            assertNotNull(resultado.getOperador());
            assertEquals("senhaCodificada", resultado.getSenha());
            assertEquals(usuario, resultado);
            assertEquals(usuario, operador.getUsuario()); // relação bidirecional
        }
    }
}

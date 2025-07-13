package com.cefet.vocealuga.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.cefet.vocealuga.models.CargoOperador;
import com.cefet.vocealuga.models.Filial;
import com.cefet.vocealuga.models.Operador;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.repositories.UsuarioRepository;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = spy(new UsuarioService(usuarioRepository, passwordEncoder));
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
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("email@test.com");
        usuario.setCpf("12345678900");
        usuario.setSenha("senha123");

        when(usuarioRepository.existsByEmailOrCpf(usuario.getEmail(), usuario.getCpf())).thenReturn(false);

        try (MockedStatic<CPFValidator> mockedCpfValidator = mockStatic(CPFValidator.class)) {
            mockedCpfValidator.when(() -> CPFValidator.validarCPF(usuario.getCpf())).thenReturn(true);

            when(passwordEncoder.encode(usuario.getSenha())).thenReturn("senhaCodificada");

            // Mockando usuarioLogado()
            Usuario usuarioLogado = new Usuario();
            Operador operadorLogado = new Operador();
            operadorLogado.setFilial(new Filial());
            usuarioLogado.setOperador(operadorLogado);

            doReturn(usuarioLogado).when(usuarioService).usuarioLogado();

            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Usuario resultado = usuarioService.salvar(usuario);

            verify(passwordEncoder).encode("senha123");
            verify(usuarioRepository).save(any(Usuario.class));
            assertNotNull(resultado.getOperador());
        }
    }
}

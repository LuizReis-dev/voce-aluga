package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.Cliente;
import com.cefet.vocealuga.models.Endereco;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.repositories.ClienteRepository;
import com.cefet.vocealuga.repositories.UsuarioRepository;
import com.cefet.vocealuga.utils.CPFValidator;
import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import com.cefet.vocealuga.webservices.requests.CreateClienteRequest;
import com.cefet.vocealuga.webservices.responses.ClienteResponse;
import com.cefet.vocealuga.webservices.responses.CreateClienteResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ClienteResponse buscaPorCpf(String cpf) {
        if(!CPFValidator.validarCPF(cpf)) {
            throw new WebserviceException("CPF inválido!", true);
        }
        return this.clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new WebserviceException("Cliente não encontrado, prosseguir com o cadastro!", false));
    }

    @Transactional
    public CreateClienteResponse create(CreateClienteRequest request) {
        if(!CPFValidator.validarCPF(request.getCpf())) {
            throw new WebserviceException("CPF inválido!", true);
        }

        Usuario usuario = usuarioRepository.findByCpf(request.getCpf()).orElse(null);
        Cliente cliente;

        if (usuario == null) {
            // Novo usuário
            usuario = new Usuario();
            usuario.setCpf(request.getCpf());
        }

        // Atualiza dados do usuário
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setTelefone(request.getTelefone());
        usuario.setDataNascimento(request.getDataNascimento());

        // Cria ou atualiza o endereço
        Endereco endereco;
        if (usuario.getCliente() != null) {
            endereco = usuario.getCliente().getEndereco();
        } else {
            endereco = new Endereco();
        }

        endereco.setCep(request.getCep().replace("-", ""));
        endereco.setLogradouro(request.getRua());
        endereco.setNumero(request.getNumero());
        endereco.setComplemento(request.getComplemento());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getUf());

        // Cria ou atualiza o cliente
        if (usuario.getCliente() != null) {
            cliente = usuario.getCliente();
        } else {
            cliente = new Cliente();
        }

        cliente.setCnh(request.getCnh());
        cliente.setApolice(request.getApolice());
        cliente.setEndereco(endereco);
        cliente.setUsuario(usuario);

        usuario.setCliente(cliente);

        usuarioRepository.save(usuario);

        return new CreateClienteResponse("Cliente criado/atualizado com sucesso!", cliente.getId());
    }
}

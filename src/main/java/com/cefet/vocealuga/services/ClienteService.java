package com.cefet.vocealuga.services;

import com.cefet.vocealuga.repositories.ClienteRepository;
import com.cefet.vocealuga.webservices.exceptions.ClienteNaoEncontradoException;
import com.cefet.vocealuga.webservices.responses.ClienteResponse;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse buscaPorCpf(String cpf) {
        return this.clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não cadastrado!"));
    }

}

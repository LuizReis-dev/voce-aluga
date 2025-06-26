package com.cefet.vocealuga.webservices;

import com.cefet.vocealuga.services.ClienteService;
import com.cefet.vocealuga.webservices.responses.ClienteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/clientes")
public class ClienteWebservice {

    private final ClienteService clienteService;
    public ClienteWebservice(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable String cpf) {
        ClienteResponse response = this.clienteService.buscaPorCpf(cpf);
        return ResponseEntity.ok(response);
    }
}

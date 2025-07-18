package com.cefet.vocealuga.webservices;

import com.cefet.vocealuga.services.ClienteService;
import com.cefet.vocealuga.webservices.requests.CreateClienteRequest;
import com.cefet.vocealuga.webservices.responses.ClienteResponse;
import com.cefet.vocealuga.webservices.responses.CreateClienteResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<CreateClienteResponse> create(@RequestBody @Valid CreateClienteRequest request) {
        CreateClienteResponse response = this.clienteService.create(request);
        return ResponseEntity.ok(response);
    }
}

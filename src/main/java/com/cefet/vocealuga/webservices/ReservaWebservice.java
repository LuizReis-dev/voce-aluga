package com.cefet.vocealuga.webservices;

import com.cefet.vocealuga.services.ReservaService;
import com.cefet.vocealuga.webservices.requests.CalculoValorReservaRequest;
import com.cefet.vocealuga.webservices.responses.CalculoValorReservaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/reservas")
public class ReservaWebservice {
    private final ReservaService reservaService;

    public ReservaWebservice(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/calcular-valor")
    public ResponseEntity<CalculoValorReservaResponse> calculoValorReserva(@RequestBody CalculoValorReservaRequest request) {
        return ResponseEntity.ok(reservaService.calcularValorReserva(request));
    }
}

package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.repositories.GrupoRepository;
import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import com.cefet.vocealuga.webservices.requests.CalculoValorReservaRequest;
import com.cefet.vocealuga.webservices.responses.CalculoValorReservaResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ReservaService {
    private final GrupoRepository grupoRepository;

    public ReservaService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public CalculoValorReservaResponse calcularValorReserva(CalculoValorReservaRequest calculoValorReservaRequest) {
        if (calculoValorReservaRequest.getDateEntrega() == null || calculoValorReservaRequest.getDataDevolucao() == null || calculoValorReservaRequest.getGrupoId() == null) {
            throw new WebserviceException("Datas e grupo id não podem ser nulos.", false);
        }
        LocalDate dataDevolucao = calculoValorReservaRequest.getDataDevolucao();
        LocalDate dataEntrega = calculoValorReservaRequest.getDateEntrega();
        Grupo grupo = grupoRepository.findById(calculoValorReservaRequest.getGrupoId())
                .orElseThrow(() -> new WebserviceException("Grupo não encontrado.", false));
        BigDecimal valorDiario = grupo.getPrecoPorDia();

        if (dataDevolucao.isBefore(dataEntrega)) {
            throw new IllegalArgumentException("Data de devolução não pode ser antes da data de entrega.");
        }

        // Quantidade de dias (incluindo o dia da entrega)
        long dias = ChronoUnit.DAYS.between(dataEntrega, dataDevolucao) + 1;

        return new CalculoValorReservaResponse(valorDiario.multiply(BigDecimal.valueOf(dias)));
    }
}

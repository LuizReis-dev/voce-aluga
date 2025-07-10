package com.cefet.vocealuga.tasks;

import com.cefet.vocealuga.models.EstadoVeiculo;
import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.models.StatusReserva;
import com.cefet.vocealuga.models.Veiculo;
import com.cefet.vocealuga.repositories.ReservaRepository;
import com.cefet.vocealuga.repositories.VeiculoRepository;
import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.logging.Logger;

@Component
public class ReservaTask implements CommandLineRunner {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ReservaRepository reservaRepository;
    private final VeiculoRepository veiculoRepository;

    public ReservaTask(ReservaRepository reservaRepository, VeiculoRepository veiculoRepository) {
        this.reservaRepository = reservaRepository;
        this.veiculoRepository = veiculoRepository;
    }

    // roda todo dia às 00:00
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cronAtualizaVeiculosReservados() {
        LocalDate hoje = LocalDate.now();
        atualizaVeiculosReservados(hoje);
    }

    @Transactional
    public void atualizaVeiculosReservados(LocalDate diaEntrega) {
        logger.info("Atualizando veiculos reservados...");
        var reservasDoDia = reservaRepository.buscaReservasPorDiaDeEntrega(diaEntrega);

        for (var reserva : reservasDoDia) {
            logger.info("Verificando reserva : " + reserva.getId());

            Veiculo veiculoReservado = reserva.getVeiculo();
            boolean veiculoJaEstaRervado = EstadoVeiculo.RESERVADO.equals(veiculoReservado.getEstadoVeiculo());
            if (veiculoJaEstaRervado) {
                logger.info("Veículo já reservado, nada a ser feito");
                continue;
            }
            boolean veiculoDisponivel = EstadoVeiculo.DISPONIVEL.equals(veiculoReservado.getEstadoVeiculo());

            if (veiculoDisponivel) {
                veiculoReservado.setEstadoVeiculo(EstadoVeiculo.RESERVADO);
                reserva.setStatus(StatusReserva.AGUARDANDO_ENTREGA);
                reservaRepository.save(reserva);
                logger.info("Reservando veículo, e alterando status da reserva para AGUARDANDO_ENTREGA");
                continue;
            }

            logger.info("Veículo não está disponível, fazer upgrade automatico ");

            var grupo = veiculoReservado.getModelo().getGrupo();
            logger.info("Grupo atual :" + grupo.getNome());

            String grupoUpgrade = switch (grupo.getNome()) {
                case "GRUPO A" -> "GRUPO B";
                case "GRUPO B", "GRUPO C" -> "GRUPO C";
                default -> null;
            };

            logger.info("Fazendo upgrade para o grupo :" + grupoUpgrade);

            Veiculo novoVeiculo =
                    veiculoRepository.findFirstDisponiveiGrupo(
                                    veiculoReservado.getFilial().getId(),
                                    grupoUpgrade, reserva.getDataEntrega(),
                                    reserva.getDataDevolucao())
                            .stream()
                            .findFirst()
                            .orElse(null);

            if(novoVeiculo == null) {
                continue;
            }

            novoVeiculo.setEstadoVeiculo(EstadoVeiculo.RESERVADO);
            reserva.setVeiculoOriginal(veiculoReservado);
            reserva.setVeiculo(novoVeiculo);
            reserva.setStatus(StatusReserva.AGUARDANDO_ENTREGA);
            logger.info("Upgrade realizado novo veículo : " + novoVeiculo.getModelo().getModelo());

            reservaRepository.save(reserva);
        }
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        atualizaVeiculosReservados(LocalDate.now().plusDays(1));
    }

}

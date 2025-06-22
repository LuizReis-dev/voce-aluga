package com.cefet.vocealuga.services;

import com.cefet.vocealuga.models.Grupo;
import com.cefet.vocealuga.repositories.GrupoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {
    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }

    public Grupo findById(int id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupo no encontrado"));
    }

    @Transactional
    public void salvar(Grupo grupo) {
        //GARANTIR Q SÓ VAI SER ALTERADOO PREÇO POR DIA
        Grupo grupoBD = findById(grupo.getId());
        grupoBD.setPrecoPorDia(grupo.getPrecoPorDia());
        grupoRepository.save(grupoBD);
    }
}

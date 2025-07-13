package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.filial.FilialDTO;
import com.cefet.vocealuga.models.*;
import com.cefet.vocealuga.repositories.EmpresaRepository;
import com.cefet.vocealuga.repositories.FilialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilialService {
	private final UsuarioService usuarioService;
	private final FilialRepository filialRepository;
	private final EmpresaRepository empresaRepository;

	public FilialService(UsuarioService usuarioService, FilialRepository filialRepository, EmpresaRepository empresaRepository) {
		this.usuarioService = usuarioService;
		this.filialRepository = filialRepository;
        this.empresaRepository = empresaRepository;
    }

	public List<FilialDTO> buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(Integer modeloId) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		return filialRepository.buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(modeloId,
				usuarioLogado.getOperador().getFilial());

	}

	public List<FilialDTO> buscarFiliaisComVeiculoDisponivelDoModelo(Integer modeloId) {
		return filialRepository.buscarFiliaisComVeiculoDisponivel(modeloId);
	}

	public List<Filial> findAll() {
		return filialRepository.findAll();
	}

	public Filial findById(Integer id) {
		return filialRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Filial não encontrada!"));
	}

	@Transactional
	public Filial salvar(Filial filial) {
		Empresa empresa = empresaRepository.findById(1)
				.orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
		filial.setEmpresa(empresa);
		return filialRepository.save(filial);
	}

}

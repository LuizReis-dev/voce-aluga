package com.cefet.vocealuga.services;

import com.cefet.vocealuga.dtos.filial.FilialDTO;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.repositories.FilialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilialService {
	private final UsuarioService usuarioService;
	private final FilialRepository filialRepository;

	public FilialService(UsuarioService usuarioService, FilialRepository filialRepository) {
		this.usuarioService = usuarioService;
		this.filialRepository = filialRepository;
	}

	public List<FilialDTO> buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(Integer modeloId) {
		Usuario usuarioLogado = usuarioService.usuarioLogado();
		return filialRepository.buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(modeloId,
				usuarioLogado.getOperador().getFilial());

	}

	public List<FilialDTO> buscarFiliaisComVeiculoDisponivelDoModelo(Integer modeloId) {
		return filialRepository.buscarFiliaisComVeiculoDisponivel(modeloId);
	}
}

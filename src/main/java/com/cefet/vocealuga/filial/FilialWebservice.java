package com.cefet.vocealuga.filial;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FilialWebservice {
    private final FilialService filialService;
    public FilialWebservice(FilialService filialService) {
        this.filialService = filialService;
    }

    @GetMapping("/admin/api/filial-possui-veiculo/{modeloId}")
    @ResponseBody
    public List<FilialDTO> filialPossuiVeiculo(@PathVariable int modeloId) {
        return filialService.buscarFiliaisComVeiculoDisponivelDoModeloExcetoFilial(modeloId);
    }
}

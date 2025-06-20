package com.cefet.vocealuga.controllers;

import com.cefet.vocealuga.dtos.veiculos.CompraVeiculoDTO;
import com.cefet.vocealuga.dtos.veiculos.SolicitacaoTransferenciaDTO;
import com.cefet.vocealuga.dtos.veiculos.VendaVeiculoDTO;
import com.cefet.vocealuga.models.GerenciamentoTransacaoVeiculo;
import com.cefet.vocealuga.models.ModeloVeiculo;
import com.cefet.vocealuga.models.Usuario;
import com.cefet.vocealuga.models.Veiculo;
import com.cefet.vocealuga.services.ModeloVeiculoService;
import com.cefet.vocealuga.services.UsuarioService;
import com.cefet.vocealuga.services.VeiculoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VeiculoController {

    private final UsuarioService usuarioService;
    private final ModeloVeiculoService modeloVeiculoService;
    private final VeiculoService veiculoService;

    public VeiculoController(UsuarioService usuarioService, ModeloVeiculoService modeloVeiculoService, VeiculoService veiculoService) {
        this.usuarioService = usuarioService;
        this.modeloVeiculoService = modeloVeiculoService;
        this.veiculoService = veiculoService;
    }

    @GetMapping("/admin/veiculos")
    public String listarVeiculos(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoService.findAll();
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/listagem");

        return "/admin/layout";
    }

    @GetMapping("/admin/veiculos/compra")
    public String compra(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = this.modeloVeiculoService.findAll();
        model.addAttribute("modelos", modelos);
        model.addAttribute("compraVeiculoDTO", new CompraVeiculoDTO());
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/compra");

        return "/admin/layout";
    }

    @GetMapping("/admin/veiculos/{id}/modelo")
    public String listarPorModelo(Model model, @PathVariable Integer id) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<Veiculo> veiculos = this.veiculoService.findAllByModelo(id);
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/listagem");

        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/compra")
    public String registrarCompra(@ModelAttribute @Valid CompraVeiculoDTO compraVeiculoDTO, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o veículo: verifique os dados informados.");
            redirectAttributes.addFlashAttribute("compraVeiculoDTO", compraVeiculoDTO);

            List<String> mensagensErro = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            redirectAttributes.addFlashAttribute("erros", mensagensErro);

            return "redirect:/admin/veiculos/compra";
        }

        try {
            veiculoService.compra(compraVeiculoDTO);
            redirectAttributes.addFlashAttribute("success", "Veículo salvo com sucesso!");
            return "redirect:/admin/veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar o veículo: " + e.getMessage());
            return "redirect:/admin/veiculos/compra";
        }
    }

    @GetMapping("/admin/veiculos/venda")
    public String venda(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = this.modeloVeiculoService.findAll();
        model.addAttribute("modelos", modelos);
        model.addAttribute("vendaVeiculoDTO", new VendaVeiculoDTO());
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/venda");

        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/venda")
    public String registrarVenda(@ModelAttribute @Valid VendaVeiculoDTO vendaVeiculoDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao vender o veículo: verifique os dados informados.");
            redirectAttributes.addFlashAttribute("vendaVeiculoDTO", vendaVeiculoDTO);

            List<String> mensagensErro = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            redirectAttributes.addFlashAttribute("erros", mensagensErro);

            return "redirect:/admin/veiculos/venda";
        }

        try {
            veiculoService.venda(vendaVeiculoDTO);
            redirectAttributes.addFlashAttribute("success", "Veículo vendido com sucesso!");
            return "redirect:/admin/veiculos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao vender o veículo: " + e.getMessage());
            return "redirect:/admin/veiculos/venda";
        }
    }

    @GetMapping("/admin/veiculos/historico-transacoes")
    public String historicoTransacoes(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<GerenciamentoTransacaoVeiculo> transacoes = veiculoService.historicoTransacoes();

        model.addAttribute("transacoes", transacoes);
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/historico-transacoes");

        return "/admin/layout";
    }

    @GetMapping("/admin/veiculos/{id}/detalhes")
    public String detalhes(Model model, @PathVariable Integer id) {
        Veiculo veiculo = veiculoService.findById(id);
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<GerenciamentoTransacaoVeiculo> transacoes = veiculoService.historicoTransacoesPorVeiculo(veiculo);

        model.addAttribute("veiculo", veiculo);
        model.addAttribute("transacoes", transacoes);

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("conteudo", "/admin/veiculos/detalhes");

        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/{id}/solicitar-manutencao")
    public String solicitarManutencao(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        try {
            veiculoService.solicitarManutencao(id);
            redirectAttributes.addFlashAttribute("success", "Veículo colocado em manutenção!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao colocar veículo em manutenção: " + e.getMessage());
        }
        return "redirect:" + (referer != null ? referer : "/admin/veiculos");
    }

    @PostMapping("/admin/veiculos/{id}/finalizar-manutencao")
    public String finalizarManutencao(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        try {
            veiculoService.finalizarManutencao(id);
            redirectAttributes.addFlashAttribute("success", "Manutenção finalizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao finalizar manutenção: " + e.getMessage());
        }
        return "redirect:" + (referer != null ? referer : "/admin/veiculos");
    }

    @GetMapping("/admin/veiculos/solicitar-transferencia")
    public String solicitarTransferencia(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<ModeloVeiculo> modelos = modeloVeiculoService.findAll();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("modelos", modelos);

        model.addAttribute("conteudo", "/admin/veiculos/solicitar-transferencia");
        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/solicitar-transferencia")
    public String registrarSolicitacaoTransferencia(@Valid SolicitacaoTransferenciaDTO solicitacaoTransferenciaDTO, RedirectAttributes redirectAttributes) {
        try {
            veiculoService.solicitarTransferencia(solicitacaoTransferenciaDTO);
            redirectAttributes.addFlashAttribute("success", "Solicitação realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao solicitar transferência: " + e.getMessage());
        }
        return "redirect:/admin/veiculos/solicitar-transferencia";
    }

    @GetMapping("/admin/veiculos/solicitacoes-transferencia")
    public String solicitacoesTransferencia(Model model) {
        Usuario usuarioLogado = usuarioService.usuarioLogado();
        List<GerenciamentoTransacaoVeiculo> solicitacoes = veiculoService.listarSolicitacoesTransferencia();
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("conteudo", "/admin/veiculos/solicitacoes-transferencia");
        return "/admin/layout";
    }

    @PostMapping("/admin/veiculos/{id}/aprovar-solicitacao-transferencia")
    public String aprovarSolicitacaoTransferencia(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            veiculoService.aprovarSolicitacaoTransferencia(id);
            redirectAttributes.addFlashAttribute("success", "Transferência realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao confirmar transferência: " + e.getMessage());
        }
        return "redirect:/admin/veiculos/solicitacoes-transferencia";

    }
}


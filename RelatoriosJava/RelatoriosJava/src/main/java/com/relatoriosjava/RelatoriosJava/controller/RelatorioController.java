package com.relatoriosjava.RelatoriosJava.controller;

import com.relatoriosjava.RelatoriosJava.entity.*;
import com.relatoriosjava.RelatoriosJava.repository.*;
import com.relatoriosjava.RelatoriosJava.service.RelatorioDinamicoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class RelatorioController {
    private final RelatorioRepository relatorioRepository;
    private final CampoRelatorioRepository campoRepository;
    private final ColunaRelatorioRepository colunaRepository;
    private final RelatorioDinamicoService relatorioService;

    public RelatorioController(RelatorioRepository relatorioRepository, CampoRelatorioRepository campoRepository,
                               ColunaRelatorioRepository colunaRepository, RelatorioDinamicoService relatorioService) {
        this.relatorioRepository = relatorioRepository;
        this.campoRepository = campoRepository;
        this.colunaRepository = colunaRepository;
        this.relatorioService = relatorioService;
    }

    @GetMapping("/")
    public String inicio() {
        return relatorioRepository.findAll().stream().findFirst()
                .map(r -> "redirect:/relatorios/" + r.getId()).orElse("relatorio");
    }

    @GetMapping("/relatorios/{id}")
    public String abrir(@PathVariable Long id, Model model) {
        carregarConfiguracao(id, model);
        return "relatorio";
    }

    @PostMapping("/relatorios/{id}/gerar")
    public String gerar(@PathVariable Long id, @RequestParam Map<String,String> filtros, Model model) {
        carregarConfiguracao(id, model);
        model.addAttribute("resultados", relatorioService.gerar(id, filtros));
        model.addAttribute("filtrosRecebidos", filtros);
        return "relatorio";
    }

    @GetMapping("/relatorios/{id}/download")
    public void download(@PathVariable Long id, @RequestParam Map<String,String> filtros,
                         HttpServletResponse response) throws IOException {
        Relatorio relatorio = relatorioRepository.findById(id).orElseThrow();
        List<ColunaRelatorio> colunas = colunaRepository.findByRelatorioIdAndVisivelTrueOrderByOrdemAsc(id);
        List<Map<String,Object>> resultados = relatorioService.gerar(id, filtros);
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment; filename=\"relatorio-" + id + ".csv\"");
        StringBuilder csv = new StringBuilder("\uFEFF");
        csv.append(colunas.stream().map(c -> csv(c.getLabel())).collect(java.util.stream.Collectors.joining(";"))).append('\n');
        for (Map<String,Object> linha : resultados) {
            csv.append(colunas.stream().map(c -> csv(Objects.toString(linha.get(c.getCampo()), "")))
                    .collect(java.util.stream.Collectors.joining(";"))).append('\n');
        }
        response.getWriter().write(csv.toString());
    }

    private void carregarConfiguracao(Long id, Model model) {
        Relatorio relatorio = relatorioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado: " + id));
        model.addAttribute("relatorio", relatorio);
        model.addAttribute("campos", campoRepository.findByRelatorioIdOrderByOrdemAsc(id));
        model.addAttribute("colunas", colunaRepository.findByRelatorioIdAndVisivelTrueOrderByOrdemAsc(id));
    }

    private static String csv(String valor) { return "\"" + valor.replace("\"", "\"\"") + "\""; }
}

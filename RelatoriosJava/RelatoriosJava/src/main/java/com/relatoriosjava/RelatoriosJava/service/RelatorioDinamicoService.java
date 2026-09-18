package com.relatoriosjava.RelatoriosJava.service;

import com.relatoriosjava.RelatoriosJava.entity.*;
import com.relatoriosjava.RelatoriosJava.repository.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RelatorioDinamicoService {
    private static final Pattern IDENTIFICADOR = Pattern.compile("^[A-Za-z0-9_]+$");
    private final NamedParameterJdbcTemplate jdbc;
    private final RelatorioRepository relatorioRepository;
    private final CampoRelatorioRepository campoRepository;
    private final ColunaRelatorioRepository colunaRepository;

    public RelatorioDinamicoService(NamedParameterJdbcTemplate jdbc, RelatorioRepository relatorioRepository,
                                    CampoRelatorioRepository campoRepository, ColunaRelatorioRepository colunaRepository) {
        this.jdbc = jdbc;
        this.relatorioRepository = relatorioRepository;
        this.campoRepository = campoRepository;
        this.colunaRepository = colunaRepository;
    }

    public List<Map<String,Object>> gerar(Long relatorioId, Map<String,String> filtros) {
        Relatorio relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new IllegalArgumentException("Relatório não encontrado: " + relatorioId));
        List<ColunaRelatorio> colunas = colunaRepository.findByRelatorioIdAndVisivelTrueOrderByOrdemAsc(relatorioId);
        List<CampoRelatorio> campos = campoRepository.findByRelatorioIdOrderByOrdemAsc(relatorioId);
        if (colunas.isEmpty()) return List.of();

        String view = identificador(relatorio.getViewNome(), "view_nome do relatorio id=" + relatorio.getId());
        String select = colunas.stream()
                .map(c -> "`" + identificador(
                        c.getCampo(),
                        "coluna_relatorio.campo id=" + c.getId()
                ) + "`")
                .collect(Collectors.joining(", "));
        StringBuilder sql = new StringBuilder("SELECT ").append(select).append(" FROM `").append(view).append("` WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        int i = 0;
        for (CampoRelatorio campo : campos) {
            String bruto = filtros.get(campo.getNome());
            if (bruto == null || bruto.isBlank()) continue;
            String coluna = identificador(
                    campo.getCampoBanco(),
                    "campo_relatorio.campo_banco id=" + campo.getId()
                            + ", nome=" + campo.getNome()
            );
            String param = "p" + i++;
            Object valor = converterValor(campo, bruto);
            switch (campo.getOperador()) {
                case LIKE -> { sql.append(" AND LOWER(`").append(coluna).append("`) LIKE :").append(param); params.addValue(param, "%" + bruto.toLowerCase() + "%"); }
                case EQUALS -> { sql.append(" AND `").append(coluna).append("` = :").append(param); params.addValue(param, valor); }
                case GTE -> { sql.append(" AND `").append(coluna).append("` >= :").append(param); params.addValue(param, valor); }
                case LTE -> { sql.append(" AND `").append(coluna).append("` <= :").append(param); params.addValue(param, valor); }
                case GT -> { sql.append(" AND `").append(coluna).append("` > :").append(param); params.addValue(param, valor); }
                case LT -> { sql.append(" AND `").append(coluna).append("` < :").append(param); params.addValue(param, valor); }
            }
        }
        return jdbc.queryForList(sql.toString(), params);
    }

    private Object converterValor(CampoRelatorio campo, String valor) {
        return switch (campo.getTipo()) {
            case DATE -> LocalDate.parse(valor);
            case NUMBER -> new java.math.BigDecimal(valor);
            case CHECKBOX -> Boolean.parseBoolean(valor);
            default -> valor;
        };
    }

    private String identificador(String valor, String contexto) {
        if (valor == null || valor.isBlank() || !IDENTIFICADOR.matcher(valor).matches()) {
            throw new IllegalArgumentException(
                    contexto + " inválido. Valor recebido: '" + valor + "'"
            );
        }

        return valor;
    }
}

package com.relatoriosjava.RelatoriosJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name="campo_relatorio")
public class CampoRelatorio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="relatorio_id", nullable=false) private Relatorio relatorio;
    @Column(nullable=false) private String nome; // nome do parâmetro HTML, ex: dataInicio
    @Column(nullable=false) private String label;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TipoCampo tipo;
    @Column(nullable=false) private boolean obrigatorio;
    @Column(nullable=false) private Integer ordem;
    private String placeholder;
    @Column(name="campo_banco", nullable=false) private String campoBanco; // coluna da view
    @Enumerated(EnumType.STRING) @Column(nullable=false) private OperadorFiltro operador = OperadorFiltro.EQUALS;

    @OneToMany(mappedBy="campo", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("ordem ASC")
    private List<Opcaocampo> opcao = new ArrayList<>();
}

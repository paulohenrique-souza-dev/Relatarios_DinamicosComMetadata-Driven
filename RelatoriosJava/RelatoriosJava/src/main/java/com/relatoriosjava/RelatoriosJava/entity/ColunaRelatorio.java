package com.relatoriosjava.RelatoriosJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="coluna_relatorio")
public class ColunaRelatorio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="relatorio_id", nullable=false) private Relatorio relatorio;
    @Column(nullable=false) private String label;
    @Column(nullable=false) private String campo; // alias/coluna da view
    @Column(nullable=false) private Integer ordem;
    @Column(nullable=false) private boolean visivel = true;
}

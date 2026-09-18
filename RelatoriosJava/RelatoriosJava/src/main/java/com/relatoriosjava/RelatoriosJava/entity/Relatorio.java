package com.relatoriosjava.RelatoriosJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "relatorio")
public class Relatorio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nome;
    private String descricao;
    @Column(name = "view_nome", nullable = false) private String viewNome;
}

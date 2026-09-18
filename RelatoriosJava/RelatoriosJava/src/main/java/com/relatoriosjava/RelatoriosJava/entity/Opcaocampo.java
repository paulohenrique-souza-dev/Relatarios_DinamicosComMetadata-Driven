package com.relatoriosjava.RelatoriosJava.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="opcao_campo")
public class Opcaocampo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="campo_id", nullable=false)
    private CampoRelatorio campo;
    @Column(nullable=false) private String valor;
    @Column(nullable=false) private String label;
    @Column(nullable=false) private Integer ordem;


}

-- Estrutura mínima esperada pelo motor dinâmico.
-- Ajuste somente o que ainda não existir no seu banco.

-- relatorio precisa identificar a VIEW fonte
-- ALTER TABLE relatorio ADD COLUMN view_nome VARCHAR(120) NOT NULL;

-- filtros pertencem a um relatório e dizem qual coluna/operador usar
-- ALTER TABLE campo_relatorio ADD COLUMN relatorio_id BIGINT NOT NULL;
-- ALTER TABLE campo_relatorio ADD COLUMN campo_banco VARCHAR(120) NOT NULL;
-- ALTER TABLE campo_relatorio ADD COLUMN operador VARCHAR(20) NOT NULL DEFAULT 'EQUALS';
-- ALTER TABLE campo_relatorio ADD CONSTRAINT fk_campo_relatorio_relatorio FOREIGN KEY (relatorio_id) REFERENCES relatorio(id);
-- Se hoje "nome" é UNIQUE globalmente, remova esse unique e, se quiser, use UNIQUE(relatorio_id,nome).

-- Exemplo de relatório baseado em view:
-- INSERT INTO relatorio(nome, descricao, view_nome) VALUES ('Vendas','Relatório de vendas','vw_relatorio_vendas');
-- SET @rid = LAST_INSERT_ID();
-- INSERT INTO coluna_relatorio(relatorio_id,label,campo,ordem,visivel) VALUES
-- (@rid,'ID','vendaId',1,1),(@rid,'Cliente','clienteNome',2,1),(@rid,'Valor','valor',3,1),(@rid,'Status','status',4,1);
-- INSERT INTO campo_relatorio(relatorio_id,nome,label,tipo,obrigatorio,ordem,placeholder,campo_banco,operador) VALUES
-- (@rid,'cliente','Cliente','TEXT',0,1,'Nome do cliente','clienteNome','LIKE'),
-- (@rid,'status','Status','SELECT',0,2,NULL,'status','EQUALS'),
-- (@rid,'dataInicio','Data inicial','DATE',0,3,NULL,'dataVenda','GTE'),
-- (@rid,'dataFim','Data final','DATE',0,4,NULL,'dataVenda','LTE');

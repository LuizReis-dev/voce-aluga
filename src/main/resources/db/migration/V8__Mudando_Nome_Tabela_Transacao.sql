DROP TABLE gerenciamentotransacoes;

CREATE TABLE gerenciamento_transacao_veiculo (
     id INT AUTO_INCREMENT PRIMARY KEY,
     tipo_transacao VARCHAR(255) NOT NULL,
     veiculo_id INT NOT NULL,
     origem_filial_id INT,
     destino_filial_id INT,
     data_transacao DATE NOT NULL,
     data_fim_transacao DATE,
     valor DOUBLE,
     operador_id INT NOT NULL,

     CONSTRAINT fk_ger_trans_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculo(id),
     CONSTRAINT fk_ger_trans_filial_origem FOREIGN KEY (origem_filial_id) REFERENCES filial(id),
     CONSTRAINT fk_ger_trans_filial_destino FOREIGN KEY (destino_filial_id) REFERENCES filial(id),
     CONSTRAINT fk_ger_trans_operador FOREIGN KEY (operador_id) REFERENCES operador(id)
);
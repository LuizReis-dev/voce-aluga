ALTER TABLE reserva
DROP FOREIGN KEY reserva_ibfk_1;

ALTER TABLE reserva
DROP COLUMN usuario_id;

-- 2. Adicionar a nova coluna para cliente (chave estrangeira para cliente)
ALTER TABLE reserva
    ADD COLUMN cliente_id INT,
ADD CONSTRAINT fk_reserva_cliente
  FOREIGN KEY (cliente_id) REFERENCES cliente(id);

-- 3. Adicionar a nova coluna para operador (chave estrangeira para operador)
ALTER TABLE reserva
    ADD COLUMN operador_id INT,
ADD CONSTRAINT fk_reserva_operador
  FOREIGN KEY (operador_id) REFERENCES operador(id);
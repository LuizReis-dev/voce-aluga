ALTER TABLE reserva
    ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'CRIADA';

ALTER TABLE reserva
    ADD COLUMN veiculo_original_id INT,
ADD CONSTRAINT fk_reserva_veiculo_original
FOREIGN KEY (veiculo_original_id) REFERENCES veiculo(id);
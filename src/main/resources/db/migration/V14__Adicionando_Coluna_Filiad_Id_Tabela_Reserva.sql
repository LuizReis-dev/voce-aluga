ALTER TABLE reserva
	ADD COLUMN filial_id INT, 
ADD CONSTRAINT fk_filial_id
	FOREIGN KEY (filial_id) REFERENCES filial(id);
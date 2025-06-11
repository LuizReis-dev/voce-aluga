ALTER TABLE veiculo DROP COLUMN modelo;
ALTER TABLE veiculo DROP COLUMN marca;
ALTER TABLE veiculo DROP COLUMN ano;

CREATE TABLE `modelo_veiculo` (
    `id` integer PRIMARY KEY,
    `modelo` varchar(255),
    `marca` varchar(255),
    `ano` year
);

ALTER TABLE `veiculo` ADD COLUMN modelo_id INTEGER AFTER `id`;
ALTER TABLE `veiculo` ADD FOREIGN KEY (`modelo_id`) REFERENCES `modelo_veiculo` (`id`);


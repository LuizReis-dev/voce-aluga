-- 1. Cria a tabela de grupos com preço por dia
CREATE TABLE `grupo` (
     `id` INT PRIMARY KEY AUTO_INCREMENT,
     `nome` VARCHAR(60) NOT NULL,
     `preco_por_dia` DECIMAL(10, 2) NOT NULL
);

-- 2. Insere os grupos com seus respectivos preços
INSERT INTO `grupo` (`id`, `nome`, `preco_por_dia`)
VALUES
    (1, 'GRUPO A', 150.00),
    (2, 'GRUPO B', 250.00),
    (3, 'GRUPO C', 350.00);

-- 3. Adiciona a coluna `grupo_id` à tabela `modelo_veiculo`
ALTER TABLE `modelo_veiculo` ADD COLUMN `grupo_id` INT AFTER `id`;

-- 4. Atribui o grupo padrão aos modelos existentes
UPDATE `modelo_veiculo` SET `grupo_id` = 2 WHERE id > 0;

-- 5. Torna a coluna obrigatória (não nula)
ALTER TABLE `modelo_veiculo` MODIFY `grupo_id` INT NOT NULL;

-- 6. Adiciona a foreign key
ALTER TABLE `modelo_veiculo`
    ADD CONSTRAINT `fk_modelo_grupo`
        FOREIGN KEY (`grupo_id`) REFERENCES `grupo`(`id`);

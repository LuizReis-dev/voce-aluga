INSERT INTO endereco (id, logradouro, numero, complemento, bairro, cidade, estado, cep) VALUES
    (2, 'R. Osório Ferreira', '01', '', 'Realengo', 'Rio de Janeiro', 'RJ', '21730-410');

INSERT INTO filial (id, nome, telefone, email, endereco_id, empresa_id) VALUES
    (2, 'Filial Secundária', '(31) 98888-0000', 'secundaria@alugamais.com', 2, 1);

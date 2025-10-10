INSERT INTO tarefa (titulo, descricao, responsavel, prioridade, status, data_criacao, data_limite)
VALUES 
 ('Preparar slides', 'Aula sobre MVC e Thymeleaf', 'admin', 'ALTA', 'PENDENTE', CURRENT_DATE, '2026-08-20'),
 ('Estudar Security', 'Ler docs Spring Security', 'admin', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-09-05'),
 ('Exercício JPA', 'CRUD com validação', 'admin', 'BAIXA', 'PENDENTE', CURRENT_DATE, '2026-08-25'),
 ('Refatorar services', 'Separar responsabilidades', 'admin', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-08-28'),
 ('Revisar modelos', 'Ajustar entidade Categoria', 'admin', 'ALTA', 'PENDENTE', CURRENT_DATE, '2026-08-30'),
 ('Feedback cliente', 'Coletar feedback do protótipo', 'admin', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-09-01');


INSERT INTO categoria (nome) VALUES ('Trabalho');
INSERT INTO categoria (nome) VALUES ('Estudos');
INSERT INTO categoria (nome) VALUES ('Pessoal');
INSERT INTO categoria (nome) VALUES ('Saude');
INSERT INTO categoria (nome) VALUES ('Urgente');

INSERT INTO users (username, password, role) VALUES ('admin', '123456', 'ADMIN');


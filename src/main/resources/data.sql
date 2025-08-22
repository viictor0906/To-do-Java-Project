INSERT INTO task (title, desc, responsible, creation_date, limit_date, status, priority)
VALUES
('Criar protótipo', 'Desenvolver protótipo inicial da aplicação', 'Ana Maria', CURRENT_DATE, '2025-09-05', 'PENDING', 'HIGH'),
('Revisar requisitos', 'Analisar e validar requisitos com o cliente', 'João Pedro', CURRENT_DATE, '2025-08-25', 'DONE', 'MID'),
('Preparar documentação', 'Documentar as funcionalidades básicas do sistema', 'Carla Silva', CURRENT_DATE, '2025-08-28', 'PENDING', 'LOW'),
('Testar funcionalidades', 'Executar testes de unidade e integração', 'Bruno Costa', CURRENT_DATE, '2025-08-30', 'PENDING', 'MID'),
('Deploy em produção', 'Publicar a aplicação no ambiente de produção', 'Lucas Martins', CURRENT_DATE, '2025-09-01', 'DONE', 'HIGH');

INSERT INTO T_BIOMELAB_USUARIO (nm_usuario, email, senha, dt_nascimento) VALUES ('João Silva', 'joao@email.com', '123456', DATE '1990-05-15');
INSERT INTO T_BIOMELAB_USUARIO (nm_usuario, email, senha, dt_nascimento) VALUES ('Maria Souza', 'maria@email.com', '123456', DATE '1995-08-22');
INSERT INTO T_BIOMELAB_USUARIO (nm_usuario, email, senha, dt_nascimento) VALUES ('Pedro Costa', 'pedro@email.com', '123456', DATE '1988-03-10');

INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Lab 1', 'R', 'ATIVO', 1);
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Lab 2', 'R', 'INATIVO', 1);
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Estufa Experimental', 'R', 'INATIVO', 1);
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Ambiente Teste Alpha', 'R', 'INATIVO', 2);
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Lab Beta', 'R', 'INATIVO', 2);

INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Deserto do Saara', 'P', NULL, NULL);
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Fundo do Oceano Pacífico', 'P', NULL, NULL);
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario) VALUES ('Floresta Amazônica', 'P', NULL, NULL);

INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais) VALUES ('Terra', 'África', 'Argélia');
INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais) VALUES ('Terra', 'Oceania', NULL);
INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais) VALUES ('Terra', 'América do Sul', 'Brasil');
INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais) VALUES ('Marte', NULL, NULL);
INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais) VALUES ('Terra', 'Europa', 'Alemanha');


INSERT INTO AMBIENTE_LOCALIDADE (fk_ambiente, fk_localidade) VALUES (6, 1); 
INSERT INTO AMBIENTE_LOCALIDADE (fk_ambiente, fk_localidade) VALUES (7, 2); 
INSERT INTO AMBIENTE_LOCALIDADE (fk_ambiente, fk_localidade) VALUES (8, 3); 


INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (38.5, 15.0, 90000.0, 9.8, 1013.0, 1); 
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (22.0, 60.0, 50000.0, 9.8, 1013.0, 2);  
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (30.0, 80.0, 70000.0, 9.8, 1013.0, 3);  
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (20.0, 50.0, 40000.0, 9.8, 1013.0, 4);  
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (25.0, 55.0, 45000.0, 9.8, 1013.0, 5); 
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (45.0, 5.0, 120000.0, 9.8, 1013.0, 6); 
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (4.0, 95.0, 1000.0, 9.8, 500.0, 7);    
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente) VALUES (27.0, 90.0, 80000.0, 9.8, 1013.0, 8); 

INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente) VALUES ('Estudo de Lab 1', NULL, 1);
INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente) VALUES ('Estudo de Lab 2', NULL, 2);
INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente) VALUES ('Estudo de Estufa Experimental', NULL, 3);
INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente) VALUES ('Estudo de Ambiente Teste Alpha', NULL, 4);
INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente) VALUES ('Estudo de Lab Beta', NULL, 5);


INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo) VALUES ('1° Teste - Lab 1', DATE '2026-01-01', NULL, NULL, NULL, 1);
INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo) VALUES ('1° Teste - Lab 2', DATE '2026-01-01', NULL, NULL, NULL, 2);
INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo) VALUES ('1° Teste - Estufa Experimental', DATE '2026-01-01', NULL, NULL, NULL, 3);
INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo) VALUES ('1° Teste - Ambiente Teste Alpha', DATE '2026-01-01', NULL, NULL, NULL, 4);
INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo) VALUES ('1° Teste - Lab Beta', DATE '2026-01-01', NULL, NULL, NULL, 5);

INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste) VALUES (38.5, 15.0, 90000.0, 9.8, 1013.0, 1);
INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste) VALUES (22.0, 60.0, 50000.0, 9.8, 1013.0, 2);
INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste) VALUES (30.0, 80.0, 70000.0, 9.8, 1013.0, 3);
INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste) VALUES (20.0, 50.0, 40000.0, 9.8, 1013.0, 4);
INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste) VALUES (25.0, 55.0, 45000.0, 9.8, 1013.0, 5);

COMMIT;
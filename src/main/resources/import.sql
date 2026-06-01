-- ============================================================
-- BiomeLab - Dados iniciais (import.sql)
-- Ordem de insercao respeita as Foreign Keys
-- Sintaxe Oracle - colocar em src/main/resources/
-- ============================================================

-- ============ 1. USUARIOS ============
INSERT INTO T_BIOMELAB_USUARIO (nm_usuario, dt_nascimento, email, senha)
VALUES ('Liana Fujisima', TO_DATE('2000-01-29','YYYY-MM-DD'), 'liana@biomelab.com', 'senha123');

INSERT INTO T_BIOMELAB_USUARIO (nm_usuario, dt_nascimento, email, senha)
VALUES ('Eduardo Locaspi', TO_DATE('2000-06-15','YYYY-MM-DD'), 'eduardo@biomelab.com', 'senha123');

INSERT INTO T_BIOMELAB_USUARIO (nm_usuario, dt_nascimento, email, senha)
VALUES ('Victor Lopes', TO_DATE('2001-03-10','YYYY-MM-DD'), 'victor@biomelab.com', 'senha123');

-- ============ 2. LOCALIDADES ============
INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais)
VALUES ('Terra', 'America do Sul', 'Brasil');

INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais)
VALUES ('Terra', 'America do Norte', 'Canada');

INSERT INTO T_BIOMELAB_LOCALIDADE (nm_planeta, continente, pais)
VALUES ('Marte', NULL, NULL);

-- ============ 3. AMBIENTES (FK: usuario) ============
INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario)
VALUES ('Amazonia Simulada', 'P', 'ATIVO', 1);

INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario)
VALUES ('Deserto do Saara', 'P', 'ATIVO', 1);

INSERT INTO T_BIOMELAB_AMBIENTE (nm_ambiente, st_visibilidade, st_ativo, fk_usuario)
VALUES ('Estacao Espacial', 'R', 'ATIVO', 2);

-- ============ 4. AMBIENTE_LOCALIDADE (N:N - FK: ambiente + localidade) ============
INSERT INTO AMBIENTE_LOCALIDADE (fk_ambiente, fk_localidade)
VALUES (1, 1);

INSERT INTO AMBIENTE_LOCALIDADE (fk_ambiente, fk_localidade)
VALUES (2, 1);

INSERT INTO AMBIENTE_LOCALIDADE (fk_ambiente, fk_localidade)
VALUES (3, 3);

-- ============ 5. ESTUDOS (1:1 - FK: ambiente) ============
INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente)
VALUES ('Crescimento Tropical', 'Estudo de plantas em clima tropical umido', 1);

INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente)
VALUES ('Resistencia Arida', 'Estudo de cactos em ambiente desertico', 2);

INSERT INTO T_BIOMELAB_ESTUDO (nm_estudo, des_estudo, fk_ambiente)
VALUES ('Cultivo Microgravidade', 'Estudo de plantas em gravidade reduzida', 3);

-- ============ 6. CONJ_PROPS_ATUAL (1:1 - FK: ambiente) ============
INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente)
VALUES (28.50000, 85.00, 12000.00000, 9.80665, 1013.25, 1);

INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente)
VALUES (45.00000, 15.00, 25000.00000, 9.80665, 1013.25, 2);

INSERT INTO T_BIOMELAB_CONJ_PROPS_ATUAL (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_ambiente)
VALUES (21.00000, 50.00, 8000.00000, 0.00100, 0.00, 3);

-- ============ 7. TESTES (FK: estudo) ============
INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo)
VALUES ('Teste Germinacao A', TO_DATE('2026-01-10','YYYY-MM-DD'), TO_DATE('2026-02-10','YYYY-MM-DD'), 'Germinacao em alta umidade', 'Crescimento acelerado', 1);

INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo)
VALUES ('Teste Seca B', TO_DATE('2026-01-15','YYYY-MM-DD'), TO_DATE('2026-03-15','YYYY-MM-DD'), 'Resistencia a falta de agua', 'Sobrevivencia de 90%', 2);

INSERT INTO T_BIOMELAB_TESTE (nm_teste, dt_inicio_teste, dt_termino_teste, obs_gerais, conclusao, fk_estudo)
VALUES ('Teste Microgravidade C', TO_DATE('2026-02-01','YYYY-MM-DD'), NULL, 'Crescimento sem gravidade definida', NULL, 3);

-- ============ 8. CONJ_PROPS_SNAPSHOT (1:1 - FK: teste) ============
INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste)
VALUES (28.00000, 84.50, 11800.00000, 9.80665, 1013.00, 1);

INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste)
VALUES (44.20000, 16.00, 24500.00000, 9.80665, 1012.80, 2);

INSERT INTO T_BIOMELAB_CONJ_PROPS_SNAPSHOT (vl_temperatura, vl_umidade, vl_luminosidade, vl_gravidade, pressao_atmosferica, fk_teste)
VALUES (20.50000, 49.00, 7900.00000, 0.00100, 0.00, 3);

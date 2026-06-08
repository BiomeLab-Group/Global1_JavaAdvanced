# 🌱 BiomeLab API

API REST desenvolvida em Java com Spring Boot para gerenciamento de ambientes experimentais, estudos científicos e testes de condições ambientais.

---

# 📖 Sobre o Projeto

O BiomeLab é uma plataforma voltada para simulação e monitoramento de ambientes experimentais.

O sistema permite:

* Gerenciar ambientes públicos e privados;
* Monitorar propriedades ambientais;
* Realizar estudos científicos;
* Executar testes com diferentes condições ambientais;
* Registrar snapshots históricos das condições utilizadas em cada teste.

---

# 🚀 Tecnologias Utilizadas

## Backend

* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* Maven

## Banco de Dados

* Oracle Database

## Documentação

* Swagger / OpenAPI

## Deploy

* Azure

---

# 📂 Arquitetura

O projeto foi desenvolvido utilizando arquitetura em camadas:

* Controller – exposição dos endpoints REST
* Repository – acesso aos dados com Spring Data JPA
* DTO – transferência de dados entre API e cliente
* Mapper – conversão entre entidades e DTOs
* Model – entidades persistidas no banco de dados

Recursos complementares:

* Spring Security
* JWT Authentication
* Bean Validation
* Swagger/OpenAPI
* Oracle Database
---

# 🔗 Links do Projeto

## Repositório GitHub

[Repositório GitHub](https://github.com/BiomeLab-Group/Global1_JavaAdvanced)

## Deploy da API

[INSERIR LINK]

## Swagger / OpenAPI

http://IP_DA_VM:PORT/swagger-ui/index.html

## Vídeo de Apresentação

[INSERIR LINK]

---

# 📚 Principais Funcionalidades

## Usuários

* Cadastro de usuário
* Login com JWT
* Consulta de dados pessoais
* Atualização de perfil

## Ambientes

* Criação de ambientes privados
* Consulta de ambientes
* Edição de ambiente
* Ativação e desativação de ambiente
* Download de ambientes públicos

## Estudos

* Criação de estudos (Automático)
* Consulta de estudos
* Edição de estudos
* Remoção de estudos

## Testes

* Criação de testes (Automático)
* Consulta de testes
* Edição de testes
* Remoção de testes finalizados

## Condições Ambientais

* Alteração das condições do ambiente
* Registro automático de snapshots
* Histórico das condições utilizadas

---

# 🔐 Autenticação

A API utiliza autenticação JWT.

Após realizar login:

```http
POST /autenticacao/login
```

o sistema retorna um token JWT.

Esse token deve ser enviado nos endpoints protegidos:

```http
Authorization: Bearer SEU_TOKEN
```

---

# ▶️ Como Executar o Projeto

## 1. Clonar o repositório

```bash
git clone https://github.com/BiomeLab-Group/Global1_JavaAdvanced.git
```

## 2. Entrar na pasta

```bash
cd Global1_JavaAdvanced/BiomeLab/
```

## 3. Configurar o banco de dados

Editar o arquivo:

```properties
application.properties
```

Configurando:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL}
spring.datasource.username=RM561713
spring.datasource.password=290107
```

## 4. Executar o projeto

```bash
mvn spring-boot:run
```

---

# 📄 Documentação da API

A documentação Swagger pode ser acessada em:

```text
http://IP_DA_VM:PORT/swagger-ui/index.html
```

---

# 👥 Equipe

* Eduardo Batista Locaspi - RM561713
* Victor Alves Lopes - RM561833
* Liana Lyumi Morisita Fujisima - RM565698
* Leticia Santiago e Silva - RM565799

---

# 📝 Observações para Avaliação

* Projeto desenvolvido utilizando Spring Boot.
* Autenticação baseada em JWT.
* Persistência de dados utilizando Oracle Database.
* API documentada com Swagger/OpenAPI.
* Estrutura baseada em DTOs para comunicação com clientes Web e Mobile.

# DSCommerce

API REST completa de e-commerce desenvolvida com Java e Spring Boot, implementando autenticação e autorização com OAuth2 e JWT, controle de acesso por perfil de usuário, e modelagem de domínio com JPA/Hibernate.

---

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.14**
- **Spring Web MVC** — camada REST
- **Spring Data JPA / Hibernate** — ORM e consultas JPQL
- **Spring Security** — autenticação e autorização
- **OAuth2 Authorization Server** — servidor de autorização customizado com password grant
- **OAuth2 Resource Server** — proteção dos endpoints com JWT
- **Bean Validation** — validação de dados de entrada
- **H2 Database** — banco em memória para ambiente de teste
- **Maven** — gerenciamento de dependências

---

## Funcionalidades

- CRUD completo de produtos com busca paginada e filtro por nome
- Listagem de categorias
- Gerenciamento de pedidos com itens e pagamento
- Autenticação via OAuth2 com JWT (access token auto-contido)
- Controle de acesso por perfil: `ROLE_ADMIN` e `ROLE_CLIENT`
- Restrição de acesso: usuário só acessa seus próprios pedidos
- Tratamento centralizado de exceções com respostas padronizadas
- Perfis de projeto: `test` (H2), `dev`, `prod`
- Seeding automático do banco via `import.sql`

---

## Modelo de Domínio

```
User (ROLE_CLIENT / ROLE_ADMIN)
 └── Order (WAITING_PAYMENT | PAID | SHIPPED | DELIVERED | CANCELED)
      ├── OrderItem (Product, quantity, price)
      └── Payment

Product
 └── Category (many-to-many)
```

---

## Endpoints principais

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `POST` | `/oauth2/token` | Público | Obter token JWT |
| `GET` | `/products` | Público | Listar produtos (paginado + filtro por nome) |
| `GET` | `/products/{id}` | Público | Buscar produto por ID |
| `POST` | `/products` | ADMIN | Cadastrar produto |
| `PUT` | `/products/{id}` | ADMIN | Atualizar produto |
| `DELETE` | `/products/{id}` | ADMIN | Remover produto |
| `GET` | `/categories` | Público | Listar categorias |
| `GET` | `/orders/{id}` | ADMIN / dono | Buscar pedido por ID |
| `POST` | `/orders` | CLIENT | Criar pedido |
| `GET` | `/users/me` | ADMIN / CLIENT | Dados do usuário logado |

---

## Como rodar localmente

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Passos

```bash
# Clone o repositório
git clone https://github.com/HendersonSilva53/Dscommerce.git
cd Dscommerce/dscommerce

# Execute a aplicação (perfil test com H2)
./mvnw spring-boot:run
```

A aplicação sobe na porta `http://localhost:8080`.

O console do H2 estará disponível em `http://localhost:8080/h2-console` com as configurações:

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** *(vazio)*

---

## Autenticação

A API usa OAuth2 com password grant customizado. Para obter o token:

```http
POST /oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
&username=maria@gmail.com
&password=123456
&client_id=myclientid
&client_secret=myclientsecret
```

Use o `access_token` retornado no header das requisições protegidas:

```http
Authorization: Bearer {access_token}
```

### Usuários de teste (seed)

| Usuário | Email | Senha | Perfil |
|---------|-------|-------|--------|
| Maria Brown | maria@gmail.com | 123456 | CLIENT |
| Alex Green | alex@gmail.com | 123456 | ADMIN + CLIENT |

---

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `CLIENT_ID` | `myclientid` | ID do cliente OAuth2 |
| `CLIENT_SECRET` | `myclientsecret` | Secret do cliente OAuth2 |
| `JWT_DURATION` | `86400` | Duração do token em segundos (24h) |
| `CORS_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Origens permitidas |

---

## Collection Postman

A collection para testar todos os endpoints está disponível em:

```
postman/DSCommerce.postman_collection.json
```

Importe no Postman via **File → Import**. A collection já inclui variáveis de ambiente e script para extração automática do JWT após o login.

---

## Estrutura do projeto

```
src/
├── main/
│   ├── java/com/devsuperior/dscommerce/
│   │   ├── config/           # Configurações OAuth2, Security, CORS
│   │   │   └── customgrant/  # Custom password grant
│   │   ├── controllers/      # Camada REST
│   │   │   └── handlers/     # Tratamento de exceções
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── entities/         # Entidades JPA
│   │   ├── projection/       # Projeções Spring Data
│   │   ├── repositories/     # Interfaces Spring Data JPA
│   │   └── services/         # Regras de negócio
│   │       └── exceptions/   # Exceções customizadas
│   └── resources/
│       ├── application.properties
│       ├── application-test.properties
│       └── import.sql        # Seed do banco de dados
└── test/
```

---

## Autor

**Henderson Silva**
- LinkedIn: [linkedin.com/in/HendersonSilva53](https://linkedin.com/in/HendersonSilva53)

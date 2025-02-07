# soa-generator
Criação arquivos XSD, WSDL e WADL para implementar serviços SOA

# Gerador de Arquivos XSD, WSDL e WADL - SOA Suite 12c

## Visão Geral
Este projeto tem como objetivo gerar dinamicamente arquivos XSD, WSDL e WADL a partir de uma API desenvolvida com **Spring Boot 3**. A aplicação permite definir os campos de entrada e saída, além de objetos complexos, e gerar os arquivos `.xsd`, `.wsdl` e `.wadl` correspondentes, que podem ser baixados posteriormente.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3**
- **Jakarta XML Binding (JAXB)**
- **Maven**

## Configuração do Projeto
### **1. Clonar o Repositório**
```sh
$ git clone <URL_DO_REPOSITORIO>
$ cd nome-do-projeto
```

### **2. Construção e Execução**
Compile e execute o projeto localmente com:
```sh
$ mvn clean install
$ mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Uso da API
### **1. Gerar Arquivos XSD**
- **Endpoint:** `POST /api/xsd/generate`
- **Descrição:** Gera e salva os arquivos `.xsd` para o serviço especificado.
- **Requisição (JSON):**
```json
{
  "serviceName": "PedidoService",
  "inputFields": [
    { "name": "idPedido", "type": "int", "required": true },
    { "name": "cliente", "type": "Cliente", "required": true, "isObject": true }
  ],
  "outputFields": [
    { "name": "status", "type": "string", "required": true }
  ],
  "objects": [
    {
      "name": "Cliente",
      "fields": [
        { "name": "id", "type": "int", "required": true },
        { "name": "nome", "type": "string", "required": true }
      ]
    }
  ]
}
```
- **Resposta Esperada:**
```text
Arquivos gerados: PedidoServiceRequest.xsd e PedidoServiceResponse.xsd
```

### **2. Baixar Arquivo XSD**
- **Endpoint:** `GET /api/xsd/download/{fileName}`
- **Descrição:** Baixa um arquivo XSD gerado.
- **Exemplo de uso:**
```sh
http://localhost:8080/api/xsd/download/PedidoServiceRequest.xsd
```

### **3. Gerar Arquivo WSDL**
- **Endpoint:** `POST /api/wsdl/generate`
- **Descrição:** Gera e salva um arquivo `.wsdl` para o serviço especificado.
- **Requisição (JSON):**
```json
{
  "serviceName": "PedidoService",
  "inputFields": [
    { "name": "idPedido", "type": "int", "required": true },
    { "name": "cliente", "type": "Cliente", "required": true, "isObject": true }
  ],
  "outputFields": [
    { "name": "status", "type": "string", "required": true }
  ]
}
```
- **Resposta Esperada:**
```text
Arquivo WSDL gerado: PedidoService.wsdl
```

### **4. Baixar Arquivo WSDL**
- **Endpoint:** `GET /api/wsdl/download/{fileName}`
- **Descrição:** Baixa um arquivo WSDL gerado.
- **Exemplo de uso:**
```sh
http://localhost:8080/api/wsdl/download/PedidoService.wsdl
```

### **5. Gerar Arquivo WADL**
- **Endpoint:** `POST /wadl/generate`
- **Descrição:** Gera e salva um arquivo `.wadl` para o serviço especificado.
- **Requisição (JSON):**
```json
{
  "baseUri": "http://localhost:8080/api",
  "serviceName": "ProdutoService",
  "methods": [
    {
      "name": "GET",
      "path": "/produtos/{id}",
      "description": "Obtém detalhes de um produto pelo ID",
      "request": {
        "parameters": [
          { "name": "id", "type": "int", "required": true }
        ]
      },
      "response": {
        "fields": [
          { "name": "id", "type": "int" },
          { "name": "nome", "type": "string" },
          { "name": "preco", "type": "double" }
        ]
      }
    },
    {
      "name": "POST",
      "path": "/produtos",
      "description": "Cria um novo produto",
      "request": {
        "fields": [
          { "name": "nome", "type": "string", "required": true },
          { "name": "preco", "type": "double", "required": true }
        ]
      },
      "response": {
        "fields": [
          { "name": "id", "type": "int" },
          { "name": "mensagem", "type": "string" }
        ]
      }
    },
    {
      "name": "PUT",
      "path": "/produtos/{id}",
      "description": "Atualiza um produto existente",
      "request": {
        "parameters": [
          { "name": "id", "type": "int", "required": true }
        ],
        "fields": [
          { "name": "nome", "type": "string" },
          { "name": "preco", "type": "double" }
        ]
      },
      "response": {
        "fields": [
          { "name": "mensagem", "type": "string" }
        ]
      }
    },
    {
      "name": "DELETE",
      "path": "/produtos/{id}",
      "description": "Exclui um produto pelo ID",
      "request": {
        "parameters": [
          { "name": "id", "type": "int", "required": true }
        ]
      },
      "response": {
        "fields": [
          { "name": "mensagem", "type": "string" }
        ]
      }
    }
  ]
}
```
- **Resposta Esperada:**
```text
Arquivo WADL gerado: ProdutoService.wadl
```

### **6. Baixar Arquivo WADL**
- **Endpoint:** `GET /wadl/download/{fileName}`
- **Descrição:** Baixa um arquivo WADL gerado.
- **Exemplo de uso:**
```sh
http://localhost:8080/wadl/download/ProdutoService.wadl
```

## Estrutura do Projeto
```
.
├── src/main/java/com/exemplo/soagenerator
│   ├── controller/XsdGeneratorController.java
│   ├── controller/WsdlGeneratorController.java
│   ├── controller/WadlGeneratorController.java
│   ├── dto/XsdField.java
│   ├── dto/XsdObject.java
│   ├── dto/XsdRequest.java
│   ├── dto/WadlRequest.java
│   ├── service/XsdGeneratorService.java
│   ├── service/WsdlGeneratorService.java
│   ├── service/WadlGeneratorService.java
├── generated_xsd/  # Pasta onde os arquivos XSD gerados são salvos
├── generated_wsdl/ # Pasta onde os arquivos WSDL gerados são salvos
├── generated_wadl/ # Pasta onde os arquivos WADL gerados são salvos
├── pom.xml
├── README.md
```

## Melhorias Futuras
- Adicionar suporte para namespaces personalizados no XSD e WSDL.
- Criar uma interface web para interação visual.
- Melhorar validações e tratamento de erros.

---
Desenvolvido para integração com **SOA Suite 12c** 🚀

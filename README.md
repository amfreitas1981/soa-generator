# soa-generator
Criação arquivos XSD, WSDL e WADL para implementar serviços SOA

# Gerador de Arquivos XSD - SOA Suite 12c

## Visão Geral
Este projeto tem como objetivo gerar dinamicamente arquivos XSD a partir de uma API desenvolvida com **Spring Boot 3**. A aplicação permite definir os campos de entrada e saída, além de objetos complexos, e gerar os arquivos `.xsd` correspondentes, que podem ser baixados posteriormente.

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

## Estrutura do Projeto
```
.
├── src/main/java/com/exemplo/soagenerator
│   ├── controller/XsdGeneratorController.java
│   ├── dto/XsdField.java
│   ├── dto/XsdObject.java
│   ├── dto/XsdRequest.java
│   ├── service/XsdGeneratorService.java
├── generated_xsd/  # Pasta onde os arquivos XSD gerados são salvos
├── pom.xml
├── README.md
```

## Melhorias Futuras
- Adicionar suporte para namespaces personalizados no XSD.
- Criar uma interface web para interação visual.
- Melhorar validações e tratamento de erros.

---
Desenvolvido para integração com **SOA Suite 12c** 🚀


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
    "serviceName": "PedidoService",
    "methods": ["GET", "POST", "PUT", "DELETE"]
}
```
- **Resposta Esperada:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<application xmlns="http://wadl.dev.java.net/2009/02">
    <resources base="http://localhost:8080/api/">
        <resource path="pedidoservice">
            <method name="GET"/>
            <method name="POST"/>
            <method name="PUT"/>
            <method name="DELETE"/>
        </resource>
    </resources>
</application>
```

## Estrutura do Projeto
```
.
├── src/main/java/com/exemplo/soagenerator
│   ├── controller/XsdGeneratorController.java
│   ├── controller/WsdlGeneratorController.java
│   ├── controller/WadlController.java
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

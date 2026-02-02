# Sistema de Gerenciamento de Locadora de Veículos (INF008)

Sistema de Locação de veículos desenvolvido com caráter avaliativo referente a disciplina **INF008 - Programação Orientada a Objetos**. O projeto consiste em uma aplicação JavaFX para gestão de locações, utilizando uma arquitetura baseada em microkernel e plug-ins.

## Tecnologias Utilizadas: 
**Java 25.0.1**; **JavaFX** para interface gráfica; **MariaDB** via Docker para persistência de dados; **Maven** para gerenciamento de dependências e build.

## Arquitetura:
O sistema utiliza um **Esqueleto de Microkernel** que permite a extensão de funcionalidades via plug-ins; **Plug-ins de Veículos:** Econômico, Compact, SUV, Luxo, VAN e Elétrico; **Plug-ins de Relatórios:** Relatório 1 (Gráfico de Combustível) e Relatório 2 (Tabela de Locações).

## Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados.
- Java JDK 25 instalado.
- Maven instalado.
  
1. Configurar o Banco de Dados: No diretório raiz do projeto, execute o container do MariaDB:
   
`docker-compose up -d`

2. Compilar o Projeto: Utilize o Maven para limpar e compilar todos os módulos:

`mvn clean install`

3. Executar a Aplicação: Navegue até o módulo principal (kernel) e execute:

`mvn javafx:run`

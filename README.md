### Alunos:
#### João Éverton de Almeida dos Santos
#### Uriel Correia dos Santos

# Sistema de Gerenciamento de Locadora de Veículos (INF008)

Sistema de Locação de veículos desenvolvido com caráter avaliativo referente a disciplina **INF008 - Programação Orientada a Objetos**. O projeto consiste em uma aplicação JavaFX para gestão de locações, utilizando uma arquitetura baseada em microkernel e plug-ins.

## Tecnologias Utilizadas: 
**Java 25.0.1**; **JavaFX** para interface gráfica; **MariaDB** via Docker para persistência de dados; **Maven** para gerenciamento de dependências e build.

## Arquitetura:
O sistema utiliza um **Esqueleto de Microkernel** que permite a extensão de funcionalidades via plug-ins; **Plug-ins de Veículos:** Econômico, Compact, SUV, Luxo, VAN e Elétrico; **Plug-ins de Relatórios:** Relatório 1 (Gráfico de Combustível) e Relatório 2 (Tabela de Locações).

## Como Executar:
### Pré-requisitos
- Docker e Docker Compose instalados.
- Java JDK 25.
- Maven instalado.

#### 1. Configurar o Banco de Dados
No diretório raiz do projeto, suba o container do MariaDB:

`docker-compose up -d`

⚠️ Problemas com a porta? O projeto está configurado por padrão para a porta 3306. Se você já tiver outro banco rodando nessa porta, altere o mapeamento no arquivo docker-compose.yml e atualize a URL de conexão no arquivo: \app\src\main\java\br\edu\ifba\inf008\DatabaseConnection.java.

#### 2. Compilar o Projeto
Antes de rodar, instale as dependências e compile os módulos:

`mvn clean install`

#### 3. Executar a Aplicação
Você pode iniciar o sistema de três formas, dependendo da sua preferência:

##### -> Opção A: Maven (Via Módulo Kernel)
Se estiver no terminal e quiser usar o plugin do JavaFX:

`mvn javafx:run -pl kernel`

##### -> Opção B: Maven (Via Classe Launcher) - Ideal para Linux/macOS
Caso prefira rodar diretamente pelo executável Java do Maven:

`mvn exec:java "-Dexec.mainClass=br.edu.ifba.inf008.Launcher" -pl app`

##### -> Opção C: IntelliJ IDEA (Interface Visual)
- Abra o painel lateral do Maven e clique em "Reload All Maven Projects";
- Navegue até o módulo app (ou kernel);
- Localize o arquivo br.edu.ifba.inf008.Launcher;
- Clique com o botão direito e selecione Run 'Launcher.main()';

## Vídeo explicativo
https://www.youtube.com/watch?v=rYfKLTBaKLI

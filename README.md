# Sistema de Gerenciamento de Biblioteca Universitária

Projeto final da disciplina de **Engenharia de Software (SOFT003)** — implementação da camada de domínio e da suíte de testes unitários para um sistema de gerenciamento de biblioteca universitária, desenvolvido em **Java** com **JUnit 5**.

## Equipe

* Bruno Henrique Duarte
* João Maestri Ferreira
* João Paulo Ersching

## Sobre o Projeto

O sistema automatiza o controle de acervo, usuários, empréstimos, devoluções, renovações, reservas em fila de espera, cálculo automático de multas por tipo de usuário e envio de notificações por e-mail ou SMS.

A arquitetura segue fielmente o diagrama de classes UML do projeto e aplica quatro padrões de projeto:

* **Strategy (`PoliticaEmprestimo`):** encapsula regras de limite de empréstimos, prazo máximo, renovações e valor de multa diária para `PoliticaAluno`, `PoliticaProfessor` e `PoliticaFuncionario`.
* **State (`EstadoExemplar`):** controla as transições de estado físico dos exemplares (`DisponivelState`, `EmprestadoState` e `ReservadoState`).
* **Observer (`Observador` / `ItemAcervo` / `Cadastro`):** notifica o próximo usuário da fila de reservas quando um exemplar do item reservado é devolvido.
* **Factory Method (`ItemAcervoFactory` e `NotificacaoFactory`):** desacopla a criação de itens do acervo (`Livro`, `Midia`, `Periodico`) e dos canais de notificação (`EmailNotificacao`, `SmsNotificacao`).

## Estrutura do Repositório

```text
sistema-biblioteca-soft/
├── lib/
│   └── junit-platform-console-standalone.jar   # Dependência standalone do JUnit 5
├── src/                                        # 32 classes, interfaces e enumerações do UML
└── test/                                       # 5 classes de testes unitários (20 testes)
```

## Suíte de Testes Unitários

Os testes foram concentrados nas regras de negócio críticas e nos pontos de extensão dos padrões de projeto:

* **`EmprestimoServiceTest`:** criação de empréstimos, limites por política, cálculo e pagamento de multas, bloqueio por pendências, renovação, fila de reservas e disparo de notificações por e-mail e SMS.
* **`PoliticaEmprestimoTest`:** validação das regras distintas e do cálculo de multas para aluno, professor e funcionário.
* **`EstadoExemplarTest`:** transições válidas e exceções em transições inválidas da máquina de estados dos exemplares.
* **`AcervoFactoryTest`:** criação polimórfica via fábricas, buscas no acervo (título, autor, categoria e código) e relatórios gerenciais.
* **`CorrecoesTest`:** cenários de borda e consistência (IDs únicos entre itens distintos, atomicidade em empréstimos múltiplos, exclusividade de retirada de exemplar reservado pelo dono da reserva, cancelamento de reserva, bloqueio de reservas duplicadas, relatório de atrasados e restrições de renovação/usuário inativo).

## Como Compilar e Executar os Testes

### Pré-requisitos
* **Java JDK 11 ou superior** instalado (`javac` e `java` disponíveis no `PATH`).

### Via Terminal (Linux / macOS)

A partir da raiz do repositório, execute:

```bash
mkdir -p bin
javac -cp lib/junit-platform-console-standalone.jar -d bin src/*.java test/*.java
java -jar lib/junit-platform-console-standalone.jar execute --class-path bin --scan-class-path
```

### Via Terminal (Windows - PowerShell)

```powershell
New-Item -ItemType Directory -Force -Path bin | Out-Null
javac -cp lib/junit-platform-console-standalone.jar -d bin src/*.java test/*.java
java -jar lib/junit-platform-console-standalone.jar execute --class-path bin --scan-class-path
```

### Via VS Code

Com a extensão **Extension Pack for Java** (incluindo *Test Runner for Java*) instalada, abra o projeto, acesse a aba **Testing** na barra lateral esquerda e clique em **Run All Tests**.
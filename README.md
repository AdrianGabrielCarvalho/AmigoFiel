# AmigoFiel

Projeto desenvolvido na disciplina de Programação Orientada a Objetos. O sistema foi feito em Java com Swing e ajuda a organizar o processo de adoção de animais.

## Funcionalidades

- cadastro, listagem e busca de animais;
- cadastro e consulta de adotantes;
- registro, conclusão e cancelamento de adoções;
- verificação de compatibilidade entre animal e adotante;
- persistência local em arquivos de texto;
- interface gráfica com Swing.

## O que foi usado no projeto

- programação orientada a objetos;
- herança e classe abstrata;
- sobrescrita e polimorfismo;
- composição e agregação;
- coleções com `ArrayList`;
- tratamento de exceções personalizadas;
- organização do código em pacotes de modelo, interface, controle e persistência.

## Estrutura

```text
src/
├── app/          Ponto de entrada da aplicação
├── control/      Regras e controladores do sistema
├── exceptions/   Exceções personalizadas
├── model/        Entidades e repositórios
├── persistence/  Leitura e escrita dos arquivos
└── view/         Interface gráfica com Swing
txt/              Dados locais gerados durante a execução
docs/             Apresentação acadêmica do projeto
```

## Requisitos

- Java 11 ou superior;
- uma IDE Java, como IntelliJ IDEA, ou o JDK disponível no terminal.

## Como executar

Abra a pasta do projeto na IDE, marque `src` como diretório de código-fonte e execute a classe `app.Main`.

Em um terminal compatível com Bash:

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

Os dados cadastrados ficam na pasta `txt/`. Os arquivos dessa pasta não são enviados ao GitHub porque podem conter CPF e telefone usados nos testes.

## Autor

Adrian Gabriel Carvalho Sousa Silva.

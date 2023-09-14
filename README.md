# Web Crawler para Coleta de Dados da ANS (Agência Nacional de Saúde)

Este projeto é um Web Crawler desenvolvido para coletar dados do site da Agência Nacional de Saúde (ANS) relacionados à documentação do padrão TISS (Troca de Informações na Saúde Suplementar), histórico de versões dos componentes TISS e tabelas relacionadas. O objetivo é automatizar a tarefa de baixar documentos e coletar informações relevantes dessas páginas.

## Tarefas

### Task 1: Baixar Documentos do Padrão TISS

1. O bot deve acessar o site utilizando os recursos aprendidos no Nível ZG, no caso, a HTTPBuilder.
2. Acessar o [site da ANS](https://www.gov.br/ans/pt-br).
3. Acessar o campo "Espaço do Prestador de Serviços de Saúde".
4. Acessar o campo "TISS - Padrão para Troca de Informação de Saúde Suplementar".
5. Acessar o campo "Padrão TISS Versão Mês/Ano".
6. Realizar o parser no HTML de modo a obter os elementos da tabela de documentos.
7. Baixar o arquivo de Componente de Comunicação, na tabela de arquivos do padrão TISS, e salvar em uma pasta de Downloads no mesmo diretório do projeto (exemplo: "./Downloads/Arquivos_padrao_TISS").

### Task 2: Coletar Dados do Histórico das Versões dos Componentes TISS

1. Acessar o [site da ANS](https://www.gov.br/ans/pt-br).
2. Acessar o campo "Histórico das versões dos Componentes do Padrão TISS".
3. Coletar, na tabela, os dados de competência, publicação e início de vigência a partir da competência de data jan/2016.
4. Armazenar os dados coletados em um arquivo de texto, por exemplo, "Historico.txt".

### Task 3: Baixar a Tabela de Erros no Envio para a ANS

1. Acessar o [site da ANS](https://www.gov.br/ans/pt-br).
2. Acessar o campo "Tabelas relacionadas".
3. Baixar a "Tabela de erros no envio para a ANS" (arquivo .xlsx).
4. Salvar o arquivo em uma pasta de Downloads no mesmo diretório do projeto (exemplo: "./Downloads/Arquivos_Relacionados").

## Ferramentas e Bibliotecas Utilizadas

- Linguagem: Groovy
- Ferramenta de Build: Gradle
- Bibliotecas:
  - HTTPBuilder NG (https://http-builder-ng.github.io/http-builder-ng/)
  - Jsoup (https://jsoup.org/)

## Contribuição

Se você quiser contribuir para o projeto, fique à vontade para abrir um pull request. Qualquer ajuda é bem-vinda!

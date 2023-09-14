package org.webCrawler.menu.tasks


class MenuTasks {

    MenuTabelasRelacionadas menuTabelasRelacionadas
    MenuDownloadHistorico menuDownloadHistorico
    MenuDownloadDocumentacao menuDownloadDocumentacao

    MenuTasks() {
        menuTabelasRelacionadas = new MenuTabelasRelacionadas()
        menuDownloadHistorico = new MenuDownloadHistorico()
        menuDownloadDocumentacao = new MenuDownloadDocumentacao()
    }

    void exibirMenu(Reader reader) {
        while (true) {
            println "Menu :"
            println "1. Gerenciar Tabelas Relacionadas"
            println "2. Gerenciar Download de Historico"
            println "3. Gerenciar Download da Documentação"
            println "4. sair"
            print("Escolha uma opção: ")

            int opcao = Integer.parseInt(reader.readLine())

            switch (opcao) {
                case 1:
                    menuTabelasRelacionadas.exibirMenu()
                    break
                case 2:
                    menuDownloadHistorico.exibirMenu()
                    break
                case 3:
                    menuDownloadDocumentacao.exibirMenu()
                    break
                case 4:
                    return
                default:
                    println "Opção inválida. Tente novamente."
            }
        }
    }
}


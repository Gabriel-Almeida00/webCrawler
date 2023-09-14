package org.webCrawler.menu.tasks

import org.webCrawler.tasks.tabelasRelacionadas.TabelasRelacionadas

class MenuTabelasRelacionadas {
    TabelasRelacionadas tabelasRelacionadas = new TabelasRelacionadas()

    void exibirMenu() {
        int escolha = -1

        while (escolha != 0) {
            println("Menu:")
            println("1 - Baixar tabelas relacionadas")
            println("0 - Sair")
            print("Escolha uma opção: ")

            escolha = System.in.newReader().readLine().toInteger()

            switch (escolha) {
                case 1:
                    tabelasRelacionadas.processar()
                    break
                case 0:
                    println("Saindo...")
                    break
                default:
                    println("Opção inválida. Tente novamente.")
                    break
            }
        }
    }
}


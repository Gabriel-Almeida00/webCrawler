package org.webCrawler.menu


import org.webCrawler.menu.email.MenuEmail
import org.webCrawler.menu.email.MenuEnviaEmail
import org.webCrawler.menu.tasks.MenuTasks

class Menu {
    MenuEmail menuEmail
    MenuEnviaEmail menuEnviaEmail
    MenuTasks menuTasks

    Menu() {
        menuEmail = new MenuEmail()
        menuEnviaEmail = new MenuEnviaEmail()
        menuTasks = new MenuTasks()
    }

    void exibirMenu(Reader reader) {
        while (true) {
            println "Menu :"
            println "1. Gerenciar emails"
            println "2. Gerenciar envio de email"
            println "3. Gerenciar  tasks "
            println "4. sair"
            print("Escolha uma opção: ")

            int opcao = Integer.parseInt(reader.readLine())
            switch (opcao) {
                case 1:
                    menuEmail.exibirMenu()
                    break
                case 2:
                    menuEnviaEmail.exibirMenu()
                    break
                case 3:
                    menuTasks.exibirMenu(reader)
                    break
                case 4:

                    return
                default:
                    println "Opção inválida. Tente novamente."
            }
        }
    }
}

package org.webCrawler.menu.email

import org.webCrawler.email.EnviarEmail

class MenuEnviaEmail {
    EnviarEmail enviarEmail

    MenuEnviaEmail() {
        enviarEmail = new EnviarEmail()
    }

    void exibirMenu() {
        def escolha = -1

        while (escolha != 2) {
            println("Menu:")
            println("1 - Enviar e-mail")
            println("2 - Sair")
            print("Escolha uma opção: ")

            escolha = System.in.newReader().readLine().toInteger()

            switch (escolha) {
                case 1:
                    enviarEmail()
                    break
                case 2:
                    println("Saindo...")
                    break
                default:
                    println("Opção inválida. Tente novamente.")
                    break
            }
        }
    }

    void enviarEmail() {
        println("Enviando email......")
        List<String> listaDeEmails = new File
                ("emails/emails_interessados.txt").readLines()

        listaDeEmails.each { email ->
            enviarEmail.enviarEmail(email)
        }
    }
}


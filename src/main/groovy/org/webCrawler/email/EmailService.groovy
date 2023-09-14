package org.webCrawler.email

class EmailService {

    EmailService() {
    }

    void salvarEmails(String caminhoArquivo, List<String> emails) {
        try {
            File file = new File(caminhoArquivo)
            file.parentFile.mkdirs()
            file.withWriter { writer ->
                emails.each { email ->
                    writer.write(email + '\n')
                }
            }
            println("Dados salvos em $caminhoArquivo")
        } catch (IOException e) {
            println("Erro ao salvar os dados: ${e.message}")
        }
    }

     List<String> lerEmails(String caminhoArquivo) {
        List<String> emails = []
        try {
            File file = new File(caminhoArquivo)
            if (file.exists()) {
                emails = file.readLines()
            } else {
                println("O arquivo $caminhoArquivo não existe.")
            }
        } catch (IOException e) {
            println("Erro ao ler o arquivo: ${e.message}")
        }
        return emails
    }

     void adicionarEmails(String caminhoArquivo, List<String> novosEmails) {
        List<String> emailsAtuais = lerEmails(caminhoArquivo)
        emailsAtuais.addAll(novosEmails)
        salvarEmails(caminhoArquivo, emailsAtuais)
        println("E-mails adicionados com sucesso.")
    }

    void editarEmail(String caminhoArquivo, String emailAntigo, String emailNovo) {
        List<String> emailsAtuais = lerEmails(caminhoArquivo)
        if (emailsAtuais.contains(emailAntigo)) {
            emailsAtuais.remove(emailAntigo)
            emailsAtuais.add(emailNovo)
            salvarEmails(caminhoArquivo, emailsAtuais)
            println("E-mail editado com sucesso: $emailAntigo -> $emailNovo")
        } else {
            println("E-mail antigo não encontrado.")
        }
    }

     void removerEmails(String caminhoArquivo, String emailParaRemover) {
        List<String> emailsAtuais = lerEmails(caminhoArquivo)
        emailsAtuais.remove(emailParaRemover)
        salvarEmails(caminhoArquivo, emailsAtuais)
        println("E-mail removido com sucesso: $emailParaRemover")
    }

}

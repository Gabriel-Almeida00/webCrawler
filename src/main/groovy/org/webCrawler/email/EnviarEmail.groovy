package org.webCrawler.email

import org.webCrawler.config.Config
import org.webCrawler.tasks.downloadsDocumentacao.DownloadDocumentacao

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EnviarEmail {
    DownloadDocumentacao downloadDocumentacao

    EnviarEmail() {
        downloadDocumentacao = new DownloadDocumentacao()
    }

    void enviarEmail(String destinatario) {
        String remetente = "gaamdal557@gmail.com"
        String senha = Config.SENHA

        Properties props = new Properties()
        props.setProperty("mail.smtp.host", "smtp.gmail.com")
        props.setProperty("mail.smtp.port", "587")
        props.setProperty("mail.smtp.auth", "true")
        props.setProperty("mail.smtp.starttls.enable", "true")

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha)
            }
        })

        try {
            MimeMessage message = new MimeMessage(session)
            message.setFrom(new InternetAddress(remetente))
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario))
            message.setSubject("Relatório de Arquivos Baixados")

            MimeBodyPart messageBodyPart = new MimeBodyPart()
            messageBodyPart.setText("Olá,\n\nSegue o relatório com os arquivos baixados:  " +
                    "\n\no seguinte link é referente ao arquivo de Componente de conteúdo e estrutura: "
                    + downloadDocumentacao.lerSegundoLinkDoArquivo())

            Multipart multipart = new MimeMultipart()
            multipart.addBodyPart(messageBodyPart)

            ArrayList<String> caminhosDosArquivos = [
                    'downloads/Arquivos_padrao_TISS/PadroTISS_ComponentedeContedoeEstrutura_202211.zip',
                    'downloads/Arquivos_padrao_TISS/PadroTISS_segurana_202305.zip',
                    'downloads/Arquivos_padrao_TISS/PadroTISSComunicao202301.zip',
                    'downloads/Historico/Historico.txt',
                    'downloads/Arquivos_Relacionados/Tabelaerrosenvioparaanspadraotiss__1_.xlsx'

            ]

            caminhosDosArquivos.each { caminho ->
                MimeBodyPart attachmentPart = new MimeBodyPart()
                attachmentPart.attachFile(new File(caminho))
                multipart.addBodyPart(attachmentPart)
            }

            message.setContent(multipart)

            Transport.send(message)
            println("E-mail enviado para ${destinatario}")
        } catch (MessagingException e) {
            println("Erro ao enviar e-mail: ${e.message} ${e.printStackTrace()}")
        }
    }

}
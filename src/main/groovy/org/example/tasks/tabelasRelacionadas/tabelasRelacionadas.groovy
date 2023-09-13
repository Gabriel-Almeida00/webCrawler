package org.example.tasks.tabelasRelacionadas

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import static groovyx.net.http.HttpBuilder.configure

class tabelasRelacionadas {
    static void main(String[] args) {
        String baseUrl = 'https://www.gov.br/ans/pt-br'

        Document page = configure {
            request.uri = baseUrl
        }.get()

        Elements firstLinkElement = page.select('a[href="https://www.gov.br/ans/pt-br/assuntos/prestadores"]')

        if (!firstLinkElement.isEmpty()) {
            String firstLinkHref = firstLinkElement.attr('href')

            println "Primeiro link href: ${firstLinkHref}"

            Document secondPage = configure {
                request.uri = firstLinkHref
            }.get()

            Elements secondLinkElement = secondPage.select('a.govbr-card-content')

            if (!secondLinkElement.isEmpty()) {
                String secondLinkHref = secondLinkElement.attr('href')

                println "Segundo link href: ${secondLinkHref}"

                Document thirdPage = configure {
                    request.uri = secondLinkHref
                }.get()

                Elements thirdLinkElement = thirdPage.select('p.callout a.alert-link.internal-link')

                Element desiredLink = null
                for (Element link : thirdLinkElement) {
                    if (link.text() == "Clique aqui para acessar as planilhas") {
                        desiredLink = link
                        break
                    }
                }

                if (desiredLink != null) {
                    String thirdLinkHref = desiredLink.attr('href')

                    println "Terceiro link href: ${thirdLinkHref}"

                    Document downloadPage = configure {
                        request.uri = thirdLinkHref
                    }.get()

                    Elements links = downloadPage.select('a[href$=.xlsx]')

                    for (Element link : links) {
                        String downloadLink = link.attr("href")
                        String fileName = downloadLink.substring(downloadLink.lastIndexOf('/') + 1)
                        String downloadFolderPath = "downloads/Arquivos_Relacionados"
                        String filePath = "${downloadFolderPath}/${fileName}"

                        File file = new File(filePath)
                        file.parentFile.mkdirs()

                        if (!file.exists()) {
                            HttpBuilder downloadConfig = configure {
                                request.uri = downloadLink
                            }

                            try {
                                byte[] responseBytes = downloadConfig.get()
                                file.withOutputStream { outputStream ->
                                    outputStream << responseBytes
                                }
                                println "Arquivo baixado: ${filePath}"
                            } catch (Exception e) {
                                println "Erro ao baixar o arquivo: ${e.message}"
                            }
                        } else {
                            println "O arquivo já existe: ${filePath}"
                        }
                    }

                } else {
                    println "Terceiro link não encontrado."
                }
            } else {
                println "Segundo link não encontrado."
            }
        } else {
            println "Primeiro link não encontrado."
        }

    }
}


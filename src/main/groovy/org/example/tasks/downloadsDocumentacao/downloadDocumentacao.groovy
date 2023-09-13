package org.example.tasks.downloadsDocumentacao

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

@Grab('io.github.http-builder-ng:http-builder-ng-core:1.0.4')
@Grab('org.jsoup:jsoup:1.9.2')
import static groovyx.net.http.HttpBuilder.configure

class downloadDocumentacao {
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

                Elements thirdLinkElement = thirdPage.select('p.callout a.internal-link')

                if (!thirdLinkElement.isEmpty()) {
                    String thirdLinkHref = thirdLinkElement.attr('href')

                    println "Terceiro link href: ${thirdLinkHref}"

                    Document downloadPage = configure {
                        request.uri = thirdLinkHref
                    }.get()

                    String downloadFolderPath = "downloads/Arquivos_padrao_TISS"
                    Elements links = downloadPage.select('div.table-responsive table.table-bordered tbody tr td:nth-child(3) a[href$=.zip]')

                    links.each { link ->
                        String downloadLink = link.attr("href")
                        String fileName = downloadLink.substring(downloadLink.lastIndexOf('/') + 1)
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
                            println "Arquivo já existe: ${filePath}"
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

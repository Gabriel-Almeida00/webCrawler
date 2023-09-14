package org.webCrawler.tasks.downloadsDocumentacao

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

@Grab('io.github.http-builder-ng:http-builder-ng-core:1.0.4')
@Grab('org.jsoup:jsoup:1.9.2')
import static groovyx.net.http.HttpBuilder.configure

class DownloadDocumentacao {
    private static final String BASE_URL = 'https://www.gov.br/ans/pt-br'
    private static final String DOWNLOAD_PATH = 'downloads/Arquivos_padrao_TISS'
    private static final String LINK_FILE_PATH = 'downloads/Arquivos_padrao_TISS/Link.txt'

    String linkArquivoZip

    void downloadArquivosPadraoTISS() {
        Document page = carregarPagina(BASE_URL)
        Elements firstLinkElement = page.select('a[href="https://www.gov.br/ans/pt-br/assuntos/prestadores"]')

        if (!firstLinkElement.isEmpty()) {
            String firstLinkHref = firstLinkElement.attr('href')
            println "Primeiro link href: ${firstLinkHref}"

            Document secondPage = carregarPagina(firstLinkHref)
            processarSegundaPagina(secondPage)
        } else {
            println "Primeiro link não encontrado."
        }
    }

    Document carregarPagina(String url) {
        return configure {
            request.uri = url
        }.get()
    }

    void processarSegundaPagina(Document secondPage) {
        Elements secondLinkElement = secondPage.select('a.govbr-card-content')
        if (!secondLinkElement.isEmpty()) {
            String secondLinkHref = secondLinkElement.attr('href')
            println "Segundo link href: ${secondLinkHref}"

            Document thirdPage = carregarPagina(secondLinkHref)
            processarTerceiraPagina(thirdPage)
        } else {
            println "Segundo link não encontrado."
        }
    }

    void processarTerceiraPagina(Document thirdPage) {
        Elements thirdLinkElement = thirdPage.select('p.callout a.internal-link')
        if (!thirdLinkElement.isEmpty()) {
            String thirdLinkHref = thirdLinkElement.attr('href')
            println "Terceiro link href: ${thirdLinkHref}"

            Document downloadPage = carregarPagina(thirdLinkHref)
            processarPaginaDeDownload(downloadPage)
        } else {
            println "Terceiro link não encontrado."
        }
    }

    void processarPaginaDeDownload(Document downloadPage) {
        Elements links = downloadPage.select('div.table-responsive table.table-bordered tbody tr td:nth-child(3) a[href$=.zip]')

        if (linkArquivoZip == null && links.size() >= 2) {
            linkArquivoZip = links.get(1).attr("href")
            salvarPrimeiroLinkEmArquivo(linkArquivoZip)
            links.remove(1)
        }

        links.each { link ->
            String downloadLink = link.attr("href")
            String fileName = downloadLink.substring(downloadLink.lastIndexOf('/') + 1)
            String filePath = "${DOWNLOAD_PATH}/${fileName}"

            processarDownload(downloadLink, filePath)
        }
    }

    void salvarPrimeiroLinkEmArquivo(String link) {
        new File(LINK_FILE_PATH).withWriter { writer ->
            writer.write(link)
        }
    }

    String lerSegundoLinkDoArquivo() {
        try {
            return new File(LINK_FILE_PATH).text
        } catch (Exception e) {
            println "Erro ao ler o segundo link do arquivo: ${e.message}"
            return null
        }
    }


    void processarDownload(String downloadLink, String filePath) {
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
}
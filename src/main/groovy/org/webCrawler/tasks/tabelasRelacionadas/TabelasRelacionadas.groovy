package org.webCrawler.tasks.tabelasRelacionadas

import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import static groovyx.net.http.HttpBuilder.configure

class TabelasRelacionadas {
    private static final String BASE_URL = 'https://www.gov.br/ans/pt-br'
    private static final String DOWNLOAD_PATH = 'downloads/Arquivos_Relacionados'

    void processar() {
        Document page = buscarDocumento(BASE_URL)
        Element primeiroLinkElemento = buscarPrimeiroLink(page)

        if (primeiroLinkElemento == null) {
            println "Primeiro link não encontrado."
            return
        }

        String primeiroLinkHref = primeiroLinkElemento.attr('href')
        println "Primeiro link href: ${primeiroLinkHref}"

        Document segundaPagina = buscarDocumento(primeiroLinkHref)
        Element segundoLinkElemento = buscarSegundoLink(segundaPagina)

        if (segundoLinkElemento == null) {
            println "Segundo link não encontrado."
            return
        }

        String segundoLinkHref = segundoLinkElemento.attr('href')
        println "Segundo link href: ${segundoLinkHref}"

        Document terceiraPagina = buscarDocumento(segundoLinkHref)
        Element terceiroLinkElemento = buscarTerceiroLink(terceiraPagina)

        if (terceiroLinkElemento == null) {
            println "Terceiro link não encontrado."
            return
        }

        String terceiroLinkHref = terceiroLinkElemento.attr('href')
        println "Terceiro link href: ${terceiroLinkHref}"
        processarArquivosRelacionados(terceiroLinkHref)
    }

    private Document buscarDocumento(String url) {
        return configure {
            request.uri = url
        }.get() as Document
    }

    private Element buscarPrimeiroLink(Document page) {
        Elements elementos = page.select('a[href="https://www.gov.br/ans/pt-br/assuntos/prestadores"]')
        return elementos.isEmpty() ? null : elementos.first()
    }

    private Element buscarSegundoLink(Document page) {
        Elements elementos = page.select('a.govbr-card-content')
        return elementos.isEmpty() ? null : elementos.first()
    }

    private Element buscarTerceiroLink(Document page) {
        Elements elementos = page.select('p.callout a.alert-link.internal-link')
        for (Element link : elementos) {
            if (link.text() == "Clique aqui para acessar as planilhas") {
                return link
            }
        }
        return null
    }

    private void processarArquivosRelacionados(String url) {
        Document downloadPage = buscarDocumento(url)
        Elements links = downloadPage.select('a[href$=.xlsx]')

        for (Element link : links) {
            String downloadLink = link.attr("href")
            String fileName = downloadLink.substring(downloadLink.lastIndexOf('/') + 1)
            String filePath = "${DOWNLOAD_PATH}/${fileName}"

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
    }
}
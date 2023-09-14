package org.webCrawler.tasks.downloadHistorico

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.text.SimpleDateFormat

import static groovyx.net.http.HttpBuilder.configure

class DownloadHistorico {
    private static final String BASE_URL = 'https://www.gov.br/ans/pt-br'
    private static final String DOWNLOAD_PATH = 'downloads/Historico/Historico.txt'
    private static final String COMPETENCE_START_DATE = 'jan/2016'

    void downloadHistorico() {
        Document page = carregarPagina(BASE_URL)

        Elements firstLinkElement = page.select('a[href="https://www.gov.br/ans/pt-br/assuntos/prestadores"]')

        if (!firstLinkElement.isEmpty()) {
            String firstLinkHref = firstLinkElement.attr('href')

            println "Primeiro link href: ${firstLinkHref}"

            Document secondPage = carregarPagina(firstLinkHref)

            Elements secondLinkElement = secondPage.select('a.govbr-card-content')

            if (!secondLinkElement.isEmpty()) {
                String secondLinkHref = secondLinkElement.attr('href')

                println "Segundo link href: ${secondLinkHref}"

                Document thirdPage = carregarPagina(secondLinkHref)

                Elements thirdLinkElement = thirdPage.select('a.alert-link.internal-link[data-tippreview-enabled="true"]')

                if (!thirdLinkElement.isEmpty()) {
                    String thirdLinkHref = thirdLinkElement.attr('href')

                    println "Terceiro link href: ${thirdLinkHref}"

                    Document downloadPage = carregarPagina(thirdLinkHref)

                    Elements tabela = downloadPage.select("table")

                    List<Historico> historicoList = processarTabelaHistorico(tabela)

                    salvarHistoricoEmArquivo(historicoList, DOWNLOAD_PATH)
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

    List<Historico> processarTabelaHistorico(Elements tabela) {
        List<Historico> historicoList = []

        tabela.select("tr").each { linha ->
            Elements colunas = linha.select("td")

            if (colunas.size() >= 3) {
                String competencia = colunas[0].text().trim()
                String publicacao = colunas[1].text().trim()
                String inicioVigencia = colunas[2].text().trim()

                SimpleDateFormat formatoData = new SimpleDateFormat("MMM/yyyy")
                Date dataCompetencia = formatoData.parse(competencia)
                Date dataInicio = formatoData.parse(COMPETENCE_START_DATE)

                if (dataCompetencia >= dataInicio) {
                    historicoList.add(new Historico(competencia, publicacao, inicioVigencia))
                }
            }
        }

        return historicoList
    }

    Document carregarPagina(String url) {
        return configure {
            request.uri = url
        }.get()
    }

    void salvarHistoricoEmArquivo(List<Historico> historicoList, String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo)
        arquivo.parentFile.mkdirs()

        arquivo.withWriter { writer ->
            historicoList.each { historico ->
                writer.write(historico.toText() + '\n')
            }
        }

        println("Dados salvos em $caminhoArquivo")
    }
}
